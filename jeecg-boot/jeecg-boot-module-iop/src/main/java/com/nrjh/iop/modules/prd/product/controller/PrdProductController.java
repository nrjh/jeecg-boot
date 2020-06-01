package com.nrjh.iop.modules.prd.product.controller;

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
import com.nrjh.iop.modules.prd.product.entity.PrdProductAttrValueRel;
import com.nrjh.iop.modules.prd.product.entity.PrdProduct;
import com.nrjh.iop.modules.prd.product.vo.PrdProductPage;
import com.nrjh.iop.modules.prd.product.service.IPrdProductService;
import com.nrjh.iop.modules.prd.product.service.IPrdProductAttrValueRelService;
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
 * @Description: 物品
 * @Author: jeecg-boot
 * @Date:   2020-05-06
 * @Version: V1.0
 */
@Api(tags="物品")
@RestController
@RequestMapping("/iop/prd/product")
@Slf4j
public class PrdProductController {
	@Autowired
	private IPrdProductService prdProductService;
	@Autowired
	private IPrdProductAttrValueRelService prdProductAttrValueRelService;
	
	/**
	 * 分页列表查询
	 *
	 * @param prdProduct
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "物品-分页列表查询")
	@ApiOperation(value="物品-分页列表查询", notes="物品-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PrdProduct prdProduct,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PrdProduct> queryWrapper = QueryGenerator.initQueryWrapper(prdProduct, req.getParameterMap());
		Page<PrdProduct> page = new Page<PrdProduct>(pageNo, pageSize);
		IPage<PrdProduct> pageList = prdProductService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param prdProductPage
	 * @return
	 */
	@AutoLog(value = "物品-添加")
	@ApiOperation(value="物品-添加", notes="物品-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PrdProductPage prdProductPage) {
		PrdProduct prdProduct = new PrdProduct();
		BeanUtils.copyProperties(prdProductPage, prdProduct);
		prdProduct.setActive(1);  // 默认启用
		prdProductService.saveMain(prdProduct, prdProductPage.getPrdProductAttrValueRelList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param prdProductPage
	 * @return
	 */
	@AutoLog(value = "物品-编辑")
	@ApiOperation(value="物品-编辑", notes="物品-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PrdProductPage prdProductPage) {
		PrdProduct prdProduct = new PrdProduct();
		BeanUtils.copyProperties(prdProductPage, prdProduct);
		PrdProduct prdProductEntity = prdProductService.getById(prdProduct.getId());
		if(prdProductEntity==null) {
			return Result.error("未找到对应数据");
		}
		prdProductService.updateMain(prdProduct, prdProductPage.getPrdProductAttrValueRelList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物品-通过id删除")
	@ApiOperation(value="物品-通过id删除", notes="物品-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		prdProductService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "物品-批量删除")
	@ApiOperation(value="物品-批量删除", notes="物品-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.prdProductService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物品-通过id查询")
	@ApiOperation(value="物品-通过id查询", notes="物品-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PrdProduct prdProduct = prdProductService.getById(id);
		if(prdProduct==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(prdProduct);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物品属性值关系集合-通过id查询")
	@ApiOperation(value="物品属性值关系集合-通过id查询", notes="物品属性值关系-通过id查询")
	@GetMapping(value = "/queryPrdProductAttrValueRelByMainId")
	public Result<?> queryPrdProductAttrValueRelListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PrdProductAttrValueRel> prdProductAttrValueRelList = prdProductAttrValueRelService.selectByMainId(id);
		return Result.ok(prdProductAttrValueRelList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param prdProduct
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PrdProduct prdProduct) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PrdProduct> queryWrapper = QueryGenerator.initQueryWrapper(prdProduct, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<PrdProduct> queryList = prdProductService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<PrdProduct> prdProductList = new ArrayList<PrdProduct>();
      if(oConvertUtils.isEmpty(selections)) {
          prdProductList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          prdProductList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<PrdProductPage> pageList = new ArrayList<PrdProductPage>();
      for (PrdProduct main : prdProductList) {
          PrdProductPage vo = new PrdProductPage();
          BeanUtils.copyProperties(main, vo);
          List<PrdProductAttrValueRel> prdProductAttrValueRelList = prdProductAttrValueRelService.selectByMainId(main.getId().toString());
          vo.setPrdProductAttrValueRelList(prdProductAttrValueRelList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "物品列表");
      mv.addObject(NormalExcelConstants.CLASS, PrdProductPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("物品数据", "导出人:"+sysUser.getRealname(), "物品"));
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
              List<PrdProductPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PrdProductPage.class, params);
              for (PrdProductPage page : list) {
                  PrdProduct po = new PrdProduct();
                  BeanUtils.copyProperties(page, po);
                  prdProductService.saveMain(po, page.getPrdProductAttrValueRelList());
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
