package com.nrjh.iop.modules.stk.quant.controller;

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
import com.nrjh.iop.modules.stk.quant.entity.StkQuant;
import com.nrjh.iop.modules.stk.quant.service.IStkQuantService;

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
 * @Description: 库存数量
 * @Author: jeecg-boot
 * @Date:   2020-06-23
 * @Version: V1.0
 */
@Api(tags="库存数量")
@RestController
@RequestMapping("/iop/stk/quant")
@Slf4j
public class StkQuantController extends JeecgController<StkQuant, IStkQuantService> {
	@Autowired
	private IStkQuantService stkQuantService;
	
	/**
	 * 分页列表查询
	 *
	 * @param stkQuant
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "库存数量-分页列表查询")
	@ApiOperation(value="库存数量-分页列表查询", notes="库存数量-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StkQuant stkQuant,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StkQuant> queryWrapper = QueryGenerator.initQueryWrapper(stkQuant, req.getParameterMap());
		Page<StkQuant> page = new Page<StkQuant>(pageNo, pageSize);
		IPage<StkQuant> pageList = stkQuantService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param stkQuant
	 * @return
	 */
	@AutoLog(value = "库存数量-添加")
	@ApiOperation(value="库存数量-添加", notes="库存数量-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StkQuant stkQuant) {
		stkQuantService.save(stkQuant);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param stkQuant
	 * @return
	 */
	@AutoLog(value = "库存数量-编辑")
	@ApiOperation(value="库存数量-编辑", notes="库存数量-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StkQuant stkQuant) {
		stkQuantService.updateById(stkQuant);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存数量-通过id删除")
	@ApiOperation(value="库存数量-通过id删除", notes="库存数量-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		stkQuantService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "库存数量-批量删除")
	@ApiOperation(value="库存数量-批量删除", notes="库存数量-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.stkQuantService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存数量-通过id查询")
	@ApiOperation(value="库存数量-通过id查询", notes="库存数量-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StkQuant stkQuant = stkQuantService.getById(id);
		if(stkQuant==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(stkQuant);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param stkQuant
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StkQuant stkQuant) {
        return super.exportXls(request, stkQuant, StkQuant.class, "库存数量");
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
        return super.importExcel(request, response, StkQuant.class);
    }

}
