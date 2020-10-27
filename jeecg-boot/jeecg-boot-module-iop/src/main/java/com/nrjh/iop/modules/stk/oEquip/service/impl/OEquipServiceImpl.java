package com.nrjh.iop.modules.stk.oEquip.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.stk.oEquip.entity.OEquip;
import com.nrjh.iop.modules.stk.oEquip.mapper.OEquipMapper;
import com.nrjh.iop.modules.stk.oEquip.service.IOEquipService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: o_equip
 * @Author: jeecg-boot
 * @Date:   2020-10-10
 * @Version: V1.0
 */
@Service
@DS("ora")
public class OEquipServiceImpl extends ServiceImpl<OEquipMapper, OEquip> implements IOEquipService {

}
