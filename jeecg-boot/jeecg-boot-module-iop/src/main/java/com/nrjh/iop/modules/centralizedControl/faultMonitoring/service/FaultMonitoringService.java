package com.nrjh.iop.modules.centralizedControl.faultMonitoring.service;

import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringResultVo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringVo;
import org.jeecg.common.api.vo.Result;

import java.util.List;
import java.util.Map;

public interface FaultMonitoringService {

    /**
     * 获取故障设备柱状图数据
     * @param faultMonitoringVo
     * @return
     */
    List<FaultMonitoringResultVo> getFaultEquipMentBarList(FaultMonitoringVo faultMonitoringVo);

    /**
     * 获取检定线饼图数据
     * @param faultMonitoringVo
     * @return
     */
    FaultMonitoringResultVo getAreaLinePieList(FaultMonitoringVo faultMonitoringVo);

    /**
     * 获取故障设备饼图数据
     * @param faultMonitoringVo
     * @return
     */
    FaultMonitoringResultVo getFailureEquipmentPieList(FaultMonitoringVo faultMonitoringVo);

    /**
     * 获取检定线故障类型饼图数据
     * @param faultMonitoringVo
     * @return
     */
    FaultMonitoringResultVo getFaultTypePieList(FaultMonitoringVo faultMonitoringVo);

    /**
     * 获取故障总时长图形数据
     * @param faultMonitoringVo
     * @return
     */
    List<FaultMonitoringResultVo> getFailureTotalTimeLineData(FaultMonitoringVo faultMonitoringVo);

//    /**
//     * 获取故障时长、故障总数及故障级别
//     * @param faultMonitoringVo
//     * @return
//     */
//    List<FaultMonitoringResultVo> getFailureHandlingTimeRateLineData(FaultMonitoringVo faultMonitoringVo);

    /**
     * 获取故障设备饼图数据
     * @param faultMonitoringVo
     * @return
     */
    FaultMonitoringResultVo getFailureEquipTypePieByDay(FaultMonitoringVo faultMonitoringVo);

    /**
     * 智能分析故障设备类型不同区域柱状图
     * @param faultMonitoringVo
     * @return
     */
    FaultMonitoringResultVo getFailureEquipTypeBarByDay(FaultMonitoringVo faultMonitoringVo);

    /**
     * 智能分析同类设备故障对比柱状图
     * @param faultMonitoringVo
     * @return
     */

    FaultMonitoringResultVo getFailureEquipNoBarByEquipType(FaultMonitoringVo faultMonitoringVo);

    /**
     *
     * @param
     * @return
     */
    Result<?> detailEquipTypeByAreaId(FaultMonitoringVo faultMonitoringVo);

    /**
     *
     * @param
     * @return
     */
    Result<?> detailEquipByAreaIdAndEquipNO(FaultMonitoringVo faultMonitoringVo);

    /**
     *
     * @param
     * @return
     */
    Result<?> detailFaultByEquipNOAndAreaId(FaultMonitoringVo faultMonitoringVo);

    /**
     *
     * @param
     * @return
     */
    Result<?> detailFaultComparisonByAreaIdAndEquipNO(FaultMonitoringVo faultMonitoringVo);

    /**
     *
     * @return
     */
    FaultMonitoringResultVo detailFaultComparisonByDay();
}
