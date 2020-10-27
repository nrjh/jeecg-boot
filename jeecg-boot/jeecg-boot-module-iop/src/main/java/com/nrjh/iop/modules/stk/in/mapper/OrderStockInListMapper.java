package com.nrjh.iop.modules.stk.in.mapper;

import java.util.List;

import com.nrjh.iop.modules.stk.in.entity.OrderStockInList;
import com.nrjh.iop.modules.stk.in.entity.StockInOrderLine;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: order_stock_in_list
 * @Author: jeecg-boot
 * @Date: 2020-08-18
 * @Version: V1.0
 */
public interface OrderStockInListMapper extends BaseMapper<OrderStockInList> {

    /**
     * 物理删除
     * @param mainId
     * @return
     */
    public boolean deleteByMainId(@Param("mainId") String mainId);

    /**
     * 逻辑删除
     * @param stockOrderId
     * @return
     */
    void deleteByStockOrderIdWithLogic(String stockOrderId);

    public boolean deleteByInOrderId(@Param("mainId") String id);

    public List<OrderStockInList> selectByMainId(@Param("mainId") String mainId);

    /**
     * 根据出入库单id把状态全部改为失效
     *
     * @param mainId
     * @return
     */
    @Update("UPDATE order_stock_in_list SET `STATUS` = 2 WHERE stock_in_order_id = #{mainId}")
    public boolean backByOrderInUd(@Param("mainId") String mainId);

    /**
     * 根据出库单id把状态全部改为失效
     *
     * @param mainId
     * @return
     */
    @Update("UPDATE order_stock_in_list SET `STATUS` = 2 WHERE stock_out_order_id = #{mainId}")
    public boolean backByOrderOutUd(@Param("mainId") String mainId);
}
