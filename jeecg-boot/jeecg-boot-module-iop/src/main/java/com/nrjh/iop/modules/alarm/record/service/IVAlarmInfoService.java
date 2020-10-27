package com.nrjh.iop.modules.alarm.record.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.record.entity.VAlarmInfo;

import java.util.List;

/**
 * @Description: 报警信息视图
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
public interface IVAlarmInfoService extends IService<VAlarmInfo> {


    List<Equip> queryListEquip(String equipNo);

    String alarmAmount(String equipNo);

    String alarmAmountByEquipId(String equipNo);
}
