package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.MtDetectTaskInfo;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper.MtDetectTaskInfoMapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.IMtDetectTaskInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 检定任务信息(新平台)实体
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Service
@DS("ora")
public class MtDetectTaskInfoServiceImpl extends ServiceImpl<MtDetectTaskInfoMapper, MtDetectTaskInfo> implements IMtDetectTaskInfoService {

    @Override
    public List<MtDetectTaskInfo> getAll(Map<String,Object> params) {
        return baseMapper.getAll(params);
    }

    @Override
    public List<MtDetectTaskInfo> checkTaskMonitorList(Map<String, Object> params) {
        return baseMapper.checkTaskMonitorList(params);
    }
}
