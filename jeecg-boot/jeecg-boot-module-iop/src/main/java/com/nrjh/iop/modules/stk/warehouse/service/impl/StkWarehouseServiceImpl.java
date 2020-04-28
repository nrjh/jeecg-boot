package com.nrjh.iop.modules.stk.warehouse.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.stk.warehouse.entity.StkWarehouse;
import com.nrjh.iop.modules.stk.warehouse.mapper.StkWarehouseMapper;
import com.nrjh.iop.modules.stk.warehouse.service.IStkWarehouseService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 库房
 * @Author: jeecg-boot
 * @Date:   2020-04-26
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkWarehouseServiceImpl extends ServiceImpl<StkWarehouseMapper, StkWarehouse> implements IStkWarehouseService {

}
