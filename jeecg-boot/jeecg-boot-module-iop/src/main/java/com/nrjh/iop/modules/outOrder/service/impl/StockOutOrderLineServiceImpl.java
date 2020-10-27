package com.nrjh.iop.modules.outOrder.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrderLine;
import com.nrjh.iop.modules.outOrder.mapper.StockOutOrderLineMapper;
import com.nrjh.iop.modules.outOrder.service.IStockOutOrderLineService;
import com.nrjh.iop.modules.outOrder.vo.StockOutOrderLineVO;
import com.nrjh.iop.modules.stk.in.entity.OrderStockInList;
import com.nrjh.iop.modules.stk.in.entity.StockInOrderLine;
import com.nrjh.iop.modules.stk.in.service.IOrderStockInListService;
import com.nrjh.iop.modules.stk.in.service.IStockInOrderLineService;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderLineVo;
import com.nrjh.iop.modules.stk.spareparts.service.IStkStockService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 出库明细表
 * @Author: jeecg-boot
 * @Date: 2020-08-19
 * @Version: V1.0
 */
@Service
@DS("iop")
@Slf4j
public class StockOutOrderLineServiceImpl extends ServiceImpl<StockOutOrderLineMapper, StockOutOrderLine> implements IStockOutOrderLineService {

    @Autowired
    private IStkStockService stockService;

    @Autowired
    private StockOutOrderLineMapper stockOutOrderLineMapper;

    @Autowired
    private IOrderStockInListService orderStockInListService;

    @Autowired
    private IStockInOrderLineService stockInOrderLineService;

    @Override
    @Transactional
    public List<StockOutOrderLine> selectByMainId(Integer mainId) {
        List<StockOutOrderLine> stockOutOrderLines = stockOutOrderLineMapper.selectByMainId(mainId);
//        return stockOutOrderLines.stream().map(s -> productStockQty(s)).collect(Collectors.toList());
        return stockOutOrderLines;
    }

    /**
     * 获取虚拟库存数量
     * @return
     */
    private StockOutOrderLine productStockQty(StockOutOrderLine s){
        Integer productStockQty = stockService.productStockQty(s.getProductId());
        s.setStockProductQty(productStockQty);
        return s;
    }


    @Override
    @Transactional
    public List<StockOutOrderLineVO> selectVoByMainId(Integer mainId) {
        List<StockOutOrderLineVO> voList = new ArrayList<>();
        List<StockOutOrderLine> list = stockOutOrderLineMapper.selectByMainId(mainId);
        for (StockOutOrderLine entity : list) {
            StockOutOrderLineVO vo = new StockOutOrderLineVO();
            BeanUtils.copyProperties(entity, vo);
            vo.setOrderStockInLists(orderStockInListService.selectByMainId(String.valueOf(entity.getId())));
            vo.setProductQty(stockOutOrderLineMapper.selectProductQty(entity.getDefaultCode()));
            QueryWrapper q2Wrapper = new QueryWrapper();
            q2Wrapper.eq("order_id", vo.getOrderId());
            q2Wrapper.eq("product_id",vo.getProductId());
            StockInOrderLine one = stockInOrderLineService.getOne(q2Wrapper);
            if (BeanUtil.isEmpty(one)) {
                log.error("查询合同编码出现异常，查询采购单号:{}，商品单号:{}", vo.getOrderId(),vo.getProductId());
            }else {
                vo.setContract(one.getContract());
            }
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public List<StockOutOrderLineVO> getGoodsLocation(Integer outOrderId, Integer productId, String productCode) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id",outOrderId);
        queryWrapper.eq("product_id",productId);
        queryWrapper.eq("default_code",productCode);
        List<StockOutOrderLine> list = this.list(queryWrapper);
        if(CollectionUtil.isEmpty(list)){
            log.error("查询出库单有误,参数：order_id = {}，product_id = {} ，defaul_code = {}",outOrderId,productId,productCode);
            throw new JeecgBootException("查询出库单有误");
        }
        List<StockOutOrderLineVO> stockOutOrderLineVOS = ConvertUtils.sourceToTarget(list, StockOutOrderLineVO.class);
        stockOutOrderLineVOS.forEach(s -> {
            Integer tempOuterOrder = s.getOrderId();
            QueryWrapper q2Wrapper = new QueryWrapper();
            q2Wrapper.eq("stock_out_order_id",tempOuterOrder);
            List<OrderStockInList> orderStockInList = orderStockInListService.list(q2Wrapper);
            if(BeanUtil.isEmpty(list)){
                log.error("出库表错误,outOrderId{}",outOrderId);
//                throw new JeecgBootException("出库表错误");
            }
            // 合同编码
            QueryWrapper q3Wrapper = new QueryWrapper();
            q3Wrapper.eq("order_id", s.getOrderId());
            q3Wrapper.eq("product_id",s.getProductId());
            StockInOrderLine one = stockInOrderLineService.getOne(q3Wrapper);
            if (BeanUtil.isEmpty(one)) {
                log.error("查询合同编码出现异常，查询采购单号:{}，商品单号:{}", s.getOrderId(),s.getProductId());
//                throw new JeecgBootException("查询合同编码出现异常");
            }else {
                s.setContract(one.getContract());
            }
            s.setOrderStockInLists(orderStockInList);
        });
        return stockOutOrderLineVOS;
    }
}
