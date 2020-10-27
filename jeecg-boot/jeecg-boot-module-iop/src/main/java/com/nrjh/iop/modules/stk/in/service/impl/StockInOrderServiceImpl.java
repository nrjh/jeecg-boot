package com.nrjh.iop.modules.stk.in.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nrjh.iop.modules.order.entity.BusinessTurnProcess;
import com.nrjh.iop.modules.order.entity.StkSuggest;
import com.nrjh.iop.modules.order.service.IBusinessTurnProcessService;
import com.nrjh.iop.modules.order.service.IStkSuggestService;
import com.nrjh.iop.modules.stk.in.entity.OrderStockInList;
import com.nrjh.iop.modules.stk.in.entity.StockInOrder;
import com.nrjh.iop.modules.stk.in.entity.StockInOrderLine;
import com.nrjh.iop.modules.stk.in.mapper.OrderStockInListMapper;
import com.nrjh.iop.modules.stk.in.mapper.StockInOrderLineMapper;
import com.nrjh.iop.modules.stk.in.mapper.StockInOrderMapper;
import com.nrjh.iop.modules.stk.in.service.IStockInOrderLineService;
import com.nrjh.iop.modules.stk.in.service.IStockInOrderService;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderLineVo;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderVo;

import com.nrjh.iop.modules.stk.spareparts.entity.StkStock;
import com.nrjh.iop.modules.stk.spareparts.service.IStkStockService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 入库单
 * @Author: jeecg-boot
 * @Date: 2020-08-16
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StockInOrderServiceImpl extends ServiceImpl<StockInOrderMapper, StockInOrder> implements IStockInOrderService {

    @Autowired
    private StockInOrderMapper stockInOrderMapper;
    @Autowired
    private StockInOrderLineMapper stockInOrderLineMapper;
    @Autowired
    private OrderStockInListMapper orderStockInListMapper;

    @Autowired
    private com.nrjh.iop.modules.sys.dict.mapper.IopSysDictMapper sysDictMapper;

    @Autowired
    private IStkSuggestService stkSuggestService;

    @Autowired
    private IStkStockService iStkStockService;

    @Autowired
    private IStockInOrderLineService stockInOrderLineService;

    @Autowired
    private IBusinessTurnProcessService iBusinessTurnProcessService;

    @Override
    @Transactional
    public void saveMain(StockInOrderVo stockInOrderVo) {
        System.out.println(stockInOrderVo.getPartnerId());
        StockInOrder stockInOrder = new StockInOrder();
        BeanUtils.copyProperties(stockInOrderVo, stockInOrder);
//        StringBuffer sb = new StringBuffer();
//        sb.append("CGRK").append(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
//        stockInOrder.setOrderInId(sb.toString());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        stockInOrder.setCreateId(sysUser.getId());
        stockInOrder.setCreateName(sysUser.getRealname());
        /**
         * 增加审批流程扭转
         * 1.自定义审批人的角色业务表business_turn_process
         * 2.每次新增获取当前角色（可能多个），加自定义业务表中的顺序号 查询
         * 3.新增没人顺序为1，审批的时候自动增一
         * 1003 入库审批业务id，business_type
         */
        // 获取当前用户的所有角色id
        QueryWrapper<BusinessTurnProcess> params=new QueryWrapper<>();
        params.eq("BUSINESS_SEQUENCE",2);
        params.eq("BUSINESS_ID","1003");
        params.eq("BUSINESS_TYPE",stockInOrder.getOrderType());
        params.orderByAsc("BUSINESS_SEQUENCE");
        List<BusinessTurnProcess>  businessTurnProcesses=iBusinessTurnProcessService.list(params);
        if(!CollectionUtil.isEmpty(businessTurnProcesses)){
            stockInOrder.setNextRoleId(businessTurnProcesses.get(0).getRoleId());
        }else {
            log.error("查询审批扭转业务流程下一个角色失败");
        }

        stockInOrder.setBusinessSequence(1);
        stockInOrderMapper.insert(stockInOrder);
        saveSubTable(stockInOrderVo, stockInOrder);
    }


    @Override
    @Transactional
    public void updateMain(StockInOrderVo stockInOrderVo) {
        StockInOrder stockInOrder = new StockInOrder();
        BeanUtils.copyProperties(stockInOrderVo, stockInOrder);
        if ("submit".equals(stockInOrder.getState())) {
            stockInOrder.setOrderInDate(null);
            stockInOrder.setOrderInName(null);
        }
        stockInOrderMapper.updateById(stockInOrder);

        //1.先删除子表数据
        stockInOrderLineMapper.deleteByMainId(String.valueOf(stockInOrder.getId()));
        orderStockInListMapper.deleteByInOrderId(String.valueOf(stockInOrder.getId()));

        saveSubTable(stockInOrderVo, stockInOrder);
    }

    @Override
    @Transactional
    public void delMain(String id) {
        orderStockInListMapper.deleteByInOrderId(id);
        stockInOrderLineMapper.deleteByMainId(id);
        stockInOrderMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            orderStockInListMapper.deleteByInOrderId(id.toString());
            stockInOrderLineMapper.deleteByMainId(id.toString());
            stockInOrderMapper.deleteById(id);
        }
    }

    @Override
    public List<StockInOrder> pageByInOrderAndDetail( Map map) {
        return baseMapper.selectByInOrderAndDetail( map);
    }

    @Override
    @Transactional
    public void saveSuggest(StkSuggest stkSuggest) {
        stkSuggestService.save(stkSuggest);
        StockInOrder stockInOrder = stockInOrderMapper.selectById(Integer.valueOf(stkSuggest.getOrderNo()));
        System.out.println(stockInOrder);
        if ("pass".equals(stkSuggest.getResult())) {
//            if ("normal".equals(stockInOrder.getOrderType())) {
//                QueryWrapper<StkSuggest> wrapper = new QueryWrapper<>();
//                wrapper.eq("order_no", stockInOrder.getId());
//                List<StkSuggest> list = stkSuggestService.list(wrapper);
//                if (list.size() > 1) {
//                    //修改备品备件的实际数量
//                    List<StockInOrderLineVo> stockInOrderLineVos = stockInOrderLineService.selectByMainId(stkSuggest.getOrderNo());
//                    if (!CollectionUtils.isEmpty(stockInOrderLineVos)) {
//                        for (StockInOrderLineVo stockInOrderLineVo : stockInOrderLineVos) {
//                            //获取实际入库数量
//                            BigDecimal productActualQty = stockInOrderLineVo.getProductActualQty();
//                            //获取物品编号
//                            Integer productId = stockInOrderLineVo.getProductId();
//                            //查询备品备件(通过物品Id查询)
//                            QueryWrapper queryWrapper = new QueryWrapper();
//                            queryWrapper.eq("product_id",productId);
//                            //获取备品备件
//                            StkStock stkStock = iStkStockService.getOne(queryWrapper);
//                            if (stkStock != null) {
//                                //修改为 ——> 实际入库数量 + 实际物品数量
//                                stkStock.setProductQty(productActualQty == null ? stkStock.getProductQty() : (stkStock.getProductQty() + productActualQty.intValue()));
//                                //修改虚拟库存
//                                stkStock.setVirtualQty(productActualQty == null ? stkStock.getVirtualQty() : (stkStock.getVirtualQty() + productActualQty.intValue()));
//                                //修改
//                                iStkStockService.updateById(stkStock);
//                            }
//                        }
//                    }
//                    stockInOrder.setState("submit");
//                }
//            } else {
//                stockInOrder.setState("submit");
//            }
            // 获取当前用户的所有角色id
            int sequenceNum=stockInOrder.getBusinessSequence()+1;
            QueryWrapper<BusinessTurnProcess> params=new QueryWrapper<BusinessTurnProcess>();
            params.eq("BUSINESS_SEQUENCE",sequenceNum+1);
            params.eq("BUSINESS_ID","1003");
            params.eq("BUSINESS_TYPE",stockInOrder.getOrderType());
            params.orderByAsc("BUSINESS_SEQUENCE");
            List<BusinessTurnProcess>  businessTurnProcesses=iBusinessTurnProcessService.list(params);
            if(!CollectionUtil.isEmpty(businessTurnProcesses)){
                stockInOrder.setNextRoleId(businessTurnProcesses.get(0).getRoleId());
                stockInOrder.setBusinessSequence(sequenceNum);
                stockInOrderMapper.updateById(stockInOrder);
            }else {
                log.error("审批自定义审批人的角色业务表business_turn_process 业务表查出的下一个角色数据为空时，审核流程结束");
                stockInOrder.setState("approve");
                stockInOrder.setNextRoleId("99");
                stockInOrderMapper.updateById(stockInOrder);
            }
        } else {
            stockInOrder.setState("back");
            stockInOrderMapper.updateById(stockInOrder);
        }
    }

    @Override
    @DS("master")
    public String queryDictTextByKey(String code, String key) {
        return sysDictMapper.queryDictTextByKey(code, key);
    }


    private void saveSubTable(StockInOrderVo stockInOrderVo, StockInOrder stockInOrder) {
        List<StockInOrderLineVo> stockInOrderLineList = stockInOrderVo.getStockInOrderLineList();
        if (stockInOrderLineList != null && stockInOrderLineList.size() > 0) {
            for (StockInOrderLineVo vo : stockInOrderLineList) {
                StockInOrderLine entity = new StockInOrderLine();
                BeanUtils.copyProperties(vo, entity);
                entity.setOrderId(stockInOrder.getId()); //外键设置
                entity.setContract(stockInOrder.getContract());
                //设置供应商id
                entity.setPartnerId(vo.getPartnerId());
                //获取应入库数量
                entity.setProductQty(vo.getProductPlyQty());
                entity.setPartnerName(stockInOrder.getPartnerName());
                stockInOrderLineMapper.insert(entity);
                List<OrderStockInList> orderStockInLists = vo.getOrderStockInLists();
                if (orderStockInLists != null && orderStockInLists.size() > 0) {
                    for (OrderStockInList oEntity : orderStockInLists) {
                        oEntity.setStockInOrderId(stockInOrder.getId());
                        oEntity.setStockOrderId(entity.getId()+"");
                        oEntity.setOperType("in");
                        oEntity.setStatus("1");
                        orderStockInListMapper.insert(oEntity);
                    }
                }
            }
        }
    }
}
