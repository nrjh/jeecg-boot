package com.nrjh.iop.modules.reserve.store.controller;

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
import com.nrjh.iop.modules.reserve.store.entity.IopOReserveStore;
import com.nrjh.iop.modules.reserve.store.service.IIopOReserveStoreService;

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
 * @Description: 库房维护
 * @Author: jeecg-boot
 * @Date:   2020-03-23
 * @Version: V1.0
 */
@RestController
@RequestMapping("/iop/reserve/store")
@Slf4j
public class IopOReserveStoreController extends JeecgController<IopOReserveStore, IIopOReserveStoreService>{
	@Autowired
	private IIopOReserveStoreService iopOReserveStoreService;
	
	/**
	 * 分页列表查询
	 *
	 * @param iopOReserveStore
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(IopOReserveStore iopOReserveStore,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String parentId = iopOReserveStore.getPid();
		if (oConvertUtils.isEmpty(parentId)) {
			parentId = "0";
		}
		iopOReserveStore.setPid(null);
		QueryWrapper<IopOReserveStore> queryWrapper = QueryGenerator.initQueryWrapper(iopOReserveStore, req.getParameterMap());
		// 使用 eq 防止模糊查询
		queryWrapper.eq("pid", parentId);
		Page<IopOReserveStore> page = new Page<IopOReserveStore>(pageNo, pageSize);
		IPage<IopOReserveStore> pageList = iopOReserveStoreService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
      * 获取子数据
      * @param iopOReserveStore
      * @param req
      * @return
      */
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(IopOReserveStore iopOReserveStore,HttpServletRequest req) {
		QueryWrapper<IopOReserveStore> queryWrapper = QueryGenerator.initQueryWrapper(iopOReserveStore, req.getParameterMap());
		List<IopOReserveStore> list = iopOReserveStoreService.list(queryWrapper);
		return Result.ok(list);
	}
	
	
	/**
	 *   添加
	 *
	 * @param iopOReserveStore
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody IopOReserveStore iopOReserveStore) {
		iopOReserveStoreService.addIopOReserveStore(iopOReserveStore);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param iopOReserveStore
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody IopOReserveStore iopOReserveStore) {
		iopOReserveStoreService.updateIopOReserveStore(iopOReserveStore);
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
		iopOReserveStoreService.deleteIopOReserveStore(id);
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
		this.iopOReserveStoreService.removeByIds(Arrays.asList(ids.split(",")));
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
		IopOReserveStore iopOReserveStore = iopOReserveStoreService.getById(id);
		if(iopOReserveStore==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(iopOReserveStore);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param iopOReserveStore
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, IopOReserveStore iopOReserveStore) {
		return super.exportXls(request, iopOReserveStore, IopOReserveStore.class, "库房维护");
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
		return super.importExcel(request, response, IopOReserveStore.class);
    }

}
