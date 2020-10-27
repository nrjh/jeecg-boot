package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.vo.UDistTaskVO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.ConvertUtils;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.UDistTask;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.IUDistTaskService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: U_DIST_TASK
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Api(tags="U_DIST_TASK")
@RestController
@RequestMapping("/ptm/uDistTask")
@Slf4j
public class UDistTaskController extends JeecgController<UDistTask, IUDistTaskService> {
	@Autowired
	private IUDistTaskService uDistTaskService;

	/**
	 * 配送任务监控列表查询
	 *
	 * @param uDistTask
	 * @return
	 */
	@AutoLog(value = "配送任务监控列表查询")
	@ApiOperation(value="配送任务监控列表查询", notes="配送任务监控列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryList(UDistTask uDistTask) {
		Map<String,Object> parmas = new HashMap<>();
		if(uDistTask.getAutoInfo()!=null){
			parmas.put("autoInfo",uDistTask.getAutoInfo());
		}
		if(uDistTask.getOrgNo()!=null){
			parmas.put("orgNo",uDistTask.getOrgNo());
		}
		List<UDistTask> uDistTaskList = uDistTaskService.getAll(parmas);
		List<UDistTaskVO> uDistTaskVOS = ConvertUtils.sourceToTarget(uDistTaskList, UDistTaskVO.class);
		Page<UDistTaskVO> uDistTaskVOSPage = new Page<>();
		uDistTaskVOSPage.setRecords(uDistTaskVOS);
		return Result.ok(uDistTaskVOSPage);
	}


}
