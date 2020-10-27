package com.nrjh.iop.modules.alarm.alarmtype.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultReasonInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultRepairInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultSopInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultTaskInfo;
import com.nrjh.iop.modules.alarm.alarmtype.mapper.FaultReasonInfoMapper;
import com.nrjh.iop.modules.alarm.alarmtype.mapper.FaultRepairInfoMapper;
import com.nrjh.iop.modules.alarm.alarmtype.mapper.FaultSopInfoMapper;
import com.nrjh.iop.modules.alarm.alarmtype.mapper.FaultTaskInfoMapper;
import com.nrjh.iop.modules.alarm.alarmtype.service.IFaultTaskInfoService;
import com.nrjh.iop.modules.alarm.bind.entity.FaultPrdProductInfo;
import com.nrjh.iop.modules.alarm.bind.mapper.FaultPrdProductInfoMapper;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 报警类型管理
 * @Author: jeecg-boot
 * @Date:   2020-08-27
 * @Version: V1.0
 */
@Service
@DS("ora")
public class FaultTaskInfoServiceImpl extends ServiceImpl<FaultTaskInfoMapper, FaultTaskInfo> implements IFaultTaskInfoService {

	@Autowired
	private FaultTaskInfoMapper faultTaskInfoMapper;
	@Autowired
	private FaultRepairInfoMapper faultRepairInfoMapper;
	@Autowired
	private FaultSopInfoMapper faultSopInfoMapper;
	@Autowired
	private FaultReasonInfoMapper faultReasonInfoMapper;
	@Autowired
	private FaultPrdProductInfoMapper faultPrdProductInfoMapper;

	@Override
	@Transactional
	public void saveMain(FaultTaskInfo faultTaskInfo, List<FaultRepairInfo> faultRepairInfoList, List<FaultSopInfo> faultSopInfo2List,List<FaultReasonInfo> faultReasonInfoList) {
		SimpleDateFormat sd=new SimpleDateFormat("YYYYMMDDHHmmss");
		faultTaskInfo.setFaultTaskNo("YJBH"+sd.format(new Date()));
		faultTaskInfoMapper.insert(faultTaskInfo);
		if(faultRepairInfoList!=null && faultRepairInfoList.size()>0) {
			for(FaultRepairInfo entity:faultRepairInfoList) {
				if(!StringUtils.isEmpty(entity.getExecMode()) && !StringUtils.isEmpty(entity.getIsReplace()) ){
					//外键设置
					entity.setFaultTaskId(faultTaskInfo.getId());
					faultRepairInfoMapper.insert(entity);
				}
			}
		}
		if(faultSopInfo2List!=null && faultSopInfo2List.size()>0) {
			for(FaultSopInfo entity:faultSopInfo2List) {
				if(!StringUtils.isEmpty(entity.getSop())){
					//外键设置
					entity.setFaultTaskId(faultTaskInfo.getId());
					faultSopInfoMapper.insert(entity);
				}
			}
		}
		if(faultReasonInfoList!=null && faultReasonInfoList.size()>0) {
			for(FaultReasonInfo entity:faultReasonInfoList) {
				if(!StringUtils.isEmpty(entity.getReasonName()) &&!StringUtils.isEmpty(entity.getReasonMode())){

					//外键设置
					entity.setFaultTaskId(faultTaskInfo.getId());
					faultReasonInfoMapper.insert(entity);
				}
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(FaultTaskInfo faultTaskInfo,List<FaultRepairInfo> faultRepairInfoList,List<FaultSopInfo> faultSopInfoList,List<FaultReasonInfo> faultReasonInfoList) {
		faultTaskInfoMapper.updateById(faultTaskInfo);

		//1.先删除子表数据
		faultRepairInfoMapper.deleteByMainId(faultTaskInfo.getId());
		faultSopInfoMapper.deleteByMainId(faultTaskInfo.getId());
		faultReasonInfoMapper.deleteByMainId(faultTaskInfo.getId());
		//2.子表数据重新插入
		if(faultRepairInfoList!=null && faultRepairInfoList.size()>0) {
			for(FaultRepairInfo entity:faultRepairInfoList) {
				//外键设置
				entity.setFaultTaskId(faultTaskInfo.getId());
				faultRepairInfoMapper.insert(entity);
			}
		}
		if(faultSopInfoList!=null && faultSopInfoList.size()>0) {
			for(FaultSopInfo entity:faultSopInfoList) {
				//外键设置
				entity.setFaultTaskId(faultTaskInfo.getId());
				faultSopInfoMapper.insert(entity);
			}
		}
		if(faultReasonInfoList!=null && faultReasonInfoList.size()>0) {
			for(FaultReasonInfo entity:faultReasonInfoList) {
				//外键设置
				entity.setFaultTaskId(faultTaskInfo.getId());
				faultReasonInfoMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = JeecgBootException.class)
	public void delMain(String id) {
		faultRepairInfoMapper.deleteByMainId(id);
		faultSopInfoMapper.deleteByMainId(id);
		faultTaskInfoMapper.deleteById(id);
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("FAULT_TASK_ID",id);
		FaultPrdProductInfo faultPrdProductInfo = faultPrdProductInfoMapper.selectOne(queryWrapper);
		if (!BeanUtil.isEmpty(faultPrdProductInfo)){
			// 删除报警设备下的数据
			faultPrdProductInfoMapper.delete(queryWrapper);
		}
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			faultRepairInfoMapper.deleteByMainId(id.toString());
			faultSopInfoMapper.deleteByMainId(id.toString());
			faultTaskInfoMapper.deleteById(id);
		}
	}

	@Override
	public List<Map> queryFaulNameByEquipType(String equipType) {
		return faultTaskInfoMapper.queryFaulNameByEquipType(equipType);
	}

}
