package com.nrjh.iop.modules.alarm.alarmtype.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultReasonInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultRepairInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultSopInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultTaskInfo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 报警类型管理
 * @Author: jeecg-boot
 * @Date:   2020-08-27
 * @Version: V1.0
 */
public interface IFaultTaskInfoService extends IService<FaultTaskInfo> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(FaultTaskInfo faultTaskInfo, List<FaultRepairInfo> faultRepairInfoList, List<FaultSopInfo> faultSopInfo2List,List<FaultReasonInfo> faultReasonInfoList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(FaultTaskInfo faultTaskInfo, List<FaultRepairInfo> faultRepairInfoList, List<FaultSopInfo> faultSopInfo2List,List<FaultReasonInfo> faultReasonInfoList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);

	List<Map> queryFaulNameByEquipType(String equipType);
	
}
