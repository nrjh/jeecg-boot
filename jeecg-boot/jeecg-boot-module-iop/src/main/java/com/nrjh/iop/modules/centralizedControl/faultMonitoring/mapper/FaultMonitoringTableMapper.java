package com.nrjh.iop.modules.centralizedControl.faultMonitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.entity.ValarmInfo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringTableResultVo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringVo;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: FaultMonitoringMapper
 * @Author: lei li
 * @Date:   2020-10-08
 * @Version: V1.0
 */

public interface FaultMonitoringTableMapper extends BaseMapper<ValarmInfo> {
    /**
     * 按天获取选中天内故障故障报警表记录
     * @param startTime
     * @param endTime
     * @param faultMonitoringVo
     * @return
     */
    List<FaultMonitoringTableResultVo> getFaultMonitoringTableListByDay(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);
    /**
     * 按月获取选中天内故障故障报警表记录
     * @param startTime
     * @param endTime
     * @param faultMonitoringVo
     * @return
     */
    List<FaultMonitoringTableResultVo> getFaultMonitoringTableListByMonth(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);

    /**
     * 按天获取选中天内故障类型表记录
     * @param startTime
     * @param endTime
     * @param faultMonitoringVo
     * @return
     */
    List<FaultMonitoringTableResultVo> getFaultTypeTableDataByDay(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);

    /**
     * 根据线体名称获取总频率
     * @param lineName
     * @return
     */
    Integer getTotalFrequency(@Param("lineName") String lineName,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * 按天获取选中天内故障类型表记录
     * @param startTime
     * @param endTime
     * @param faultMonitoringVo
     * @return
     */
    List<FaultMonitoringTableResultVo> getFaultTypeTableDataByMonth(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);

    /**
     * 根据线体名称获取总频率
     * @param lineName
     * @return
     */
    Integer getTotalFrequencyByMonth(@Param("lineName") String lineName,@Param("startTime") String startTime,@Param("endTime") String endTime);
}
