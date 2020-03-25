package com.nrjh.iop.modules.reserve.categ.controller;

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
import com.nrjh.iop.modules.reserve.categ.entity.IopOReserveCateg;
import com.nrjh.iop.modules.reserve.categ.service.IIopOReserveCategService;

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
 * @Description: 类别信息
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
@RestController
@RequestMapping("/iop/reserve/categ")
@Slf4j
public class IopOReserveCategController extends JeecgController<IopOReserveCateg, IIopOReserveCategService>{
	@Autowired
	private IIopOReserveCategService iopOReserveCategService;
	
	/**
	 * 分页列表查询
	 *
	 * @param iopOReserveCateg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(IopOReserveCateg iopOReserveCateg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String parentId = iopOReserveCateg.getPid();
		if (oConvertUtils.isEmpty(parentId)) {
			parentId = "0";
		}
		iopOReserveCateg.setPid(null);
		QueryWrapper<IopOReserveCateg> queryWrapper = QueryGenerator.initQueryWrapper(iopOReserveCateg, req.getParameterMap());
		// 使用 eq 防止模糊查询
		queryWrapper.eq("pid", parentId);
		Page<IopOReserveCateg> page = new Page<IopOReserveCateg>(pageNo, pageSize);
		IPage<IopOReserveCateg> pageList = iopOReserveCategService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
      * 获取子数据
      * @param iopOReserveCateg
      * @param req
      * @return
      */
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(IopOReserveCateg iopOReserveCateg,HttpServletRequest req) {
		QueryWrapper<IopOReserveCateg> queryWrapper = QueryGenerator.initQueryWrapper(iopOReserveCateg, req.getParameterMap());
		List<IopOReserveCateg> list = iopOReserveCategService.list(queryWrapper);
		return Result.ok(list);
	}
	
	
	/**
	 *   添加
	 *
	 * @param iopOReserveCateg
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody IopOReserveCateg iopOReserveCateg) {
		iopOReserveCategService.addIopOReserveCateg(iopOReserveCateg);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param iopOReserveCateg
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody IopOReserveCateg iopOReserveCateg) {
		iopOReserveCategService.updateIopOReserveCateg(iopOReserveCateg);
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
		iopOReserveCategService.deleteIopOReserveCateg(id);
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
		this.iopOReserveCategService.removeByIds(Arrays.asList(ids.split(",")));
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
		IopOReserveCateg iopOReserveCateg = iopOReserveCategService.getById(id);
		if(iopOReserveCateg==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(iopOReserveCateg);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param iopOReserveCateg
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IopOReserveCateg iopOReserveCateg) {
		return super.exportXls(request, iopOReserveCateg, IopOReserveCateg.class, "类别信息");
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
		return super.importExcel(request, response, IopOReserveCateg.class);
    }

}
