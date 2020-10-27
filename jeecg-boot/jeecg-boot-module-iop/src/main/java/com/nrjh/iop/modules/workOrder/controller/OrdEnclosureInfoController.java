package com.nrjh.iop.modules.workOrder.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.workOrder.entity.OrdEnclosureInfo;
import com.nrjh.iop.modules.workOrder.service.IOrdEnclosureInfoService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;


import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 工单附件表
 * @Author: lei-li
 * @Date:   2020-09-02
 * @Version: V1.0
 */
@Api(tags="工单附件表")
@RestController
@RequestMapping("/ordEnclosure/ordEnclosureInfo")
@Slf4j
public class OrdEnclosureInfoController extends JeecgController<OrdEnclosureInfo, IOrdEnclosureInfoService> {
	@Autowired
	private IOrdEnclosureInfoService ordEnclosureInfoService;

	/**
	 * 分页列表查询
	 *
	 * @param ordEnclosureInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "工单附件表-分页列表查询")
	@ApiOperation(value="工单附件表-分页列表查询", notes="工单附件表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(OrdEnclosureInfo ordEnclosureInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<OrdEnclosureInfo> queryWrapper = QueryGenerator.initQueryWrapper(ordEnclosureInfo, req.getParameterMap());
		Page<OrdEnclosureInfo> page = new Page<OrdEnclosureInfo>(pageNo, pageSize);
		IPage<OrdEnclosureInfo> pageList = ordEnclosureInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param ordEnclosureInfo
	 * @return
	 */
	@AutoLog(value = "工单附件表-添加")
	@ApiOperation(value="工单附件表-添加", notes="工单附件表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody OrdEnclosureInfo ordEnclosureInfo) {
		ordEnclosureInfoService.save(ordEnclosureInfo);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param ordEnclosureInfo
	 * @return
	 */
	@AutoLog(value = "工单附件表-编辑")
	@ApiOperation(value="工单附件表-编辑", notes="工单附件表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody OrdEnclosureInfo ordEnclosureInfo) {
		ordEnclosureInfoService.updateById(ordEnclosureInfo);
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "工单附件表-通过id删除")
	@ApiOperation(value="工单附件表-通过id删除", notes="工单附件表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ordEnclosureInfoService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "工单附件表-批量删除")
	@ApiOperation(value="工单附件表-批量删除", notes="工单附件表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ordEnclosureInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "工单附件表-通过id查询")
	@ApiOperation(value="工单附件表-通过id查询", notes="工单附件表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		OrdEnclosureInfo ordEnclosureInfo = ordEnclosureInfoService.getById(id);
		if(ordEnclosureInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(ordEnclosureInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ordEnclosureInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OrdEnclosureInfo ordEnclosureInfo) {
        return super.exportXls(request, ordEnclosureInfo, OrdEnclosureInfo.class, "工单附件表");
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
        return super.importExcel(request, response, OrdEnclosureInfo.class);
    }

     /**
      * 通过工单单号查询附件信息
      *
      * @param workOrderId
      * @return
      */
     @AutoLog(value = "通过工单单号查询附件信息")
     @ApiOperation(value="通过工单单号查询附件信息", notes="通过工单单号查询附件信息")
     @GetMapping(value = "/queryEnclosureInfo")
     public Result<?> queryEnclosureInfo(@RequestParam(name="id",required=true) String workOrderId) {
         OrdEnclosureInfo ordEnclosureInfo = ordEnclosureInfoService.queryEnclosureInfo(workOrderId);
         if(ordEnclosureInfo==null) {
             return Result.error("未找到对应数据");
         }
         return Result.ok(ordEnclosureInfo);
     }
}
