package com.nrjh.iop.modules.intelligent.automation.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nrjh.iop.modules.intelligent.automation.entity.FailuresCount;
import com.nrjh.iop.modules.intelligent.automation.service.IFailuresCountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

 /**
 * @Description: FAILURES_COUNT
 * @Author: jeecg-boot
 * @Date:   2020-10-14
 * @Version: V1.0
 */
@Api(tags="FAILURES_COUNT")
@RestController
@RequestMapping("/intelligent/automation/failuresCount")
@Slf4j
public class FailuresCountController extends JeecgController<FailuresCount, IFailuresCountService> {
	@Autowired
	private IFailuresCountService failuresCountService;

	 /**
	  * 通过设备名称查询
	  *
	  * @param equipName
	  * @return
	  */
	 @AutoLog(value = "FAILURES_COUNT-通过设备名称查询")
	 @ApiOperation(value="FAILURES_COUNT-通过设备名称查询", notes="FAILURES_COUNT-通过设备名称查询")
	 @GetMapping(value = "/queryByEquipName")
	 public Result<?> queryByEquipName(@RequestParam(name="equipName",required=true) String equipName) {
		 QueryWrapper<FailuresCount> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("equip_name",equipName);
		 FailuresCount failuresCount = failuresCountService.getOne(queryWrapper);
		 if(failuresCount==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(failuresCount);
	 }

	 /**
	  *   通过设备名称删除
	  *
	  * @param equipName
	  * @return
	  */
	 @AutoLog(value = "FAILURES_COUNT-通过设备名称删除")
	 @ApiOperation(value="FAILURES_COUNT-通过设备名称删除", notes="FAILURES_COUNT-通过设备名称删除")
	 @DeleteMapping(value = "/deleteByEquipName")
	 public Result<?> deleteByEquipName(@RequestParam(name="equipName",required=true) String equipName) {
		 QueryWrapper<FailuresCount> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("equip_name",equipName);
		 FailuresCount failuresCount = failuresCountService.getOne(queryWrapper);
		 if(failuresCount==null) {
			 return Result.error("未找到对应数据");
		 }
		 failuresCountService.removeById(failuresCount.getId());
		 return Result.ok("删除成功!");
	 }

	/**
	 *   添加
	 *
	 * @param failuresCount
	 * @return
	 */
	@AutoLog(value = "FAILURES_COUNT-添加")
	@ApiOperation(value="FAILURES_COUNT-添加", notes="FAILURES_COUNT-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FailuresCount failuresCount) {
		if (!StringUtils.isEmpty(failuresCount.getId()) && !BeanUtil.isEmpty(failuresCountService.getById(failuresCount.getId()))){
			failuresCountService.updateById(failuresCount);
		}else{
			failuresCountService.save(failuresCount);
		}
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param failuresCount
	 * @return
	 */
	@AutoLog(value = "FAILURES_COUNT-编辑")
	@ApiOperation(value="FAILURES_COUNT-编辑", notes="FAILURES_COUNT-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FailuresCount failuresCount) {
		failuresCountService.updateById(failuresCount);
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "FAILURES_COUNT-通过id删除")
	@ApiOperation(value="FAILURES_COUNT-通过id删除", notes="FAILURES_COUNT-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		failuresCountService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "FAILURES_COUNT-批量删除")
	@ApiOperation(value="FAILURES_COUNT-批量删除", notes="FAILURES_COUNT-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.failuresCountService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "FAILURES_COUNT-通过id查询")
	@ApiOperation(value="FAILURES_COUNT-通过id查询", notes="FAILURES_COUNT-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FailuresCount failuresCount = failuresCountService.getById(id);
		if(failuresCount==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(failuresCount);
	}



}
