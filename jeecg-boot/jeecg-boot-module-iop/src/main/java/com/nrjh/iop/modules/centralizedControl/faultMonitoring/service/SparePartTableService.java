package com.nrjh.iop.modules.centralizedControl.faultMonitoring.service;

import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringTableResultVo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringVo;

import java.util.List;

public interface SparePartTableService {
    /**
     * 获取备品备件表数据
     * @param faultMonitoringVo
     * @return
     */
    List<FaultMonitoringTableResultVo> getSparePartTableList(FaultMonitoringVo faultMonitoringVo);
}
