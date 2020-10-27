package com.nrjh.iop.modules.alarm.alarmtype.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultRepairInfo;
import com.nrjh.iop.modules.alarm.alarmtype.mapper.FaultRepairInfoMapper;
import com.nrjh.iop.modules.alarm.alarmtype.service.IFaultRepairInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 修复方法
 * @Author: jeecg-boot
 * @Date:   2020-08-27
 * @Version: V1.0
 */
@Service
@DS("ora")
public class FaultRepairInfoServiceImpl extends ServiceImpl<FaultRepairInfoMapper, FaultRepairInfo> implements IFaultRepairInfoService {
	
	@Autowired
	private FaultRepairInfoMapper faultRepairInfoMapper;
	
	@Override
	public List<FaultRepairInfo> selectByMainId(String mainId) {
		return faultRepairInfoMapper.selectByMainId(mainId);
	}
}
