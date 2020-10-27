package com.nrjh.iop.modules.stk.goodsplaceadjustment.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.entity.StockMoveOrderLine;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: stock_move_order_line
 * @Author: jeecg-boot
 * @Date:   2020-10-09
 * @Version: V1.0
 */
@DS("iop")
public interface IStockMoveOrderLineService extends IService<StockMoveOrderLine> {
    /**
     * 根据移库单号进行删除(物理)
     * @param orderId
     */
    void deleteByOrderId(String orderId);

    /**
     * 根据移库单号进行删除(逻辑)
     * @param orderId
     */
    void deleteByOrderIdWithLogic(String orderId);
}
