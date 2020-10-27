package com.nrjh.iop.modules.alarm.record.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.alarm.record.entity.MtEquipAlarmInfo;
import com.nrjh.iop.modules.alarm.record.mapper.MtEquipAlarmInfoMapper;
import com.nrjh.iop.modules.alarm.record.service.IMtEquipAlarmInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: MT_EQUIP_ALARM_INFO
 * @Author: jeecg-boot
 * @Date:   2020-08-28
 * @Version: V1.0
 */
@Service
@DS("ora")
public class MtEquipAlarmInfoServiceImpl extends ServiceImpl<MtEquipAlarmInfoMapper, MtEquipAlarmInfo> implements IMtEquipAlarmInfoService {

}
