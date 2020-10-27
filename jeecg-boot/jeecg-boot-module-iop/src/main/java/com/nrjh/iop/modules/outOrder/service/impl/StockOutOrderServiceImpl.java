package com.nrjh.iop.modules.outOrder.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.alarm.alarmtype.service.IEquipldTreeService;
import com.nrjh.iop.modules.alarm.alarmtype.vo.EquipIdModel;
import com.nrjh.iop.modules.order.service.IPcsPurchaseOrderService;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrder;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrderLine;
import com.nrjh.iop.modules.outOrder.mapper.StockOutOrderLineMapper;
import com.nrjh.iop.modules.outOrder.mapper.StockOutOrderMapper;
import com.nrjh.iop.modules.outOrder.service.IStockOutOrderService;
import com.nrjh.iop.modules.outOrder.vo.StockOutOrderLineVO;
import com.nrjh.iop.modules.outOrder.vo.StockOutOrderVO;
import com.nrjh.iop.modules.stk.in.entity.OrderStockInList;
import com.nrjh.iop.modules.stk.in.mapper.OrderStockInListMapper;
import com.nrjh.iop.modules.stk.spareparts.entity.StkStock;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 领料出库单
 * @Author: lei-li
 * @Date: 2020-08-19
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StockOutOrderServiceImpl extends ServiceImpl<StockOutOrderMapper, StockOutOrder> implements IStockOutOrderService {

    @Autowired
    private StockOutOrderMapper stockOutOrderMapper;

    @Autowired
    private StockOutOrderLineMapper stockOutOrderLineMapper;

    @Autowired
    private OrderStockInListMapper orderStockInListMapper;

    @Autowired
    private IEquipldTreeService iEquipldTreeService;
    @Autowired
    private IPcsPurchaseOrderService iPcsPurchaseOrderService;


    @Override
    @Transactional
    public void saveMain(StockOutOrder stockOutOrder, List<StockOutOrderLine> stockOutOrderLineList) {
        //设置时间
        stockOutOrder.setCreateTime(new Date());
        //获取当前登录用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        stockOutOrder.setCreateOutName(sysUser.getRealname());
        stockOutOrder.setCreateOutBy(sysUser.getId());

        /**
         * 正常：检修工单（出库单）->仓库管理员
         * 非正常：出库单-> 运维班长（审批）->生产管理员（审批）-> 仓库管理员
         * 增加领料审批扭转流程业务
         * 1.自定义审批人的角色业务表business_turn_process
         * 2.每次新增获取当前角色（可能多个），加自定义业务表中的顺序号 查询
         * 3.新增没人顺序为1，审批的时候自动增一
         * 1002 订单审批业务id，business_type
         */
         //设置状态，若申请类型为 –检修工单,则设置状态为“待领取”
//        if (!StringUtils.isEmpty(stockOutOrder.getOrderType()) && stockOutOrder.getOrderType().equals("order")){
//            stockOutOrder.setState("approve");
//        }
        //非正常
//        if(!StringUtils.isEmpty(stockOutOrder.getOrderType()) && stockOutOrder.getOrderType().equals("urgency")){
//
//
//        }else {
//            //正常
//
//        }
        String orderType="";
        if(!StringUtils.isEmpty(stockOutOrder.getOrderType()) && stockOutOrder.getOrderType().equals("urgency")){
            orderType="urgency";
        }else {
            orderType="normal";
        }
        String nextRoleId=iPcsPurchaseOrderService.getBusinessApproveNextRoleId(1,"1002",orderType);
        stockOutOrder.setNextRoleId(nextRoleId);
        stockOutOrder.setBusinessSequence(1);
        //设置出库状态
        stockOutOrder.setOutState("freeze");
        stockOutOrderMapper.insert(stockOutOrder);
        if (stockOutOrderLineList != null && stockOutOrderLineList.size() > 0) {
            for (StockOutOrderLine entity : stockOutOrderLineList) {
                //外键设置
                entity.setOrderId(stockOutOrder.getId());
                stockOutOrderLineMapper.insert(entity);
                //减库存
                stockOutOrderMapper.updateStock(entity);
            }
        }

    }

    @Override
    @Transactional
    public void updateMain(StockOutOrder stockOutOrder, List<StockOutOrderLine> stockOutOrderLineList) {
        stockOutOrderMapper.updateById(stockOutOrder);

        //根据id查询明细中原来的计划出库的数量
        for(StockOutOrderLine stockOutOrderLine : stockOutOrderLineList){
            StockOutOrderLine st1 = stockOutOrderLineMapper.selectById(stockOutOrderLine.getId());
            stockOutOrderLine.setStartProductPlyQty(st1.getProductPlyQty());
        }
        //1.先删除子表数据
        stockOutOrderLineMapper.deleteByMainId(stockOutOrder.getId());

        //2.子表数据重新插入
        if (stockOutOrderLineList != null && stockOutOrderLineList.size() > 0) {
            for (StockOutOrderLine entity : stockOutOrderLineList) {
                //外键设置
                entity.setOrderId(stockOutOrder.getId());
                stockOutOrderLineMapper.insert(entity);
                //修改出库单需要减去的虚拟库存数量
                Integer VirtualInventory = entity.getProductPlyQty() - entity.getStartProductPlyQty();
                //减库存
                stockOutOrderMapper.updateStockByChange(entity,VirtualInventory);
            }
        }
    }

    @Override
    @Transactional
    public void delMain(Integer id) {
        stockOutOrderLineMapper.deleteByMainId(id);
        stockOutOrderMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Integer[] idList) {
        for (Integer id : idList) {
            stockOutOrderLineMapper.deleteByMainId(id);
            stockOutOrderMapper.deleteById(id);
        }
    }

    @Override
    @Transactional
    public void saveOutOrderLocator(StockOutOrderVO stockOutOrderVO) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        StockOutOrder stockOutOrder = new StockOutOrder();
        stockOutOrder.setId(stockOutOrderVO.getId());
        stockOutOrder.setState(stockOutOrderVO.getState());
        stockOutOrder.setOutState(stockOutOrderVO.getOutState());
        if("done".equals(stockOutOrderVO.getState())){
            stockOutOrder.setOuBy(sysUser.getId());
            stockOutOrder.setOutTime(new Date());
            stockOutOrder.setOuName(sysUser.getRealname());
            stockOutOrder.setOutState(stockOutOrderVO.getState());
        }
        stockOutOrderMapper.updateById(stockOutOrder);
        List<StockOutOrderLineVO> lineVOS = stockOutOrderVO.getStockOutOrderLineList();
        if (CollUtil.isEmpty(lineVOS)) {
            return;
        }
        for (StockOutOrderLineVO lineVO : lineVOS) {
            orderStockInListMapper.deleteByMainId(String.valueOf(lineVO.getId()));
            List<OrderStockInList> orderStockInLists = lineVO.getOrderStockInLists();
            StockOutOrderLine stockOutOrderLine = stockOutOrderLineMapper.selectById(lineVO.getId());
            if (stockOutOrderLine != null) {
                stockOutOrderLine.setProductActualQty(lineVO.getProductActualQty());
                stockOutOrderLineMapper.updateById(stockOutOrderLine);
            }
            if (CollUtil.isNotEmpty(orderStockInLists)) {
                for (OrderStockInList entity : orderStockInLists) {
                    entity.setStockOutOrderId(stockOutOrderVO.getId());
                    entity.setStockOrderId(lineVO.getId()+"");
                    entity.setOperType("out");
                    entity.setStatus("1");
                    orderStockInListMapper.insert(entity);
                }
            }
            if("done".equals(stockOutOrderVO.getState())) {
                //实际库存需要扣除的数量
                Integer reduceProductQty = lineVO.getProductActualQty();
                //虚拟库存实际需要扣除的数量
                Integer VirtualInventory = reduceProductQty - lineVO.getProductPlyQty();
                //修改人
                String updateBy = sysUser.getUsername();
                //修改时间
                Date updateTime = new Date();
                StkStock stkStock = new StkStock();
                stkStock.setUpdateBy(updateBy);
                stkStock.setUpdateTime(updateTime);
                stkStock.setProductNo(lineVO.getDefaultCode());
                //领料出库完成扣除库存
                stockOutOrderLineMapper.updateStock(reduceProductQty, VirtualInventory, stkStock);
            }
        }
    }

    @Override
    @Transactional
    public void backOutOrder(List<String> ids) {
        for (String id : ids) {
            stockOutOrderMapper.backOutOrderById(id);
            stockOutOrderLineMapper.backByMainUd(id);
//            orderStockInListMapper.backByOrderOutUd(id);
            //查询明细表库存信息
            StockOutOrderLine stockOutOrderLine = stockOutOrderLineMapper.selectByOrderId(id);
            //修改库存虚拟库存数量，虚拟库存回退
            stockOutOrderMapper.backStkStockById(stockOutOrderLine);
        }
    }
	@Override
	public void submitApproval(Integer id) {
		StockOutOrder stockOutOrder = new StockOutOrder();
		stockOutOrder.setId(id);
		stockOutOrder.setState("submit");
		stockOutOrderMapper.updateById(stockOutOrder);
	}

    @Override
    public List<String> queryLocation(String defaultCode) {
        List<String> locationName = stockOutOrderMapper.queryLoction(defaultCode);
        return locationName;
    }

    @Override
    public StockOutOrder queryStockOutById(String orderOutId) {
        QueryWrapper<StockOutOrder> stockOutOrderQueryWrapper = new QueryWrapper<>();
        stockOutOrderQueryWrapper.eq("order_out_id",orderOutId);
        return stockOutOrderMapper.selectOne(stockOutOrderQueryWrapper);
    }

    @Override
    public String getProdDetails(String id) {
        List<Map<String,Object>>  list=stockOutOrderMapper.getProdDetails(id);
        StringBuffer resultBuff=new StringBuffer();
        for (int i=0;i<list.size();i++){
            Map<String,Object> result=list.get(i);
            if(!CollectionUtils.isEmpty(result)){
                resultBuff.append(result.get("prodName") == null ? "" : result.get("prodName"));
                resultBuff.append("-");
                if(result.get("cateName")!=null){
                    resultBuff.append(result.get("cateName") == null ? "" : result.get("cateName"));
                    resultBuff.append(";");
                }
                if(result.get("product_uom_qty")!=null){
                    resultBuff.append(result.get("product_uom_qty"));
                }
                if(result.get("unit")!=null){
                    resultBuff.append(result.get("unit"));
                }
                resultBuff.append(";");
            }
        }
        if(StringUtils.isEmpty(String.valueOf(resultBuff))){
            return "";
        }else {
            return resultBuff.substring(0,resultBuff.length()-1);
        }
    }

    @Override
    @Transactional
    public void saveStockOutMessage(StockOutOrder stockOutOrder, List<StockOutOrderLine> stockOutOrderLineList) {
        //设置出库状态
        stockOutOrder.setOutState("freeze");
        stockOutOrder.setDataFrom("02");
        stockOutOrderMapper.insert(stockOutOrder);
        if (stockOutOrderLineList != null && stockOutOrderLineList.size() > 0) {
            for (StockOutOrderLine entity : stockOutOrderLineList) {
                //外键设置
                entity.setOrderId(stockOutOrder.getId());
                stockOutOrderLineMapper.insert(entity);
                //减库存
                stockOutOrderMapper.updateStock(entity);
            }
        }
    }


}
