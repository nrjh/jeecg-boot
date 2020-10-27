package com.nrjh.iop.modules.workOrder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultRepairInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultTaskInfo;
import com.nrjh.iop.modules.workOrder.entity.OrdProcessInfo;
import com.nrjh.iop.modules.workOrder.vo.OrdProcessInfoEnclosureInfoVo;

import java.util.List;

/**
 * @Description: 工单处理明细
 * @Author: jeecg-boot
 * @Date:   2020-09-08
 * @Version: V1.0
 */
public interface IOrdProcessInfoService extends IService<OrdProcessInfo> {

    /**
     * 根据设备编号和报警名称查询修复方法
     *
     * @param faultTaskInfo
     * @return
     */
    List<FaultRepairInfo> queryRepairInfo(FaultTaskInfo faultTaskInfo);

    OrdProcessInfo getOrdProcessInfoByWordOrderId(String workOrderId);

    /**
     * 工单处理提交
     * @param ordProcessInfoEnclosureInfoVo
     */
    void saveOrdProcessAndEnclosureInfo(OrdProcessInfoEnclosureInfoVo ordProcessInfoEnclosureInfoVo);
}
