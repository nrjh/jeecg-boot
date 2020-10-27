package com.nrjh.iop.modules.stk.inventory.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.stk.inventory.entity.StkInventoryRel;
import com.nrjh.iop.modules.stk.inventory.mapper.StkInventoryRelMapper;
import com.nrjh.iop.modules.stk.inventory.service.IStkInventoryRelService;
import org.springframework.stereotype.Service;

/**
 * @Description: stk_inventory_rel
 * @Author: jeecg-boot
 * @Date:   2020-08-05
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkInventoryRelServiceImpl extends ServiceImpl<StkInventoryRelMapper, StkInventoryRel> implements IStkInventoryRelService {

}
