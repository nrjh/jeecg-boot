package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.dto.CountAndTaskDTO;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.dto.MtDetectTaskMonitoDTO;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.*;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.*;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.vo.ExceptionCheckTaskVO;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.vo.MtDetectTaskMonitoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 检定任务信息(新平台)实体
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Api(tags="检定任务信息(新平台)实体")
@RestController
@RequestMapping("/ptm/mtDetectTaskInfo")
@Slf4j
public class MtDetectTaskInfoController extends JeecgController<MtDetectTaskInfo, IMtDetectTaskInfoService> {
	@Autowired
	private IMtDetectTaskInfoService mtDetectTaskInfoService;

	@Autowired
	private ITDetectTaskService tDetectTaskService;

	@Autowired
	private IMtPositionDetectInfoService mtPositionDetectInfoService;

	@Autowired
	private DIoTaskService ioTaskService;

	@Autowired
	private IUDistTaskService uDistTaskService;

	/**
	 * 核对任务异常列表查询
	 * @param mtDetectTaskInfo
	 * @return
	 */
	@AutoLog(value = "核对任务异常列表查询")
	@ApiOperation(value="核对任务异常列表查询", notes="核对任务异常列表查询")
	@GetMapping(value = "/exceptionList")
	public Result<?> exceptionList(MtDetectTaskInfo mtDetectTaskInfo) {
		Map<String,Object> parmas = new HashMap<>();
		if(mtDetectTaskInfo.getDetectTaskNo()!=null){
			parmas.put("detectTaskNo",mtDetectTaskInfo.getDetectTaskNo());
		}
		if(mtDetectTaskInfo.getLineNo() != null){
			parmas.put("lineNo",mtDetectTaskInfo.getLineNo());
		}
		// 获取《检定任务信息》列表
		List<MtDetectTaskInfo> mtdetactTaskInfoList = mtDetectTaskInfoService.getAll(parmas);
		List<MtDetectTaskInfo> exceptionList = getExceptionList(mtdetactTaskInfoList);
		// 必要数据的转换
		List<ExceptionCheckTaskVO> exceptionCheckTaskVOS = ConvertUtils.sourceToTarget(exceptionList, ExceptionCheckTaskVO.class);
		List<ExceptionCheckTaskVO> exceptionCheckTaskVOList = exceptionCheckTaskVOS.stream().map(e -> getCompleteData(e)).collect(Collectors.toList());
		// 检定任务信息《mt_detect_task_info》与检定任务单《T_DETECT_TASK》进行必要数据替换
		Page<ExceptionCheckTaskVO> excepitonCheckTaskVOPage = new Page<>();
		excepitonCheckTaskVOPage.setRecords(exceptionCheckTaskVOList);
		return Result.ok(excepitonCheckTaskVOPage);
	}

	// 返回异常的list
	List<MtDetectTaskInfo> getExceptionList(List<MtDetectTaskInfo> mtdetactTaskInfoLis){
		List<MtDetectTaskInfo> resultList = new ArrayList<>();
		mtdetactTaskInfoLis.forEach(e -> {
			/**
			 * 检定异常方式一：《表位检定结果信息-MT_POSITION_DETECT_INFO》与《检定任务信息-MT_DETECT_TASK_INFO》
			 */
			// 1、根据检定任务编号，统计表位信息表的合格量和不合格量个数
			// 检定开始时间
			Date detectStartDate = e.getDetectStartDate();
			// 检定结束时间
			Date detectEndDate = e.getDetectEndDate();
			Integer qualified = mtPositionDetectInfoService.getQualified(e.getDetectTaskNo(),detectStartDate,detectEndDate);
			Integer unQualified = mtPositionDetectInfoService.getUnQualified(e.getDetectTaskNo(),detectStartDate,detectEndDate);
			// 2、合格量与T_DETECT_TASK中的合格数量、不合格数量一致则无异常
			TDetectTask tDetectTask = tDetectTaskService.getInfoByDetectTaskNo(e.getDetectTaskNo(), e.getArriveBatchNo());
			// 3、检定任务信息的设备数量 = 检定任务的检定次数
			if(BeanUtil.isEmpty(tDetectTask) ||
					!tDetectTask.getDetectConcP().equals(qualified) ||
					!tDetectTask.getDetectConcU().equals(unQualified) ||
					!tDetectTask.getDetectQty().equals(e.getEquipQty())||
					!iOTaskNoIsRight(e.getDetectTaskNo())){
				resultList.add(e);
			}

		});
		return resultList;
	}

