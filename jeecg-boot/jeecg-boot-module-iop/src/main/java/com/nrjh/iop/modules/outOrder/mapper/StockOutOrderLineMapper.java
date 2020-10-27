package com.nrjh.iop.modules.outOrder.mapper;

import java.util.Date;
import java.util.List;

import com.nrjh.iop.modules.outOrder.entity.StockOutOrderLine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.stk.spareparts.entity.StkStock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 出库明细表
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
public interface StockOutOrderLineMapper extends BaseMapper<StockOutOrderLine> {

    public boolean deleteByMainId(@Param("mainId") Integer mainId);

    public List<StockOutOrderLine> selectByMainId(@Param("mainId") Integer mainId);


    /**
     * 根据出库单id把状态全部改为退库
     *
     * @param mainId
     * @return
     */
    @Update("UPDATE stock_out_order_line SET `status` = 'output' WHERE order_id = #{mainId}")
    public boolean backByMainUd(@Param("mainId") String mainId);

    /**
     * 出库完成修改库存
     * @param reduceProductQty
     * @param virtualInventory
     * @param stkStock
     */
    void updateStock(@Param("reduceProductQty") Integer reduceProductQty, @Param("virtualInventory") Integer virtualInventory, @Param("stkStock") StkStock stkStock);

    /**
     * 根据物品编号查询物品实际库存数量
     * @param defaultCode
     * @return
     */
    Integer selectProductQty(String defaultCode);

    /**
     * 根据出库单号查询出库明细
     * @param id
     * @return
     */
    StockOutOrderLine selectByOrderId(String id);
}
