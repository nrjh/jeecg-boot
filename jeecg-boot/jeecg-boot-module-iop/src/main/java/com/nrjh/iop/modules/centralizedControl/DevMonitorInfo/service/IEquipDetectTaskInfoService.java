package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service;

import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.EquipDetectTaskInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: EQUIP_DETECT_TASK_INFO
 * @Author: jeecg-boot
 * @Date:   2020-09-12
 * @Version: V1.0
 */
public interface IEquipDetectTaskInfoService extends IService<EquipDetectTaskInfo> {
    EquipDetectTaskInfo getInfoByTaskNo(String detectTaskNo);
}
