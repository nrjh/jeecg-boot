package com.nrjh.iop.modules.alarm.alarmtype.service;

import com.nrjh.iop.modules.alarm.alarmtype.vo.EquipIdModel;

import java.util.List;

/**
 * Created by shiyiteng on 2020/8/26.
 */
public interface IEquipldTreeService {
    List<EquipIdModel> selectFristEquip();

    List<EquipIdModel> selectThreeEquip(String id);
    List<EquipIdModel> selectEquipType(String id);
    List<EquipIdModel> selectEquipByEquipTypeId(String id);
    List<EquipIdModel> selectEquipById(String equipId);
}
