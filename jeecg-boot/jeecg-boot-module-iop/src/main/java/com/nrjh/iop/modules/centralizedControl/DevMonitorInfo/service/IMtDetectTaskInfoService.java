package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.MtDetectTaskInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: 检定任务信息(新平台)实体
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
public interface IMtDetectTaskInfoService extends IService<MtDetectTaskInfo> {
    List<MtDetectTaskInfo> getAll(Map<String,Object> params);
    List<MtDetectTaskInfo> checkTaskMonitorList(Map<String, Object> params);

}
