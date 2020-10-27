package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.TDetectTask;

/**
 * @Description: T_DETECT_TASK
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
public interface ITDetectTaskService extends IService<TDetectTask> {
    /**
     * 查询检定任务
     * @param detectTaskNo   检定任务号
     * @param arriveBatchNo  到货批次号
     * @return
     */
    TDetectTask getInfoByDetectTaskNo(String detectTaskNo,String arriveBatchNo);
}
