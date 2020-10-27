package com.nrjh.iop.modules.stk.goodsplaceadjustment.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.dto.GeneralGoodsPlaceAdjustmentDTO;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.entity.StockMoveOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: stock_move_order
 * @Author: jeecg-boot
 * @Date:   2020-10-09
 * @Version: V1.0
 */
@DS("iop")
public interface IStockMoveOrderService extends IService<StockMoveOrder> {
    /**
     * 保存出库货位
     * @param dto
     */
    void saveWithStockMoveOrderLine(GeneralGoodsPlaceAdjustmentDTO dto);
    /**
     * 修改出库货位
     */
    void updateWithStockMoveOrderLine(GeneralGoodsPlaceAdjustmentDTO dto);
    /**
     * 根据货位调整单号删除
     */
    void deleteWithStockMoveOrderLine(String orderMoveId);
    /**
     * 根据货位调整单号批量删除
     */
    void deleteBatchWithStockMoveOrderLine(List<String> orderMoveIdList);
}
