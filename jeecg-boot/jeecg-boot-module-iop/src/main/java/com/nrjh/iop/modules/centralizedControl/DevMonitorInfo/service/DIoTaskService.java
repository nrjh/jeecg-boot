package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.dto.CountAndTaskDTO;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.DIoTask;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.MdIoTask;

import java.util.List;
import java.util.Map;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/10 19:13
 */
public interface DIoTaskService extends IService<DIoTask> {
    List<DIoTask> getAllDIotaskInfo(Map<String,Object> params);

    /**
     * 根据检定任务编号获取出入库任务列表
     * @param relaNo 检定任务编号
     * @return
     */
    List<MdIoTask> getListByRelaNo(String relaNo);

    /**
     * @return
     */
    List<CountAndTaskDTO> getCountAndTaskNo();
}
