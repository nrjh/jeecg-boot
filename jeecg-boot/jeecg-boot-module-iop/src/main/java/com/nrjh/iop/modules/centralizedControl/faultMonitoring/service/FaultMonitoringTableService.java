package com.nrjh.iop.modules.centralizedControl.faultMonitoring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringResultVo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringTableResultVo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringVo;

import java.util.List;

public interface FaultMonitoringTableService {
    /**
     * 获取故障设备报警表数据
     * @param faultMonitoringVo
     * @return
     */
    List<FaultMonitoringTableResultVo> getFaultEquipmentAlarmTableList(FaultMonitoringVo faultMonitoringVo);

    /**
     * 故障类型表数据
     * @param faultMonitoringVo
     * @return
     */
    List<FaultMonitoringTableResultVo> getFaultTypeTableList(FaultMonitoringVo faultMonitoringVo);
}
