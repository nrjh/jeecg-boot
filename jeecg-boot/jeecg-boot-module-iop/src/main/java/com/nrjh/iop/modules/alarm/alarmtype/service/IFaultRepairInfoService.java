package com.nrjh.iop.modules.alarm.alarmtype.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultRepairInfo;

import java.util.List;

/**
 * @Description: 修复方法
 * @Author: jeecg-boot
 * @Date:   2020-08-27
 * @Version: V1.0
 */
public interface IFaultRepairInfoService extends IService<FaultRepairInfo> {

	public List<FaultRepairInfo> selectByMainId(String mainId);
}
