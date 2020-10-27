package com.nrjh.iop.modules.alarm.record.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.bind.mapper.EquipMapper;
import com.nrjh.iop.modules.alarm.record.entity.VAlarmInfo;
import com.nrjh.iop.modules.alarm.record.mapper.VAlarmInfoMapper;
import com.nrjh.iop.modules.alarm.record.service.IVAlarmInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 报警信息视图
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Service
@DS("ora")
public class VAlarmInfoServiceImpl extends ServiceImpl<VAlarmInfoMapper, VAlarmInfo> implements IVAlarmInfoService {
    @Autowired
    private EquipMapper equipMapper;

    @Autowired
    private VAlarmInfoMapper vAlarmInfoMapper;

    @Override
    public List<Equip> queryListEquip(String equipNo) {
        return vAlarmInfoMapper.queryEquipOrEquipfno(equipNo);
    }

    @Override
    public String alarmAmount(String equipNo) {
        return vAlarmInfoMapper.alarmAmount(equipNo);
    }

    @Override
    public String alarmAmountByEquipId(String equipNo) {
        return vAlarmInfoMapper.alarmAmountByEquipId(equipNo);
    }
}
