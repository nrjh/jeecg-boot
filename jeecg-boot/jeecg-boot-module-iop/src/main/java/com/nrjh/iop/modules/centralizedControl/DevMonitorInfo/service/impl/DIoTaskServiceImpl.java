package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.dto.CountAndTaskDTO;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.DIoTask;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.MdIoTask;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper.DIoTaskMapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.DIoTaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/10 19:13
 */
@Service
@DS("ora")
public class DIoTaskServiceImpl extends ServiceImpl<DIoTaskMapper, DIoTask> implements DIoTaskService {
    @Override
    public List<DIoTask> getAllDIotaskInfo(Map<String, Object> params) {
        return baseMapper.getAllDIotaskInfo(params);
    }

    @Override
    public List<MdIoTask> getListByRelaNo(String relaNo) {
        return baseMapper.getListByRelaNo(relaNo);
    }

    @Override
    public List<CountAndTaskDTO> getCountAndTaskNo() {
        return baseMapper.getCountAndTaskNo();
    }
}
