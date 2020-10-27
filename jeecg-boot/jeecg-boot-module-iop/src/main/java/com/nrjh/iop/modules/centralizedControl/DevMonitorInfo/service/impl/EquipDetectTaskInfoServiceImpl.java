package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.EquipDetectTaskInfo;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper.EquipDetectTaskInfoMapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.IEquipDetectTaskInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: EQUIP_DETECT_TASK_INFO
 * @Author: jeecg-boot
 * @Date:   2020-09-12
 * @Version: V1.0
 */
@DS("ora")
@Service
public class EquipDetectTaskInfoServiceImpl extends ServiceImpl<EquipDetectTaskInfoMapper, EquipDetectTaskInfo> implements IEquipDetectTaskInfoService {

    @Override
    public EquipDetectTaskInfo getInfoByTaskNo(String detectTaskNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("DETECT_TASK_NO",detectTaskNo);
        EquipDetectTaskInfo equipDetectTaskInfo = baseMapper.selectOne(queryWrapper);
        return equipDetectTaskInfo;
    }
}
