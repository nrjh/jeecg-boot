package com.nrjh.iop.modules.centralizedControl.RightScreen.mapper;

import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProduceCycle;
import com.nrjh.iop.modules.centralizedControl.RightScreen.entity.vo.*;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.system.vo.DictModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RightScreenMapper {

    /**
     * 查询当天在检信息列表
     * @return
     */
    List<DetectTaskInfoVO> detailDetectTaskInfoVoList(@Param("areaId")String areaId);

    /**
     * 查询当天配送任务列表
     * @return
     */
    List<DeliveryTaskVO> detailDeliveryTaskVOList();

    /**
     * 查询报警信息列表
     * @return
     */
    List<AlarmInfoVO> detailAlarmInfoVoList(@Param("areaId")String areaId);

    /**
     * 查询故障报警列表
     * @return
     */
    List<EquipFailureVO> detailEquipFailureVoList(@Param("areaId")String areaId);

    /**
     * 查询本月的单相、三相、互感器、采集终端列表
     * @param dataMap
     * @return
     */
    List<DevProduceCycle> detailDevProduceCycleList(@Param("dataMap") Map<String,Object> dataMap);

    /**
     * 分组查询检定任务信息表的单相，三相，互感器，采集终端检定数量
     *1本月 2本日
     */
    List<Map<String, Object>> detailDetectTaskInfoMap(@Param("status") Integer status);

    /**
     * 分组查询计划明细表的单相、三相、互感器、采集终端当日检定数量
     */
    List<Map<String, Object>> detailProduceDPlanMapList(@Param("dataMap") Map<String,Object> dataMap);

    /**
     * 查询检定线区域和类型的统计
     * @return
     */
    List<FaultTypeVO> detailFaultTypeVoList(@Param("areaId")String areaId);

    /**
     * 查询当天流水线故障占比
     */
    List<LineFaultVO> detailLineFaultVoList(@Param("areaId")String areaId);

    /**
     * 查询当天的故障区域占比
     */
    List<FaultAreaVO> detailAreaFaultVoList(@Param("areaId")String areaId);

    /**
     *查询当天的检测走势
     */
    List<DetectTrendVO> detailDetectTrendVoList(@Param("areaId")String areaId);

    /**
     * 查询当天在检状态
     */
    List<DetectInfoVO> detailDetectInfoVoList();

    /**
     * 通过名称模糊查询检定区域id
     */
    String detailArea(@Param("name")String name);

    /**
     * 查询当天完成量/累计完成量
     * 1表示查询当天 0表示查询所有
     */
    Map<String,Object> detailCompleteNum(@Param("areaId")String areaId,@Param("thatDay")int thatDay);

    /**
     * 获取所有的检定区域
     */
    List<DictModel> detailAreaList();

    /**
     * 当天出入库设备列表
     */
    List<AccessEquipVO> detailStkInOutTEquipInfo(@Param("areaId")String areaId);
}
