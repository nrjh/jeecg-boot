package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service;

import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.MtPositionDetectInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @Description: MT_POSITION_DETECT_INFO
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
public interface IMtPositionDetectInfoService extends IService<MtPositionDetectInfo> {
    MtPositionDetectInfo getInfo(String taskNo);
    // 获取合格数
    Integer getUnQualified(String taskNo, Date startDate,Date endDate);
    // 获取不合格数
    Integer getQualified(String taskNo,Date startDate,Date endDate);
    // 根据检定任务编号查询已检定量
    Integer hadCheckByTaskNo(String taskNo);
}
