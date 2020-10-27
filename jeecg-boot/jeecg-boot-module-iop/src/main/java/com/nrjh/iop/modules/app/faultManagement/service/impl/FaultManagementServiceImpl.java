package com.nrjh.iop.modules.app.faultManagement.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.record.entity.VAlarmInfo;
import com.nrjh.iop.modules.app.faultManagement.entity.Oarea;
import com.nrjh.iop.modules.app.faultManagement.mapper.FaultMannagementMapper;
import com.nrjh.iop.modules.app.faultManagement.service.FaultManagementService;
import com.nrjh.iop.modules.app.faultManagement.vo.EquipAndAreaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-10-15 17:24
 */
@Service
@DS("ora")
public class FaultManagementServiceImpl implements FaultManagementService {

    @Autowired
    private FaultMannagementMapper faultMannagementMapper;

    @Override
    public EquipAndAreaVo queryEquipAndAreaById(String equipNo) {
        return faultMannagementMapper.queryEquipAndAreaById(equipNo);
    }

    @Override
    public List<VAlarmInfo> queryFaultMessageList() {
        List<VAlarmInfo> list = faultMannagementMapper.queryFaultMessageList();
        return list;
    }

    @Override
    public List<Equip> queryEquipByLineId(String lineId) {
        return faultMannagementMapper.queryEquipByLineId(lineId);
    }

    @Override
    public List<Oarea> queryAreaInfo() {
        return faultMannagementMapper.queryAreaInfo();
    }
}
