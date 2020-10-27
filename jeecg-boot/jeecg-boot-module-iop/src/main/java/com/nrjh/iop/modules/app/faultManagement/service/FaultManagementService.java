package com.nrjh.iop.modules.app.faultManagement.service;

import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.record.entity.VAlarmInfo;
import com.nrjh.iop.modules.app.faultManagement.entity.Oarea;
import com.nrjh.iop.modules.app.faultManagement.vo.EquipAndAreaVo;

import java.util.List;

public interface FaultManagementService {

    /**
     * 通过设备id查询设备和检定线信息
     * @param equipNo
     * @return
     */
    EquipAndAreaVo queryEquipAndAreaById(String equipNo);

    /**
     * 查询所有报警信息
     * @return
     */
    List<VAlarmInfo> queryFaultMessageList();

    /**
     * 通过检定线id查询设备信息
     * @param lineId
     * @return
     */
    List<Equip> queryEquipByLineId(String lineId);

    /**
     * 查询检定线信息
     * @param
     * @return
     */
    List<Oarea> queryAreaInfo();
}
