package com.nrjh.iop.modules.prd.attrcategory.controller;

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
import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttrCategAttrValueRel;
import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttributeCategory;
import com.nrjh.iop.modules.prd.attrcategory.vo.PrdAttributeCategoryPage;
import com.nrjh.iop.modules.prd.attrcategory.service.IPrdAttributeCategoryService;
import com.nrjh.iop.modules.prd.attrcategory.service.IPrdAttrCategAttrValueRelService;
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
 * @Description: 规格
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
@Api(tags="规格")
@RestController
@RequestMapping("/iop/prd/attrcategory")
@Slf4j
public class PrdAttributeCategoryController {
	@Autowired
	private IPrdAttributeCategoryService prdAttributeCategoryService;
	@Autowired
	private IPrdAttrCategAttrValueRelService prdAttrCategAttrValueRelService;
	
	/**
	 * 分页列表查询
	 *
	 * @param prdAttributeCategory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "规格-分页列表查询")
	@ApiOperation(value="规格-分页列表查询", notes="规格-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PrdAttributeCategory prdAttributeCategory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PrdAttributeCategory> queryWrapper = QueryGenerator.initQueryWrapper(prdAttributeCategory, req.getParameterMap());
		Page<PrdAttributeCategory> page = new Page<PrdAttributeCategory>(pageNo, pageSize);
		IPage<PrdAttributeCategory> pageList = prdAttributeCategoryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param prdAttributeCategoryPage
	 * @return
	 */
	@AutoLog(value = "规格-添加")
	@ApiOperation(value="规格-添加", notes="规格-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PrdAttributeCategoryPage prdAttributeCategoryPage) {
		PrdAttributeCategory prdAttributeCategory = new PrdAttributeCategory();
		BeanUtils.copyProperties(prdAttributeCategoryPage, prdAttributeCategory);
		prdAttributeCategoryService.saveMain(prdAttributeCategory, prdAttributeCategoryPage.getPrdAttrCategAttrValueRelList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param prdAttributeCategoryPage
	 * @return
	 */
	@AutoLog(value = "规格-编辑")
	@ApiOperation(value="规格-编辑", notes="规格-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PrdAttributeCategoryPage prdAttributeCategoryPage) {
		PrdAttributeCategory prdAttributeCategory = new PrdAttributeCategory();
		BeanUtils.copyProperties(prdAttributeCategoryPage, prdAttributeCategory);
		PrdAttributeCategory prdAttributeCategoryEntity = prdAttributeCategoryService.getById(prdAttributeCategory.getId());
		if(prdAttributeCategoryEntity==null) {
			return Result.error("未找到对应数据");
		}
		prdAttributeCategoryService.updateMain(prdAttributeCategory, prdAttributeCategoryPage.getPrdAttrCategAttrValueRelList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "规格-通过id删除")
	@ApiOperation(value="规格-通过id删除", notes="规格-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		prdAttributeCategoryService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "规格-批量删除")
	@ApiOperation(value="规格-批量删除", notes="规格-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.prdAttributeCategoryService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "规格-通过id查询")
	@ApiOperation(value="规格-通过id查询", notes="规格-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PrdAttributeCategory prdAttributeCategory = prdAttributeCategoryService.getById(id);
		if(prdAttributeCategory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(prdAttributeCategory);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物品规格与产品属性值关系集合-通过id查询")
	@ApiOperation(value="物品规格与产品属性值关系集合-通过id查询", notes="物品规格与产品属性值关系-通过id查询")
	@GetMapping(value = "/queryPrdAttrCategAttrValueRelByMainId")
	public Result<?> queryPrdAttrCategAttrValueRelListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PrdAttrCategAttrValueRel> prdAttrCategAttrValueRelList = prdAttrCategAttrValueRelService.selectByMainId(id);
		return Result.ok(prdAttrCategAttrValueRelList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param prdAttributeCategory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PrdAttributeCategory prdAttributeCategory) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PrdAttributeCategory> queryWrapper = QueryGenerator.initQueryWrapper(prdAttributeCategory, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<PrdAttributeCategory> queryList = prdAttributeCategoryService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<PrdAttributeCategory> prdAttributeCategoryList = new ArrayList<PrdAttributeCategory>();
      if(oConvertUtils.isEmpty(selections)) {
          prdAttributeCategoryList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          prdAttributeCategoryList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<PrdAttributeCategoryPage> pageList = new ArrayList<PrdAttributeCategoryPage>();
      for (PrdAttributeCategory main : prdAttributeCategoryList) {
          PrdAttributeCategoryPage vo = new PrdAttributeCategoryPage();
          BeanUtils.copyProperties(main, vo);
          List<PrdAttrCategAttrValueRel> prdAttrCategAttrValueRelList = prdAttrCategAttrValueRelService.selectByMainId(main.getId().toString());
          vo.setPrdAttrCategAttrValueRelList(prdAttrCategAttrValueRelList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "规格列表");
      mv.addObject(NormalExcelConstants.CLASS, PrdAttributeCategoryPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("规格数据", "导出人:"+sysUser.getRealname(), "规格"));
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
              List<PrdAttributeCategoryPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PrdAttributeCategoryPage.class, params);
              for (PrdAttributeCategoryPage page : list) {
                  PrdAttributeCategory po = new PrdAttributeCategory();
                  BeanUtils.copyProperties(page, po);
                  prdAttributeCategoryService.saveMain(po, page.getPrdAttrCategAttrValueRelList());
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
