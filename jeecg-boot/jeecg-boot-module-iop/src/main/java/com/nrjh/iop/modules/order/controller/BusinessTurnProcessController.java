package com.nrjh.iop.modules.order.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nrjh.iop.modules.order.entity.BusinessTurnProcess;
import com.nrjh.iop.modules.order.service.IBusinessTurnProcessService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;

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
 * @Description: business_turn_process
 * @Author: jeecg-boot
 * @Date:   2020-10-24
 * @Version: V1.0
 */
@Api(tags="business_turn_process")
@RestController
@RequestMapping("/businessTurnProcess/businessTurnProcess")
@Slf4j
public class BusinessTurnProcessController extends JeecgController<BusinessTurnProcess, IBusinessTurnProcessService> {
	@Autowired
	private IBusinessTurnProcessService businessTurnProcessService;
	
	/**
	 * 分页列表查询
	 *
	 * @param businessTurnProcess
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "business_turn_process-分页列表查询")
	@ApiOperation(value="business_turn_process-分页列表查询", notes="business_turn_process-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BusinessTurnProcess businessTurnProcess,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BusinessTurnProcess> queryWrapper = QueryGenerator.initQueryWrapper(businessTurnProcess, req.getParameterMap());
		Page<BusinessTurnProcess> page = new Page<BusinessTurnProcess>(pageNo, pageSize);
		IPage<BusinessTurnProcess> pageList = businessTurnProcessService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param businessTurnProcess
	 * @return
	 */
	@AutoLog(value = "business_turn_process-添加")
	@ApiOperation(value="business_turn_process-添加", notes="business_turn_process-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BusinessTurnProcess businessTurnProcess) {
		businessTurnProcessService.save(businessTurnProcess);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param businessTurnProcess
	 * @return
	 */
	@AutoLog(value = "business_turn_process-编辑")
	@ApiOperation(value="business_turn_process-编辑", notes="business_turn_process-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BusinessTurnProcess businessTurnProcess) {
		businessTurnProcessService.updateById(businessTurnProcess);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "business_turn_process-通过id删除")
	@ApiOperation(value="business_turn_process-通过id删除", notes="business_turn_process-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		businessTurnProcessService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "business_turn_process-批量删除")
	@ApiOperation(value="business_turn_process-批量删除", notes="business_turn_process-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.businessTurnProcessService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "business_turn_process-通过id查询")
	@ApiOperation(value="business_turn_process-通过id查询", notes="business_turn_process-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BusinessTurnProcess businessTurnProcess = businessTurnProcessService.getById(id);
		if(businessTurnProcess==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(businessTurnProcess);
	}


}
