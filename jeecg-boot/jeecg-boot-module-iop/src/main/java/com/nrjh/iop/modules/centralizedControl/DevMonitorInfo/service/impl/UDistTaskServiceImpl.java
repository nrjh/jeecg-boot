package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.UDistTask;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper.UDistTaskMapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.IUDistTaskService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: U_DIST_TASK
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Service
@DS("ora")
public class UDistTaskServiceImpl extends ServiceImpl<UDistTaskMapper, UDistTask> implements IUDistTaskService {
    @Override
    public List<UDistTask> getAll(Map<String,Object> params) {
        return baseMapper.getAll(params);
    }
}
