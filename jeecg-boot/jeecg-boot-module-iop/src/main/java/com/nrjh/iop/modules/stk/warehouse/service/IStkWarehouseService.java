package com.nrjh.iop.modules.stk.warehouse.service;

import com.nrjh.iop.modules.stk.warehouse.entity.StkWarehouse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 库房
 * @Author: jeecg-boot
 * @Date:   2020-04-26
 * @Version: V1.0
 */
public interface IStkWarehouseService extends IService<StkWarehouse> {
    void deleteById(String id);
    void saveWithChildLocation(StkWarehouse stkWarehouse);
}