	/**
	 * 检定异常方式二：出入库任务《MD_IO_TASK》中的任务量 = 出入库设备明细《MD_IO_DET》的输入库数量， 出入库任务编号一致
	 */
	private boolean iOTaskNoIsRight(String detectTaskNO){
		List<MdIoTask> listByRelaNo = ioTaskService.getListByRelaNo(detectTaskNO);
		if (BeanUtil.isEmpty(listByRelaNo)){
			return false;
		}
		boolean result = true;
		List<CountAndTaskDTO> countAndTaskNoList = ioTaskService.getCountAndTaskNo();
		// 把查询出来的对象转为map快速获取信息
		Map<String,Integer> map = new HashMap<>();
		countAndTaskNoList.forEach(t -> {
			if(!map.containsKey(t.getIoTaskNo())){map.put(t.getIoTaskNo(),t.getTaskCount());}
		});
		// 若是《出入库任务》的taskNo不存在于《出入库设备明细》中 -> 出错
		//  若是《出入库任务》的数量 ≠ 《出入库设备明细》中的设备数量 -> 出错
		for (int i = 0; i < listByRelaNo.size(); i++) {
			MdIoTask mdIoTask = listByRelaNo.get(i);
			if(!map.containsKey(mdIoTask.getIoTaskNo()) || !mdIoTask.getQty().equals(map.get(mdIoTask.getIoTaskNo()))) {
				result = false;
				log.info("出入库任务编号：{},数量:{} ,map中查询的数量:{}",mdIoTask.getIoTaskNo(),mdIoTask.getQty(),map.get(mdIoTask.getIoTaskNo()));
				break;
			}
		}
		return result;
	}

	private ExceptionCheckTaskVO getCompleteData(ExceptionCheckTaskVO exceptionCheckTaskVO){
		QueryWrapper q1 = new QueryWrapper();
		q1.eq("TASK_ID", exceptionCheckTaskVO.getDetectTaskNo());
		TDetectTask tDetectTask = tDetectTaskService.getOne(q1);
		if(BeanUtil.isEmpty(tDetectTask)){
			log.error("检定任务信息的检定任务编号为,{}", exceptionCheckTaskVO.getDetectTaskNo());
		}
		exceptionCheckTaskVO.setTaskType(null == tDetectTask ? null : tDetectTask.getTaskType());
		exceptionCheckTaskVO.setTaskBgnDate(null == tDetectTask ? null : tDetectTask.getTaskBgnDate());
		exceptionCheckTaskVO.setTaskEndDate(null == tDetectTask ? null : tDetectTask.getTaskEndDate());
		exceptionCheckTaskVO.setAuditRemark(null == tDetectTask ? null : tDetectTask.getAuditRemark());
		Integer hadCheckNum = 0;
		if(null != tDetectTask){
			hadCheckNum = tDetectTask.getDetectQty() - tDetectTask.getDetectConcN();
		}
		exceptionCheckTaskVO.setHadCheckNum(hadCheckNum);
		exceptionCheckTaskVO.setDetectConcP(null == tDetectTask ? null :tDetectTask.getDetectConcP());
		exceptionCheckTaskVO.setDetectConcU(null == tDetectTask ? null :tDetectTask.getDetectConcU());
		return exceptionCheckTaskVO;
	}

