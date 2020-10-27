package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.EquipDetectTaskInfo;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.IEquipDetectTaskInfoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: EQUIP_DETECT_TASK_INFO
 * @Author: jeecg-boot
 * @Date:   2020-09-12
 * @Version: V1.0
 */
@Api(tags="核对异常检测说明控制类")
@RestController
@RequestMapping("/checkExplain")
@Slf4j
public class CheckExplainController extends JeecgController<EquipDetectTaskInfo, IEquipDetectTaskInfoService> {
	@Autowired
	private IEquipDetectTaskInfoService equipDetectTaskInfoService;

	/**
	 *   保存异常说明
	 *
	 * @param equipDetectTaskInfo
	 * @return
	 */
	@AutoLog(value = "保存异常说明")
	@ApiOperation(value="保存异常说明", notes="保存异常说明")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody EquipDetectTaskInfo equipDetectTaskInfo) {
		equipDetectTaskInfoService.saveOrUpdate(equipDetectTaskInfo);
		return Result.ok("保存成功！");
	}


	/**
	 * 通过检定任务编号查询
	 *
	 * @param detectTaskNo
	 * @return
	 */
	@AutoLog(value = "通过检定任务id查询")
	@ApiOperation(value="通过检定任务id查询", notes="通过检定任务id查询")
	@GetMapping(value = "/queryByTaskNo")
	public Result<?> queryById(@RequestParam(name="detectTaskNo",required=true) String detectTaskNo) {
		EquipDetectTaskInfo equipDetectTaskInfo = equipDetectTaskInfoService.getInfoByTaskNo(detectTaskNo);
		if(equipDetectTaskInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(equipDetectTaskInfo);
	}


}
