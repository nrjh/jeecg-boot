package com.nrjh.iop.modules.prd.attribute.controller;

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
import com.nrjh.iop.modules.prd.attribute.entity.PrdAttributeValue;
import com.nrjh.iop.modules.prd.attribute.entity.PrdAttribute;
import com.nrjh.iop.modules.prd.attribute.vo.PrdAttributePage;
import com.nrjh.iop.modules.prd.attribute.service.IPrdAttributeService;
import com.nrjh.iop.modules.prd.attribute.service.IPrdAttributeValueService;
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
 * @Description: 属性
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
@Api(tags="属性")
@RestController
@RequestMapping("/iop/prd/attribute")
@Slf4j
public class PrdAttributeController {
	@Autowired
	private IPrdAttributeService prdAttributeService;
	@Autowired
	private IPrdAttributeValueService prdAttributeValueService;
	
	/**
	 * 分页列表查询
	 *
	 * @param prdAttribute
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "属性-分页列表查询")
	@ApiOperation(value="属性-分页列表查询", notes="属性-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PrdAttribute prdAttribute,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PrdAttribute> queryWrapper = QueryGenerator.initQueryWrapper(prdAttribute, req.getParameterMap());
		Page<PrdAttribute> page = new Page<PrdAttribute>(pageNo, pageSize);
		IPage<PrdAttribute> pageList = prdAttributeService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param prdAttributePage
	 * @return
	 */
	@AutoLog(value = "属性-添加")
	@ApiOperation(value="属性-添加", notes="属性-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PrdAttributePage prdAttributePage) {
		PrdAttribute prdAttribute = new PrdAttribute();
		BeanUtils.copyProperties(prdAttributePage, prdAttribute);
		prdAttributeService.saveMain(prdAttribute, prdAttributePage.getPrdAttributeValueList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param prdAttributePage
	 * @return
	 */
	@AutoLog(value = "属性-编辑")
	@ApiOperation(value="属性-编辑", notes="属性-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PrdAttributePage prdAttributePage) {
		PrdAttribute prdAttribute = new PrdAttribute();
		BeanUtils.copyProperties(prdAttributePage, prdAttribute);
		PrdAttribute prdAttributeEntity = prdAttributeService.getById(prdAttribute.getId());
		if(prdAttributeEntity==null) {
			return Result.error("未找到对应数据");
		}
		prdAttributeService.updateMain(prdAttribute, prdAttributePage.getPrdAttributeValueList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "属性-通过id删除")
	@ApiOperation(value="属性-通过id删除", notes="属性-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		prdAttributeService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "属性-批量删除")
	@ApiOperation(value="属性-批量删除", notes="属性-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.prdAttributeService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "属性-通过id查询")
	@ApiOperation(value="属性-通过id查询", notes="属性-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PrdAttribute prdAttribute = prdAttributeService.getById(id);
		if(prdAttribute==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(prdAttribute);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "属性值集合-通过id查询")
	@ApiOperation(value="属性值集合-通过id查询", notes="属性值-通过id查询")
	@GetMapping(value = "/queryPrdAttributeValueByMainId")
	public Result<?> queryPrdAttributeValueListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PrdAttributeValue> prdAttributeValueList = prdAttributeValueService.selectByMainId(id);
		return Result.ok(prdAttributeValueList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param prdAttribute
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PrdAttribute prdAttribute) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PrdAttribute> queryWrapper = QueryGenerator.initQueryWrapper(prdAttribute, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<PrdAttribute> queryList = prdAttributeService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<PrdAttribute> prdAttributeList = new ArrayList<PrdAttribute>();
      if(oConvertUtils.isEmpty(selections)) {
          prdAttributeList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          prdAttributeList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<PrdAttributePage> pageList = new ArrayList<PrdAttributePage>();
      for (PrdAttribute main : prdAttributeList) {
          PrdAttributePage vo = new PrdAttributePage();
          BeanUtils.copyProperties(main, vo);
          List<PrdAttributeValue> prdAttributeValueList = prdAttributeValueService.selectByMainId(main.getId().toString());
          vo.setPrdAttributeValueList(prdAttributeValueList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "属性列表");
      mv.addObject(NormalExcelConstants.CLASS, PrdAttributePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("属性数据", "导出人:"+sysUser.getRealname(), "属性"));
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
              List<PrdAttributePage> list = ExcelImportUtil.importExcel(file.getInputStream(), PrdAttributePage.class, params);
              for (PrdAttributePage page : list) {
                  PrdAttribute po = new PrdAttribute();
                  BeanUtils.copyProperties(page, po);
                  prdAttributeService.saveMain(po, page.getPrdAttributeValueList());
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
