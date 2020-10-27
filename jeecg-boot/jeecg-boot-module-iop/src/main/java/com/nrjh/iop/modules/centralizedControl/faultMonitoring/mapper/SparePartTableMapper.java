package com.nrjh.iop.modules.centralizedControl.faultMonitoring.mapper;

import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringTableResultVo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringVo;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface SparePartTableMapper {
    /**
     * 按天获取选中天内备品备件表记录
     * @param startTime
     * @param endTime
     * @param faultMonitoringVo
     * @return
     */
    List<FaultMonitoringTableResultVo> getSparePartTableListByDay(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);

    /**
     * 按天获取选中天内备品备件表记录
     * @param startTime
     * @param endTime
     * @param faultMonitoringVo
     * @return
     */
    List<FaultMonitoringTableResultVo> getSparePartTableListByMonth(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);
}
