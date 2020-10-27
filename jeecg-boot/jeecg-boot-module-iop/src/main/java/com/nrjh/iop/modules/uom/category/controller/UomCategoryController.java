package com.nrjh.iop.modules.uom.category.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.uom.category.entity.UomUom;
import com.nrjh.iop.modules.uom.category.entity.UomCategory;
import com.nrjh.iop.modules.uom.category.vo.UomCategoryPage;
import com.nrjh.iop.modules.uom.category.service.IUomCategoryService;
import com.nrjh.iop.modules.uom.category.service.IUomUomService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 计量单位类别
 * @Author: jeecg-boot
 * @Date:   2020-04-29
 * @Version: V1.0
 */
@Api(tags="计量单位类别")
@RestController
@RequestMapping("/iop/uom/category")
@Slf4j
public class UomCategoryController {
	@Autowired
	private IUomCategoryService uomCategoryService;
	@Autowired
	private IUomUomService uomUomService;

	/**
	 * 分页列表查询
	 *
	 * @param uomCategory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "计量单位类别-分页列表查询")
	@ApiOperation(value="计量单位类别-分页列表查询", notes="计量单位类别-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(UomCategory uomCategory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<UomCategory> queryWrapper = QueryGenerator.initQueryWrapper(uomCategory, req.getParameterMap());
		Page<UomCategory> page = new Page<UomCategory>(pageNo, pageSize);
		IPage<UomCategory> pageList = uomCategoryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
	  * 获取单位的list
	  *
	  * @return
	  */
	 @AutoLog(value = "获取单位的list")
	 @ApiOperation(value="获取单位的list", notes="获取单位的list")
	 @GetMapping(value = "/uomUomList")
	 public Result<?> uomUomList() {
		 List<UomUom> uomUomList = uomUomService.list();
		 return Result.ok(uomUomList);
	 }

	/**
	 *   添加
	 *
	 * @param uomCategoryPage
	 * @return
	 */
	@AutoLog(value = "计量单位类别-添加")
	@ApiOperation(value="计量单位类别-添加", notes="计量单位类别-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody UomCategoryPage uomCategoryPage) {
		UomCategory uomCategory = new UomCategory();
		BeanUtils.copyProperties(uomCategoryPage, uomCategory);
		uomCategoryService.saveMain(uomCategory, uomCategoryPage.getUomUomList());
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param uomCategoryPage
	 * @return
	 */
	@AutoLog(value = "计量单位类别-编辑")
	@ApiOperation(value="计量单位类别-编辑", notes="计量单位类别-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody UomCategoryPage uomCategoryPage) {
		UomCategory uomCategory = new UomCategory();
		BeanUtils.copyProperties(uomCategoryPage, uomCategory);
		UomCategory uomCategoryEntity = uomCategoryService.getById(uomCategory.getId());
		if(uomCategoryEntity==null) {
			return Result.error("未找到对应数据");
		}
		uomCategoryService.updateMain(uomCategory, uomCategoryPage.getUomUomList());
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "计量单位类别-通过id删除")
	@ApiOperation(value="计量单位类别-通过id删除", notes="计量单位类别-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		uomCategoryService.delMain(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "计量单位类别-批量删除")
	@ApiOperation(value="计量单位类别-批量删除", notes="计量单位类别-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.uomCategoryService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "计量单位类别-通过id查询")
	@ApiOperation(value="计量单位类别-通过id查询", notes="计量单位类别-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		UomCategory uomCategory = uomCategoryService.getById(id);
		if(uomCategory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(uomCategory);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "计量单位集合-通过id查询")
	@ApiOperation(value="计量单位集合-通过id查询", notes="计量单位-通过id查询")
	@GetMapping(value = "/queryUomUomByMainId")
	public Result<?> queryUomUomListByMainId(@RequestParam(name="id",required=true) String id) {
		List<UomUom> uomUomList = uomUomService.selectByMainId(id);
		return Result.ok(uomUomList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param uomCategory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, UomCategory uomCategory) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<UomCategory> queryWrapper = QueryGenerator.initQueryWrapper(uomCategory, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<UomCategory> queryList = uomCategoryService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<UomCategory> uomCategoryList = new ArrayList<UomCategory>();
      if(oConvertUtils.isEmpty(selections)) {
          uomCategoryList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          uomCategoryList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<UomCategoryPage> pageList = new ArrayList<UomCategoryPage>();
      for (UomCategory main : uomCategoryList) {
          UomCategoryPage vo = new UomCategoryPage();
          BeanUtils.copyProperties(main, vo);
          List<UomUom> uomUomList = uomUomService.selectByMainId(main.getId().toString());
          vo.setUomUomList(uomUomList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "计量单位类别列表");
      mv.addObject(NormalExcelConstants.CLASS, UomCategoryPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("计量单位类别数据", "导出人:"+sysUser.getRealname(), "计量单位类别"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<UomCategoryPage> list = ExcelImportUtil.importExcel(file.getInputStream(), UomCategoryPage.class, params);
              for (UomCategoryPage page : list) {
                  UomCategory po = new UomCategory();
                  BeanUtils.copyProperties(page, po);
                  uomCategoryService.saveMain(po, page.getUomUomList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
    }

}
