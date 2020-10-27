package com.nrjh.iop.modules.stk.officesupplies.service;

import com.nrjh.iop.modules.stk.officesupplies.entity.StkSpareParts;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 办公用品
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
public interface IStkOfficeSuppliesService extends IService<StkSpareParts> {

    //保存物品和库存
    void saveProdAndStock(StkSpareParts stkSpareParts);

    //编辑物品和库存
    void updateProdAndStock(StkSpareParts stkSpareParts);

    //删除物品和库存
    void deleteProdAndStock(String id);
}
