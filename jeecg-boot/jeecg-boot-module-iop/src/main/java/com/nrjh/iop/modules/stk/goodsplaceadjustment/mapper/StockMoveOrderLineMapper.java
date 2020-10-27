package com.nrjh.iop.modules.stk.goodsplaceadjustment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.entity.StockMoveOrderLine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: stock_move_order_line
 * @Author: jeecg-boot
 * @Date:   2020-10-09
 * @Version: V1.0
 */
public interface StockMoveOrderLineMapper extends BaseMapper<StockMoveOrderLine> {
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
