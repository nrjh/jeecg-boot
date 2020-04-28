package com.nrjh.iop.modules.stk.location.controller;

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
import com.nrjh.iop.modules.stk.location.entity.StkLocation;
import com.nrjh.iop.modules.stk.location.service.IStkLocationService;

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

 /**
 * @Description: 位置
 * @Author: jeecg-boot
 * @Date:   2020-04-26
 * @Version: V1.0
 */
@RestController
@RequestMapping("/iop/stk/location")
@Slf4j
public class StkLocationController extends JeecgController<StkLocation, IStkLocationService>{
	@Autowired
	private IStkLocationService stkLocationService;
	
	/**
	 * 分页列表查询
	 *
	 * @param stkLocation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(StkLocation stkLocation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String parentId = stkLocation.getPid();
		if (oConvertUtils.isEmpty(parentId)) {
			parentId = "1";  // 只看物理位置
		}
		stkLocation.setPid(null);
		QueryWrapper<StkLocation> queryWrapper = QueryGenerator.initQueryWrapper(stkLocation, req.getParameterMap());
		// 使用 eq 防止模糊查询
		queryWrapper.eq("pid", parentId);
		Page<StkLocation> page = new Page<StkLocation>(pageNo, pageSize);
		IPage<StkLocation> pageList = stkLocationService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
      * 获取子数据
      * @param stkLocation
      * @param req
      * @return
      */
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(StkLocation stkLocation,HttpServletRequest req) {
		QueryWrapper<StkLocation> queryWrapper = QueryGenerator.initQueryWrapper(stkLocation, req.getParameterMap());
		List<StkLocation> list = stkLocationService.list(queryWrapper);
		return Result.ok(list);
	}
	
	
	/**
	 *   添加
	 *
	 * @param stkLocation
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StkLocation stkLocation) {
		stkLocationService.addStkLocation(stkLocation);
		// 更新全名和全路径
		stkLocationService.updateComplateName(stkLocation);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param stkLocation
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StkLocation stkLocation) {
		stkLocationService.updateStkLocation(stkLocation);
		// 更新全名和全路径
		stkLocationService.updateComplateName(stkLocation);
		return Result.ok("编辑成功!");
	}
	
	/**
	  *   通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @DeleteMapping(value = "/delete")
	 public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		 stkLocationService.deleteStkLocation(id);
		 return Result.ok("删除成功!");
	 }
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.stkLocationService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StkLocation stkLocation = stkLocationService.getById(id);
		if(stkLocation==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(stkLocation);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param stkLocation
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StkLocation stkLocation) {
		return super.exportXls(request, stkLocation, StkLocation.class, "位置");
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
		return super.importExcel(request, response, StkLocation.class);
    }

}
