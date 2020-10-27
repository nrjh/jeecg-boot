package com.nrjh.iop.modules.stk.goodsplaceadjustment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.dto.GeneralGoodsPlaceAdjustmentDTO;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.entity.StockMoveOrder;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.entity.StockMoveOrderLine;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.mapper.StockMoveOrderMapper;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.service.IStockMoveOrderLineService;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.service.IStockMoveOrderService;
import com.nrjh.iop.modules.stk.in.entity.OrderStockInList;
import com.nrjh.iop.modules.stk.in.service.IOrderStockInListService;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: stock_move_order
 * @Author: jeecg-boot
 * @Date:   2020-10-09
 * @Version: V1.0
 */
@Service
public class StockMoveOrderServiceImpl extends ServiceImpl<StockMoveOrderMapper, StockMoveOrder> implements IStockMoveOrderService {

    @Autowired
    private IStockMoveOrderLineService stockMoveOrderLineService;

    @Autowired
    private IOrderStockInListService orderStockInListService;

    @Override
    @Transactional(rollbackFor = JeecgBootException.class)
    public void saveWithStockMoveOrderLine(GeneralGoodsPlaceAdjustmentDTO dto) {
        StockMoveOrder stockMoveOrder = new StockMoveOrder();
        List<StockMoveOrderLine> stockMoveOrderLineList = dto.getStockMoveOrderLineList();
        // 从dto中赋值StockMoveOrder的属性值到stockMoveOrder
        BeanUtil.copyProperties(dto, stockMoveOrder);
        baseMapper.insert(stockMoveOrder);

        stockMoveOrderLineList.forEach(stockMoveOrderLine -> {
            stockMoveOrderLineService.save(stockMoveOrderLine);
            // 获取stockMoveOrderLine的stock_in_order_id（主键）
            Integer stockInOrderId = stockMoveOrderLine.getStockInOrderId();
            String[] splitWhInputStockLotId = stockMoveOrderLine.getWhInputStockLotId().split(",");
            String[] splitWhInputStockLotName = stockMoveOrderLine.getWhInputStockLotName().split(",");
            for (int i = 0; i < splitWhInputStockLotId.length; i++) {
                String[] tempSplit = splitWhInputStockLotId[i].split(":");
                // 获得库区库存位置Id
                String whInputStockLotId = tempSplit[1];
                // 获得库区库存位置名称
                String[] whInputStockLotName = splitWhInputStockLotName[i].split(":");
                OrderStockInList orderStockInList = new OrderStockInList();
                orderStockInList.setStockInOrderId(stockInOrderId);
                // 操作类型
                orderStockInList.setOperType("move");
                // 库区库存位置
                orderStockInList.setWhInputStockLotId(whInputStockLotId);
                // 库区库存位置名称
                orderStockInList.setWhInputStockLotName(whInputStockLotName[0]);
                // 状态
                orderStockInList.setStatus(stockMoveOrderLine.getStatus());
                // 需求数量
                orderStockInList.setProductActualQty(stockMoveOrderLine.getProductMoveQty());
                // 单据明细
                orderStockInList.setStockOrderId(stockMoveOrderLine.getOrderId());
                orderStockInListService.save(orderStockInList);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = JeecgBootException.class)
    public void updateWithStockMoveOrderLine(GeneralGoodsPlaceAdjustmentDTO dto) {
        StockMoveOrder stockMoveOrder = new StockMoveOrder();
        List<StockMoveOrderLine> stockMoveOrderLineList = dto.getStockMoveOrderLineList();
        // 从dto中赋值StockMoveOrder的属性值到stockMoveOrder
        BeanUtil.copyProperties(dto, stockMoveOrder);
        baseMapper.update(stockMoveOrder,new UpdateWrapper<StockMoveOrder>().eq("order_move_id",stockMoveOrder.getOrderMoveId()));
        // 因为没有从前端获取到stockMoveOrderLine的主键，所以无法根据主键进行更新，更新操作更改为：先删除数据库中存在的，再插入
        stockMoveOrderLineService.deleteByOrderId(stockMoveOrder.getOrderMoveId());
//        stockMoveOrderLineList.forEach(s -> {
//            StockMoveOrderLine stockMoveOrderLine = new StockMoveOrderLine();
//            BeanUtils.copyProperties(s,stockMoveOrderLine);
//            stockMoveOrderLine.setOrderId(stockMoveOrder.getOrderMoveId());
//            stockMoveOrderLineService.save(stockMoveOrderLine);
//        });
        // 删除order_stock_in_list原先存在的数据
        orderStockInListService.deleteByStockOrderIdWithLogic(stockMoveOrder.getOrderMoveId());
        stockMoveOrderLineList.forEach(stockMoveOrderLine -> {
            stockMoveOrderLineService.save(stockMoveOrderLine);
            // 获取stockMoveOrderLine的stock_in_order_id（主键）
            Integer stockInOrderId = stockMoveOrderLine.getStockInOrderId();

            String[] splitWhInputStockLotId = stockMoveOrderLine.getWhInputStockLotId().split(",");
            String[] splitWhInputStockLotName = stockMoveOrderLine.getWhInputStockLotName().split(",");
            for (int i = 0; i < splitWhInputStockLotId.length; i++) {
                String[] tempSplit = splitWhInputStockLotId[i].split(":");
                // 获得库区库存位置Id
                String whInputStockLotId = tempSplit[1];
                // 获得库区库存位置名称
                String[] whInputStockLotName = splitWhInputStockLotName[i].split(":");
                OrderStockInList orderStockInList = new OrderStockInList();
                orderStockInList.setStockInOrderId(stockInOrderId);
                // 操作类型
                orderStockInList.setOperType("move");
                // 库区库存位置
                orderStockInList.setWhInputStockLotId(whInputStockLotId);
                // 库区库存位置名称
                orderStockInList.setWhInputStockLotName(whInputStockLotName[0]);
                // 状态
                orderStockInList.setStatus(stockMoveOrderLine.getStatus());
                // 需求数量
                orderStockInList.setProductActualQty(stockMoveOrderLine.getProductMoveQty());
                // 单据明细
                orderStockInList.setStockOrderId(stockMoveOrderLine.getOrderId());
                orderStockInListService.save(orderStockInList);

            }
        });

    }

    @Override
    @Transactional(rollbackFor = JeecgBootException.class)
    public void deleteWithStockMoveOrderLine(String orderMoveId) {
        // 删除《库存移库单》
        baseMapper.delete(new QueryWrapper<StockMoveOrder>().eq("order_move_id",orderMoveId));
        // 删除《移库信息明细》
        stockMoveOrderLineService.deleteByOrderIdWithLogic(orderMoveId);
        // 删除order_stock_in_list存在的数据
        orderStockInListService.deleteByStockOrderIdWithLogic(orderMoveId);
    }

    @Override
    @Transactional(rollbackFor = JeecgBootException.class)
    public void deleteBatchWithStockMoveOrderLine(List<String> orderMoveIdList) {
        orderMoveIdList.forEach(o -> {
            // 删除《库存移库单》
            baseMapper.delete(new QueryWrapper<StockMoveOrder>().eq("order_move_id",o));
            // 删除《移库信息明细》
            stockMoveOrderLineService.deleteByOrderIdWithLogic(o);
            // 删除order_stock_in_list存在的数据
            orderStockInListService.deleteByStockOrderIdWithLogic(o);
        });
    }
}
