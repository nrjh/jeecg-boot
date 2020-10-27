package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.dto.DevMonitorVo;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.DevMonitorInfo;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper.DevMonitorInfoMapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.IDevMonitorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 监控设备管理
 * @Author: jeecg-boot
 * @Date:   2020-09-11
 * @Version: V1.0
 */
@Service
@DS("ora")
public class DevMonitorInfoServiceImpl extends ServiceImpl<DevMonitorInfoMapper, DevMonitorInfo> implements IDevMonitorInfoService {
    @Autowired
    private DevMonitorInfoMapper devMonitorInfoMapper;
    @Override
    public List<DevMonitorVo> queryDevList(String lineNo) {
        return devMonitorInfoMapper.queryDevList(lineNo);
    }
}
