package com.nrjh.iop.modules.alarm.alarmtype.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultReasonInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultSopInfo;

import java.util.List;

/**
 * @Description: 作业指导
 * @Author: jeecg-boot
 * @Date:   2020-08-27
 * @Version: V1.0
 */
public interface IFaultSopInfoService extends IService<FaultSopInfo> {

	public List<FaultSopInfo> selectByMainId(String mainId);
	public List<FaultReasonInfo> selectReasonByMainId(String mainId);
}
