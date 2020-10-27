package com.nrjh.iop.modules.alarm.bind.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.bind.mapper.EquipMapper;
import com.nrjh.iop.modules.alarm.bind.service.IEquipService;
import com.nrjh.iop.modules.alarm.bind.vo.TreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: fault_prd_product_info
 * @Author: jeecg-boot
 * @Date: 2020-08-26
 * @Version: V1.0
 */
@Service
@DS("ora")
public class EquipServiceImpl extends ServiceImpl<EquipMapper, Equip> implements IEquipService {
    @Autowired
    EquipMapper equipMapper;

    @Override
    public List<TreeModel> getTreeList() {
        List<TreeModel> list = equipMapper.getAllEquipCategory();
        list.addAll(equipMapper.getAllArea());
        list.addAll(equipMapper.getAllEquip());
        return list;
    }

    @Override
    public List<String> quwerEquipNo(String equipType) {
        return equipMapper.queryEquipNo(equipType);
    }
}
