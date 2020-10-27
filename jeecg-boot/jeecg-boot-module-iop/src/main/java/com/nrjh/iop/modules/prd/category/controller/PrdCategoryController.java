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

import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttributeCategory;
import com.nrjh.iop.modules.prd.attrcategory.service.IPrdAttributeCategoryService;
import com.nrjh.iop.modules.prd.product.service.IPrdProductService;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
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

	@Autowired
	private IPrdAttributeCategoryService prdAttributeCategoryService;

	@Autowired
	private IPrdProductService prdProductService;

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
		updateComplateName(prdCategory);
		return Result.ok("添加成功！");
	}

	 /**
	  * 更新代码路径和名称路径
	  * @param prdCategory
	  */
	private void updateComplateName( PrdCategory prdCategory){
		if(StringUtils.isNotBlank(prdCategory.getPid())){
			PrdCategory prdCategoryParent = prdCategoryService.getById(prdCategory.getPid().toString());
			if(prdCategoryParent!=null){
				prdCategory.setCompleteName(prdCategoryParent.getCompleteName()+"/"+prdCategory.getName());
				prdCategory.setIdPath(prdCategoryParent.getIdPath()+"/"+prdCategory.getId());
				prdCategoryService.updateById(prdCategory);
			}
		}
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
		updateComplateName(prdCategory);
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
		// 相关分类
		List<PrdCategory> prdCategoryList = prdCategoryService.selectCategoryListByCategoryId(id);
		// 相关规格
		List<PrdAttributeCategory> prdAttributeCategoryList = prdAttributeCategoryService.selectAttrCategoryListByCategoryId(id);
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("CATEGORY_ID",id);
		// 相关物品
		List prdProductList = prdProductService.list(queryWrapper);
		if (prdCategoryList.size() != 0 || prdAttributeCategoryList.size() != 0 || prdProductList.size() != 0){
			return Result.error("品类有关联设备,不可删除");
		}
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
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "品牌-通过id查询")
	 @ApiOperation(value = "品牌-通过id查询相关品类", notes = "品牌-通过id查询相关品类")
	 @GetMapping(value = "/queryPrdCategoryListByBrandId")
	 public Result<?> queryPrdCategoryListByBrandId(@RequestParam(name = "id", required = true) String id) {
		 List<PrdCategory> prdCategoryList = prdCategoryService.selectCategoryListByBrandId(id);
		 return Result.ok(prdCategoryList);
	 }
	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "品类-通过id查询相关品类")
	 @ApiOperation(value="品类-通过id查询相关品类", notes="品类-通过id查询相关品类")
	 @GetMapping(value = "/queryPrdCategoryListByCategoryId")
	 public Result<?> queryPrdCategoryListByCategoryId(@RequestParam(name="id",required=true) String id) {
		 List<PrdCategory> prdCategoryList = prdCategoryService.selectCategoryListByCategoryId(id);
		 return Result.ok(prdCategoryList);
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
