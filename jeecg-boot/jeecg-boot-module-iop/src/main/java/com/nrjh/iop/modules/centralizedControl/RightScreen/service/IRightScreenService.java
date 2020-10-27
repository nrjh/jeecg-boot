package com.nrjh.iop.modules.centralizedControl.RightScreen.service;


import com.nrjh.iop.modules.centralizedControl.RightScreen.entity.vo.DetectTaskInfoVO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.DictModel;

import java.util.List;

public interface IRightScreenService {
    /**
     * 查询当年在检信息列表
     * @return
     */
    Result<?> checkList(String[] areaId);

    /**
     * 查询配送任务列表
     * @return
     */
    Result<?> taskList();

    /**
     * 查询通知公告列表
     */
    Result<?> announcementList();

    /*
     *查询报警信息列表
     */
    Result<?> alarmList(String[] areaId);

    /**
     * 查询故障设备报警TOP8
     */
    Result<?> equipAlarmList(String[] areaId);

    /**
     * 单相，三相，互感器，采集终端统计
     */
    Result<?> detectCountList();

    /**
     * 检定区域和故障类型的统计
     */
    Result<?> faultTypeList(String[] areaId,List<DictModel> dictModels);

    /**
     * 查询当天流水线故障占比情况
     */
    Result<?> lineFaultList(String[] areaId);

    /**
     * 查询当天流水线故障占比情况
     */
    Result<?> faultAreaList(String[] areaId);

    /**
     * 查询当天检测走势
     */
    Result<?> detectTrendList(String[] areaId);

    /**
     * 获取当前检定任务状态
     */
    Result<?> switchMode();

    /**
     * 获取出入库设备列表
     */
    Result<?> inOutEquipList(String areaId);

}
