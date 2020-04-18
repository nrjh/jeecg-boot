package com.nrjh.iop.modules.prd.category.controller;

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
import com.nrjh.iop.modules.prd.category.entity.PrdCategory;
import com.nrjh.iop.modules.prd.category.service.IPrdCategoryService;

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
 * @Description: 品类
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
@Api(tags="品类")
@RestController
@RequestMapping("/iop/prd/category")
@Slf4j
public class PrdCategoryController extends JeecgController<PrdCategory, IPrdCategoryService> {
	@Autowired
	private IPrdCategoryService prdCategoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param prdCategory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "品类-分页列表查询")
	@ApiOperation(value="品类-分页列表查询", notes="品类-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PrdCategory prdCategory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PrdCategory> queryWrapper = QueryGenerator.initQueryWrapper(prdCategory, req.getParameterMap());
		Page<PrdCategory> page = new Page<PrdCategory>(pageNo, pageSize);
		IPage<PrdCategory> pageList = prdCategoryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param prdCategory
	 * @return
	 */
	@AutoLog(value = "品类-添加")
	@ApiOperation(value="品类-添加", notes="品类-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PrdCategory prdCategory) {
		prdCategoryService.save(prdCategory);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param prdCategory
	 * @return
	 */
	@AutoLog(value = "品类-编辑")
	@ApiOperation(value="品类-编辑", notes="品类-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PrdCategory prdCategory) {
		prdCategoryService.updateById(prdCategory);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "品类-通过id删除")
	@ApiOperation(value="品类-通过id删除", notes="品类-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		prdCategoryService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "品类-批量删除")
	@ApiOperation(value="品类-批量删除", notes="品类-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.prdCategoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "品类-通过id查询")
	@ApiOperation(value="品类-通过id查询", notes="品类-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PrdCategory prdCategory = prdCategoryService.getById(id);
		if(prdCategory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(prdCategory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param prdCategory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PrdCategory prdCategory) {
        return super.exportXls(request, prdCategory, PrdCategory.class, "品类");
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
        return super.importExcel(request, response, PrdCategory.class);
    }

}
