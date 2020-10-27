package com.nrjh.iop.modules.stk.goodsplaceadjustment.service.impl;

import com.nrjh.iop.modules.stk.goodsplaceadjustment.entity.StockMoveOrderLine;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.mapper.StockMoveOrderLineMapper;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.service.IStockMoveOrderLineService;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: stock_move_order_line
 * @Author: jeecg-boot
 * @Date:   2020-10-09
 * @Version: V1.0
 */
@Service
public class StockMoveOrderLineServiceImpl extends ServiceImpl<StockMoveOrderLineMapper, StockMoveOrderLine> implements IStockMoveOrderLineService {

    @Override
    @Transactional(rollbackFor = JeecgBootException.class)
    public void deleteByOrderId(String orderId) {
        baseMapper.deleteByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = JeecgBootException.class)
    public void deleteByOrderIdWithLogic(String orderId) {
        baseMapper.deleteByOrderIdWithLogic(orderId);
    }
}
