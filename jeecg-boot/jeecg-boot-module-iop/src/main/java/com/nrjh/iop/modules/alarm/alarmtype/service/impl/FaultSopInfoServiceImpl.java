package com.nrjh.iop.modules.alarm.alarmtype.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultReasonInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultSopInfo;
import com.nrjh.iop.modules.alarm.alarmtype.mapper.FaultReasonInfoMapper;
import com.nrjh.iop.modules.alarm.alarmtype.mapper.FaultSopInfoMapper;
import com.nrjh.iop.modules.alarm.alarmtype.service.IFaultSopInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 作业指导
 * @Author: jeecg-boot
 * @Date:   2020-08-27
 * @Version: V1.0
 */
@Service
@DS("ora")
public class FaultSopInfoServiceImpl extends ServiceImpl<FaultSopInfoMapper, FaultSopInfo> implements IFaultSopInfoService {
	
	@Autowired
	private FaultSopInfoMapper faultSopInfoMapper;
	@Autowired
	private FaultReasonInfoMapper faultReasonInfoMapper;
	
	@Override
	public List<FaultSopInfo> selectByMainId(String mainId) {
		return faultSopInfoMapper.selectByMainId(mainId);
	}

	@Override
	public List<FaultReasonInfo> selectReasonByMainId(String mainId) {
		return faultReasonInfoMapper.selectByMainId(mainId);
	}
}
