package com.nrjh.iop.modules.alarm.record.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.record.entity.VAlarmInfo;

import java.util.List;

/**
 * @Description: 报警信息视图
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
public interface VAlarmInfoMapper extends BaseMapper<VAlarmInfo> {
    String alarmAmount(String equipNo);
    String alarmAmountByEquipId(String equipNo);
    List<Equip> queryEquipOrEquipfno(String equipNo);
}
