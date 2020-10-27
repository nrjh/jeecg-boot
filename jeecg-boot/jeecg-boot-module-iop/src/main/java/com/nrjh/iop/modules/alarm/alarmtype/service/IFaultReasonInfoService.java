package com.nrjh.iop.modules.alarm.alarmtype.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultReasonInfo;

import java.util.List;

/**
 * @Description: 故障原因
 * @Author: jeecg-boot
 * @Date:   2020-09-18
 * @Version: V1.0
 */
public interface IFaultReasonInfoService extends IService<FaultReasonInfo> {
    List<FaultReasonInfo> errorReasonNameAndId(String lineNO, String productId);
}
