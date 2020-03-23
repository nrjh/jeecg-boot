package com.nrjh.iop.modules.reserve.pallet.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.io.resource.ResourceUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.reserve.pallet.entity.IopOReservePallet;
import com.nrjh.iop.modules.reserve.pallet.service.IIopOReservePalletService;

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
 * @Description: 托盘维护
 * @Author: jeecg-boot
 * @Date:   2020-03-16
 * @Version: V1.0
 */
@Api(tags="托盘维护")
@RestController
@RequestMapping("/iop/reserve/iopOReservePallet")
@Slf4j
public class IopOReservePalletController extends JeecgController<IopOReservePallet, IIopOReservePalletService> {
	@Autowired
	private IIopOReservePalletService iopOReservePalletService;
	
	/**
	 * 分页列表查询
	 *
	 * @param iopOReservePallet
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "托盘维护-分页列表查询")
	@ApiOperation(value="托盘维护-分页列表查询", notes="托盘维护-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(IopOReservePallet iopOReservePallet,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<IopOReservePallet> queryWrapper = QueryGenerator.initQueryWrapper(iopOReservePallet, req.getParameterMap());
		Page<IopOReservePallet> page = new Page<IopOReservePallet>(pageNo, pageSize);
		IPage<IopOReservePallet> pageList = iopOReservePalletService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param iopOReservePallet
	 * @return
	 */
	@AutoLog(value = "托盘维护-添加")
	@ApiOperation(value="托盘维护-添加", notes="托盘维护-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody IopOReservePallet iopOReservePallet) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		iopOReservePallet.setOptCode(sysUser.getId());
		iopOReservePallet.setOptDate(new Date());
		iopOReservePalletService.save(iopOReservePallet);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param iopOReservePallet
	 * @return
	 */
	@AutoLog(value = "托盘维护-编辑")
	@ApiOperation(value="托盘维护-编辑", notes="托盘维护-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody IopOReservePallet iopOReservePallet) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		iopOReservePallet.setOptCode(sysUser.getUsername());
		iopOReservePallet.setOptDate(new Date());
		iopOReservePalletService.updateById(iopOReservePallet);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "托盘维护-通过id删除")
	@ApiOperation(value="托盘维护-通过id删除", notes="托盘维护-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		iopOReservePalletService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "托盘维护-批量删除")
	@ApiOperation(value="托盘维护-批量删除", notes="托盘维护-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.iopOReservePalletService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "托盘维护-通过id查询")
	@ApiOperation(value="托盘维护-通过id查询", notes="托盘维护-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		IopOReservePallet iopOReservePallet = iopOReservePalletService.getById(id);
		if(iopOReservePallet==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(iopOReservePallet);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param iopOReservePallet
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IopOReservePallet iopOReservePallet) {
        return super.exportXls(request, iopOReservePallet, IopOReservePallet.class, "托盘维护");
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
        return super.importExcel(request, response, IopOReservePallet.class);
    }

}
