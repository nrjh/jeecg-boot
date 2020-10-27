package com.nrjh.iop.modules.workOrder.mapper;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultRepairInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultTaskInfo;
import com.nrjh.iop.modules.workOrder.entity.OrdProcessInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Description: 工单处理明细
 * @Author: jeecg-boot
 * @Date:   2020-09-08
 * @Version: V1.0
 */
public interface OrdProcessInfoMapper extends BaseMapper<OrdProcessInfo> {

    List<FaultRepairInfo> queryRepairInfo(FaultTaskInfo faultTaskInfo);
}
