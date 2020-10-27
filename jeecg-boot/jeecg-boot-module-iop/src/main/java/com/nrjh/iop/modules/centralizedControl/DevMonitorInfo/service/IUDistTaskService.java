package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service;

import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.UDistTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: U_DIST_TASK
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
public interface IUDistTaskService extends IService<UDistTask> {
    List<UDistTask> getAll(Map<String,Object> params);
}
