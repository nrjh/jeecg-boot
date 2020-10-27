package com.nrjh.iop.modules.stk.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.stk.inventory.entity.InventoryProdVo;
import com.nrjh.iop.modules.stk.inventory.entity.StkInventory;

import java.util.List;
import java.util.Map;

/**
 * @Description: stk_inventory
 * @Author: jeecg-boot
 * @Date:   2020-08-04
 * @Version: V1.0
 */
public interface StkInventoryMapper extends BaseMapper<StkInventory> {
    /**
     * stk_inventory_location_rel 盘点单 与库位
     */
    void  saveInventoryLocation(Map<String,Object> map);

    /**
     * stk_inventory_rel 盘点单范围
     */
    void saveInventoryRel(Map<String,Object> map);


    List<InventoryProdVo> queryProdList(String inventoryId);

    List<InventoryProdVo> queryProdTotal(String inventoryId);
    /**
     * 获取最大Id值
     * @return
     */
    Integer getMaxIdNum();

}