	@AutoLog(value = "检定任务列表查询")
	@ApiOperation(value="检定任务列表查询", notes="检定任务列表查询")
	@GetMapping(value = "/checkTaskMonitorList")
	public Result<?> checkTaskMonitorList(MtDetectTaskMonitoDTO dto) {
		Map<String, Object> params = new HashMap<>();
		if(dto.getDetectTaskNo() != null){
			params.put("detectTaskNo",dto.getDetectTaskNo());
		}
		if(dto.getTaskStatus() != null){
			params.put("taskStatus",dto.getTaskStatus());
		}
		if(dto.getLineNo()!=null){
			params.put("lineNo",dto.getLineNo());
		}
		List<MtDetectTaskInfo> mtDetectTaskInfosList = mtDetectTaskInfoService.checkTaskMonitorList(params);
		List<MtDetectTaskMonitoVO> mtDetectTaskMonitoVOS = ConvertUtils.sourceToTarget(mtDetectTaskInfosList, MtDetectTaskMonitoVO.class);
		// 组装数据
		List<MtDetectTaskMonitoVO> resultList = mtDetectTaskMonitoVOS.stream().map(m -> getCompleteCheckTaskMonitorList(m)).collect(Collectors.toList());
		Page<MtDetectTaskMonitoVO> pageDate = new Page<>();
		pageDate.setRecords(resultList);
		// 若是由供应商则进行过滤
		if(dto.getSupplyNo() != null){
			List<MtDetectTaskMonitoVO> filterSupplyNo = resultList.stream().filter(m -> m.getSupplyNo().equals(dto.getSupplyNo())).collect(Collectors.toList());
			pageDate.setRecords(filterSupplyNo);
		}
		return Result.ok(pageDate);
	}

	@AutoLog(value = "获取各个列表的数量")
	@ApiOperation(value="获取各个列表的数量", notes="获取各个列表的数量")
	@GetMapping(value = "/getCount")
	public Result<?> getCount(){
		Map<String, Object> nums = new HashMap<>();
		Map<String,Object> parmas = new HashMap<>();
		// 核对任务异常列表查询
		List<MtDetectTaskInfo> mtdetactTaskInfoList = mtDetectTaskInfoService.getAll(parmas);
		List<MtDetectTaskInfo> exceptionList = getExceptionList(mtdetactTaskInfoList);
		nums.put("exceptionCount",exceptionList.size());
		// 检定任务列表查询
		List<MtDetectTaskInfo> mtDetectTaskInfosList = mtDetectTaskInfoService.checkTaskMonitorList(parmas);
		nums.put("mtDetectTaskInfoCount",mtDetectTaskInfosList.size());
		// 出入库列表
		List<DIoTask> allDIotaskInfo = ioTaskService.getAllDIotaskInfo(parmas);
		nums.put("ioStockCount",allDIotaskInfo.size());
		// 配送任务监控
		List<UDistTask> uDistTaskList = uDistTaskService.getAll(parmas);
		nums.put("taskMonitorCount",uDistTaskList.size());
		return Result.ok(nums);
	}

	private MtDetectTaskMonitoVO getCompleteCheckTaskMonitorList(MtDetectTaskMonitoVO mtDetectTaskMonitoVO){
		// 供应商
		TDetectTask tDetectTask = tDetectTaskService.getInfoByDetectTaskNo(mtDetectTaskMonitoVO.getDetectTaskNo(), mtDetectTaskMonitoVO.getArriveBatchNo());
		if(BeanUtil.isEmpty(tDetectTask)){
			log.error("查找检定任务为null,查找信息:检定任务号 = {}，批次号 = {}",mtDetectTaskMonitoVO.getDetectTaskNo(),mtDetectTaskMonitoVO.getArriveBatchNo());
		}
		mtDetectTaskMonitoVO.setSupplyNo(tDetectTask == null ? null : tDetectTask.getSupplyNo());
		// 合格量
		mtDetectTaskMonitoVO.setDetectConcP(tDetectTask == null ? null :tDetectTask.getDetectConcP());
		// 已检定量
		Integer hadCheckNum = mtPositionDetectInfoService.hadCheckByTaskNo(mtDetectTaskMonitoVO.getDetectTaskNo());
		mtDetectTaskMonitoVO.setHadCheckNum(hadCheckNum);
		// 执行进度 = 已检定量/任务量*100
		double taskNum = new Double(mtDetectTaskMonitoVO == null ? 0 :mtDetectTaskMonitoVO.getEquipQty());
		double doubleHadCheckNum = new Double(hadCheckNum);
		if (taskNum != 0 && doubleHadCheckNum != 0){
			NumberFormat num = NumberFormat.getPercentInstance();
			String rates = num.format(doubleHadCheckNum / taskNum);
			mtDetectTaskMonitoVO.setTaskRate(rates);
		}
		return mtDetectTaskMonitoVO;
	}

}
