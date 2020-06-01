package com.nrjh.iop.modules.stk.move.controller;

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
import com.nrjh.iop.modules.stk.move.entity.StkMoveLine;
import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.nrjh.iop.modules.stk.move.vo.StkMovePage;
import com.nrjh.iop.modules.stk.move.service.IStkMoveService;
import com.nrjh.iop.modules.stk.move.service.IStkMoveLineService;
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
 * @Description: 移库单
 * @Author: jeecg-boot
 * @Date:   2020-05-13
 * @Version: V1.0
 */
@Api(tags="移库单")
@RestController
@RequestMapping("/iop/stk/move")
@Slf4j
public class StkMoveController {
	@Autowired
	private IStkMoveService stkMoveService;
	@Autowired
	private IStkMoveLineService stkMoveLineService;

	/**
	 * 分页列表查询
	 *
	 * @param stkMove
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "移库单-分页列表查询")
	@ApiOperation(value="移库单-分页列表查询", notes="移库单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StkMove stkMove,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StkMove> queryWrapper = QueryGenerator.initQueryWrapper(stkMove, req.getParameterMap());
		Page<StkMove> page = new Page<StkMove>(pageNo, pageSize);
		IPage<StkMove> pageList = stkMoveService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param stkMovePage
	 * @return
	 */
	@AutoLog(value = "移库单-添加")
	@ApiOperation(value="移库单-添加", notes="移库单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StkMovePage stkMovePage) {
		StkMove stkMove = new StkMove();
		BeanUtils.copyProperties(stkMovePage, stkMove);
		stkMoveService.saveMain(stkMove, stkMovePage.getStkMoveLineList());
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param stkMovePage
	 * @return
	 */
	@AutoLog(value = "移库单-编辑")
	@ApiOperation(value="移库单-编辑", notes="移库单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StkMovePage stkMovePage) {
		StkMove stkMove = new StkMove();
		BeanUtils.copyProperties(stkMovePage, stkMove);
		StkMove stkMoveEntity = stkMoveService.getById(stkMove.getId());
		if(stkMoveEntity==null) {
			return Result.error("未找到对应数据");
		}
		stkMoveService.updateMain(stkMove, stkMovePage.getStkMoveLineList());
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "移库单-通过id删除")
	@ApiOperation(value="移库单-通过id删除", notes="移库单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		stkMoveService.delMain(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "移库单-批量删除")
	@ApiOperation(value="移库单-批量删除", notes="移库单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.stkMoveService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "移库单-通过id查询")
	@ApiOperation(value="移库单-通过id查询", notes="移库单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StkMove stkMove = stkMoveService.getById(id);
		if(stkMove==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(stkMove);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存移动明细集合-通过id查询")
	@ApiOperation(value="库存移动明细集合-通过id查询", notes="库存移动明细-通过id查询")
	@GetMapping(value = "/queryStkMoveLineByMainId")
	public Result<?> queryStkMoveLineListByMainId(@RequestParam(name="id",required=true) String id) {
		List<StkMoveLine> stkMoveLineList = stkMoveLineService.selectByMainId(id);
		return Result.ok(stkMoveLineList);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param stkMove
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, StkMove stkMove) {
		// Step.1 组装查询条件查询数据
		QueryWrapper<StkMove> queryWrapper = QueryGenerator.initQueryWrapper(stkMove, request.getParameterMap());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		//Step.2 获取导出数据
		List<StkMove> queryList = stkMoveService.list(queryWrapper);
		// 过滤选中数据
		String selections = request.getParameter("selections");
		List<StkMove> stkMoveList = new ArrayList<StkMove>();
		if(oConvertUtils.isEmpty(selections)) {
			stkMoveList = queryList;
		}else {
			List<String> selectionList = Arrays.asList(selections.split(","));
			stkMoveList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		}

		// Step.3 组装pageList
		List<StkMovePage> pageList = new ArrayList<StkMovePage>();
		for (StkMove main : stkMoveList) {
			StkMovePage vo = new StkMovePage();
			BeanUtils.copyProperties(main, vo);
			List<StkMoveLine> stkMoveLineList = stkMoveLineService.selectByMainId(main.getId().toString());
			vo.setStkMoveLineList(stkMoveLineList);
			pageList.add(vo);
		}

		// Step.4 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "移库单列表");
		mv.addObject(NormalExcelConstants.CLASS, StkMovePage.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("移库单数据", "导出人:"+sysUser.getRealname(), "移库单"));
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
				List<StkMovePage> list = ExcelImportUtil.importExcel(file.getInputStream(), StkMovePage.class, params);
				for (StkMovePage page : list) {
					StkMove po = new StkMove();
					BeanUtils.copyProperties(page, po);
					stkMoveService.saveMain(po, page.getStkMoveLineList());
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
