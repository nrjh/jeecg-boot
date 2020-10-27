package com.nrjh.iop.modules.stk.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.stk.inventory.entity.InventoryProdVo;
import com.nrjh.iop.modules.stk.inventory.entity.StkInventory;
import com.nrjh.iop.modules.stk.inventory.model.StkInventoryModel;
import org.jeecg.modules.online.config.exception.BusinessException;

import java.util.List;

/**
 * @Description: stk_inventory
 * @Author: jeecg-boot
 * @Date:   2020-08-04
 * @Version: V1.0
 */
public interface IStkInventoryService extends IService<StkInventory> {

    void  saveInventoryDeal(StkInventoryModel stkInventoryModel) throws BusinessException;

    void  updateInventory(String id,String opt);


    List<InventoryProdVo>  queryProdList(String inventoryId);

    void dealInventoryResult(List<InventoryProdVo> inventoryProdVo);

    String getReultInventory(StkInventory stkInventory);

    List<InventoryProdVo>  queryProdTotal(String inventoryId);

}
