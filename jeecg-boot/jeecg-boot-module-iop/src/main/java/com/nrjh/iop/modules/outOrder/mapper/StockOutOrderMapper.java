package com.nrjh.iop.modules.outOrder.mapper;

import java.util.List;
import java.util.Map;

import com.nrjh.iop.modules.outOrder.entity.StockOutOrderLine;
import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 领料出库单
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
public interface StockOutOrderMapper extends BaseMapper<StockOutOrder> {

    List<String> queryLoction(String defaultCode);

    List<Map<String, Object>> getProdDetails(String id);

    /**
     * 新增时减库存
     * @param entity
     */
    void updateStock(@Param("entity") StockOutOrderLine entity);

    /**
     * 修改时减库存
     * @param entity
     * @param virtualInventory
     */
    void updateStockByChange(@Param("entity")StockOutOrderLine entity,@Param("virtualInventory") Integer virtualInventory);

    /**
     * 退回处理
     * @param id
     */
    void backOutOrderById(String id);

    /**
     * 回退虚拟库存
     * @param stockOutOrderLine
     */
    void backStkStockById(@Param("stockOutOrderLine") StockOutOrderLine stockOutOrderLine);
}
