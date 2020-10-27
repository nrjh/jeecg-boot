package com.nrjh.iop.modules.alarm.alarmtype.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultReasonInfo;
import com.nrjh.iop.modules.alarm.alarmtype.mapper.FaultReasonInfoMapper;
import com.nrjh.iop.modules.alarm.alarmtype.service.IFaultReasonInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 故障原因
 * @Author: jeecg-boot
 * @Date:   2020-09-18
 * @Version: V1.0
 */
@Service
@DS("ora")
public class FaultReasonInfoServiceImpl extends ServiceImpl<FaultReasonInfoMapper, FaultReasonInfo> implements IFaultReasonInfoService {

    @Override
    public List<FaultReasonInfo> errorReasonNameAndId(String lineNO, String productId) {
        return baseMapper.errorReasonNameAndId(lineNO, productId);
    }
}
