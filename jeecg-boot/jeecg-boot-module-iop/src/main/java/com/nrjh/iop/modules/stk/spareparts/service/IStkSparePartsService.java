package com.nrjh.iop.modules.stk.spareparts.service;

import com.nrjh.iop.modules.stk.spareparts.entity.StkSpareParts;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.stk.stkscrap.entity.ScrapPro;

/**
 * @Description: 备品备件
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
public interface IStkSparePartsService extends IService<StkSpareParts> {

    //保存物品和库存
    void saveProdAndStock(StkSpareParts stkSpareParts);

    //编辑物品和库存
    void updateProdAndStock(StkSpareParts stkSpareParts);

    //删除物品和库存
    void deleteProdAndStock(String id);
}
