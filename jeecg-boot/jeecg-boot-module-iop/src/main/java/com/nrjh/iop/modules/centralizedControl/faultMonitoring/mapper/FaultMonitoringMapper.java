package com.nrjh.iop.modules.centralizedControl.faultMonitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.entity.ValarmInfo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: FaultMonitoringMapper
 * @Author: lei li
 * @Date:   2020-09-26
 * @Version: V1.0
 */

public interface FaultMonitoringMapper extends BaseMapper<ValarmInfo> {
    /**
     * 按天查询故障设备 获取线图数据
     * @param nextDay
     * @param faultMonitoringVo
     * @return
     */
    List<Map<String, Object>> getFaultEquipMentBarByDay(@Param("alarmTime") String nextDay, @Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);

    /**
     * 按月查询故障设备 获取线图数据
     * @param proximo
     * @param faultMonitoringVo
     * @return
     */
    List<Map<String, Object>> getFaultEquipMentBarByMonth(@Param("alarmTime") String proximo, @Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);


    /**
     * 按天查询检定线故障设备 获取饼图数据
     * @param startTime
     * @param endTime
     * @param faultMonitoringVo
     */
    List<Map<String,Object>> getAreaLinePieByDay(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);
    /**
     * 按月查询检定线故障设备 获取饼图数据
     * @param startTime
     * @param endTime
     * @param faultMonitoringVo
     */
    List<Map<String, Object>> getAreaLinePieByMonth(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);


    /**
     * 按天根据设备类型分类查询所有故障设备，获取饼图数据
     * @param startTime
     * @param endTime
     * @param faultMonitoringVo
     * @return
     */
    List<Map<String, Object>> getFailureEquipmentPieByDay(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);

    /**
     * 按天根据设备类型分类查询所有故障设备类别，获取饼图数据
     * @param startTime
     * @param endTime
     * @param faultMonitoringVo
     * @return
     */
    List<Map<String, Object>> getFailureEquipTypePieByDay(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);

    /**
     * 按天
     * 智能分析故障设备类型不同区域柱状图
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> getFailureEquipTypeBarByArea(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("equipType") String equipType);

    /**
     * 按天
     * 同类设备故障对比
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> getFailureEquipNoBarByEquipType(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("equipType") String equipType,@Param("areaId") String areaId);

    /**
     * 按月查询所有故障设备，获取饼图数据
     * @param startTime
     * @param endTime
     * @param faultMonitoringVo
     * @return
     */
    List<Map<String, Object>> getFailureEquipmentPieByMonth(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);

    /**
     * 按天查询检定线故障类型统计数据，获取饼图数据
     * @param startTime
     * @param endTime
     * @param faultMonitoringVo
     * @return
     */
    List<Map<String, Object>> getFaultTypePieByDay(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);
    /**
     * 按月查询检定线故障类型统计数据，获取饼图数据
     * @param startTime
     * @param endTime
     * @param faultMonitoringVo
     * @return
     */
    List<Map<String, Object>> getFaultTypePieByMonth(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);

    /**
     * 按天查询故障统计数据，获取图型数据
     * @param nextDay
     * @param faultMonitoringVo
     * @return
     */
    Map<String, Object> getFailureEquipmetTotalByDay(@Param("alarmTime") String nextDay,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);

    /**
     * 按天查询故障时长
     * @param nextDay
     * @return
     */
    List<Map<String,Object>> getFailureTotalTimeByDay(@Param("alarmTime") String nextDay,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);
    /**
     * 按月查询故障统计数据，获取图型数据
     * @param proximo
     * @param faultMonitoringVo
     * @return
     */
    Map<String, Object> getFailureEquipmetTotalByMonth(@Param("alarmTime") String proximo,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);
    /**
     * 按月查询故障时长
     * @param proximo
     * @return
     */
    List<Map<String, Object>> getFailureTotalTimeByMonth(@Param("alarmTime") String proximo,@Param("faultMonitoringVo") FaultMonitoringVo faultMonitoringVo);

    /**
     * 查询设备类型通过区域编号
     * @param
     * @return
     */
    List<Map<String,Object>> detailEquipTypeByAreaId(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("faultMonitoringVo")FaultMonitoringVo faultMonitoringVo);
    /**
     * 查询设备通过区域编号和设备编号
     * @param
     * @return
     */
    List<Map<String,Object>> detailEquipByAreaIdAndEquipNO(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("faultMonitoringVo")FaultMonitoringVo faultMonitoringVo);
    /**
     * 查询故障通过设备编号和区域编号
     * @param
     * @return
     */
    List<Map<String,Object>> detailFaultByEquipNOAndAreaId(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("faultMonitoringVo")FaultMonitoringVo faultMonitoringVo);
    /**
     * 查询故障对比通过
     * @param
     * @return
     */
    List<Map<String,Object>> detailFaultComparisonByAreaIdAndEquipNO(@Param("time") String time,@Param("faultMonitoringVo")FaultMonitoringVo faultMonitoringVo);
}
