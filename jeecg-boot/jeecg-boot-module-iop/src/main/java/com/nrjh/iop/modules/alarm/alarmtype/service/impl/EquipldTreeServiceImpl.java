package com.nrjh.iop.modules.alarm.alarmtype.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.alarm.alarmtype.mapper.FaultTaskInfoMapper;
import com.nrjh.iop.modules.alarm.alarmtype.service.IEquipldTreeService;
import com.nrjh.iop.modules.alarm.alarmtype.vo.EquipIdModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shiyiteng on 2020/8/26.
 */
@Service
@DS("ora")
public class EquipldTreeServiceImpl  implements IEquipldTreeService {
    @Autowired private FaultTaskInfoMapper faultTaskInfoMapper;


    @Override
    public List<EquipIdModel> selectFristEquip() {
        return faultTaskInfoMapper.selectFristEquip();
    }

    @Override
    public List<EquipIdModel> selectThreeEquip(String id) {
        return faultTaskInfoMapper.selectThreeEquip(id);
    }

    @Override
    public List<EquipIdModel> selectEquipType(String id) {
        return faultTaskInfoMapper.selectEquipType(id);
    }

    @Override
    public List<EquipIdModel> selectEquipByEquipTypeId(String id) {
        return faultTaskInfoMapper.selectEquipByEquipTypeId(id);
    }

    @Override
    public List<EquipIdModel> selectEquipById(String equipId) {
        return faultTaskInfoMapper.selectEquipById(equipId);
    }
}
