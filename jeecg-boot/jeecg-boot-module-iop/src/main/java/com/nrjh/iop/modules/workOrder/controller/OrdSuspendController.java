package com.nrjh.iop.modules.workOrder.controller;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nrjh.iop.modules.workOrder.entity.OrdSuspend;
import com.nrjh.iop.modules.workOrder.service.IOrdSuspendService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 工单挂单表
 * @Author: lei-li
 * @Date:   2020-09-03
 * @Version: V1.0
 */
@Api(tags="工单挂单表")
@RestController
@RequestMapping("/ordSuspend/ordSuspend")
@Slf4j
public class OrdSuspendController extends JeecgController<OrdSuspend, IOrdSuspendService> {
	@Autowired
	private IOrdSuspendService ordSuspendService;

	/**
	 * 分页列表查询
	 *
	 * @param ordSuspend
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "工单挂单表-分页列表查询")
	@ApiOperation(value="工单挂单表-分页列表查询", notes="工单挂单表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(OrdSuspend ordSuspend,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<OrdSuspend> queryWrapper = QueryGenerator.initQueryWrapper(ordSuspend, req.getParameterMap());
		Page<OrdSuspend> page = new Page<OrdSuspend>(pageNo, pageSize);
		IPage<OrdSuspend> pageList = ordSuspendService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param ordSuspend
	 * @return
	 */
	@AutoLog(value = "工单挂单表-添加")
	@ApiOperation(value="工单挂单表-添加", notes="工单挂单表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody OrdSuspend ordSuspend) {
		ordSuspend.setCreateTime(new Date());
		//获取当前登录用户信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		ordSuspend.setCreateName(sysUser.getRealname());
		ordSuspend.setCreateId(sysUser.getId());
		ordSuspendService.Suspend(ordSuspend);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param ordSuspend
	 * @return
	 */
	@AutoLog(value = "工单挂单表-编辑")
	@ApiOperation(value="工单挂单表-编辑", notes="工单挂单表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody OrdSuspend ordSuspend) {
		ordSuspendService.updateById(ordSuspend);
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "工单挂单表-通过id删除")
	@ApiOperation(value="工单挂单表-通过id删除", notes="工单挂单表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ordSuspendService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "工单挂单表-批量删除")
	@ApiOperation(value="工单挂单表-批量删除", notes="工单挂单表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ordSuspendService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "工单挂单表-通过id查询")
	@ApiOperation(value="工单挂单表-通过id查询", notes="工单挂单表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		OrdSuspend ordSuspend = ordSuspendService.getById(id);
		if(ordSuspend==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(ordSuspend);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ordSuspend
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OrdSuspend ordSuspend) {
        return super.exportXls(request, ordSuspend, OrdSuspend.class, "工单挂单表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, OrdSuspend.class);
    }

}
