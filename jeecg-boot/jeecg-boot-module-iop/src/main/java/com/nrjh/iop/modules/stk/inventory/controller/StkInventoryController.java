package com.nrjh.iop.modules.stk.inventory.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.stk.inventory.entity.InventoryProdVo;
import com.nrjh.iop.modules.stk.inventory.entity.StkInventory;
import com.nrjh.iop.modules.stk.inventory.entity.StkInventoryRel;
import com.nrjh.iop.modules.stk.inventory.model.StkInventoryModel;
import com.nrjh.iop.modules.stk.inventory.service.IStkInventoryRelService;
import com.nrjh.iop.modules.stk.inventory.service.IStkInventoryService;
import com.nrjh.iop.modules.stk.location.entity.StkLocation;
import com.nrjh.iop.modules.stk.location.service.IStkLocationService;
import com.nrjh.iop.modules.stk.warehouse.entity.StkWarehouse;
import com.nrjh.iop.modules.stk.warehouse.service.IStkWarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: stk_inventory
 * @Author: jeecg-boot
 * @Date:   2020-08-04
 * @Version: V1.0
 */
@Api(tags="stk_inventory")
@RestController
@RequestMapping("/StkInventory/stkInventory")
@Slf4j
public class StkInventoryController extends JeecgController<StkInventory, IStkInventoryService> {
	@Autowired
	private IStkInventoryService stkInventoryService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;

	@Autowired
	private IStkInventoryRelService iStkInventoryRelService;

	@Autowired
	private IStkLocationService stkLocationService;

	@Autowired
	private IStkWarehouseService stkWarehouseService;



	/**
	 * 分页列表查询
	 *
	 * @param stkInventory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "stk_inventory-分页列表查询")
	@ApiOperation(value="stk_inventory-分页列表查询", notes="stk_inventory-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StkInventory stkInventory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StkInventory> queryWrapper = QueryGenerator.initQueryWrapper(stkInventory, req.getParameterMap());
		Page<StkInventory> page = new Page<StkInventory>(pageNo, pageSize);
		IPage<StkInventory> pageList = stkInventoryService.page(page, queryWrapper);
		pageList.getRecords().forEach(item -> {
			//批量查询用户名称
			item.setExecBy(sysBaseAPI.getUserById(item.getExecBy()).getUsername());
			Map<String, Object> var1=new HashMap<String, Object>();
			var1.put("iinventory_id",item.getId());
			Collection<StkInventoryRel> list= iStkInventoryRelService.listByMap(var1);
			StringBuffer sb=new StringBuffer();
			list.forEach(itemv -> {
				//盘点范围
				StkLocation stkLocation = stkLocationService.getById(itemv.getLocationId());
				sb.append(stkLocation.getName());
				sb.append("/");
				item.setLocationName(String.valueOf(sb));

				Map<String, Object> var2=new HashMap<String, Object>();
				var2.put("stock_location_id",item.getId());
				//盘点库房
				Collection<StkWarehouse> list2= stkWarehouseService.listByMap(var2);
				StringBuffer sbWarehouse=new StringBuffer();
				list2.forEach(stkWarehouse -> {
					sbWarehouse.append(stkWarehouse.getName());
					sbWarehouse.append("/");
				});
				item.setSbWarehouse(String.valueOf(sbWarehouse));

			});
			//盘点结果
			item.setInventoryResult(stkInventoryService.getReultInventory(item));
		});

		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "stk_inventory-添加")
	@ApiOperation(value="stk_inventory-添加", notes="stk_inventory-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StkInventoryModel stkInventoryModel) throws BusinessException {
//		stkInventoryService.save(stkInventory);
		stkInventoryService.saveInventoryDeal(stkInventoryModel);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param stkInventory
	 * @return
	 */
	@AutoLog(value = "stk_inventory-编辑")
	@ApiOperation(value="stk_inventory-编辑", notes="stk_inventory-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StkInventory stkInventory) {
		stkInventoryService.updateById(stkInventory);
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "stk_inventory-通过id删除")
	@ApiOperation(value="stk_inventory-通过id删除", notes="stk_inventory-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		stkInventoryService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "stk_inventory-批量删除")
	@ApiOperation(value="stk_inventory-批量删除", notes="stk_inventory-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.stkInventoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "stk_inventory-通过id查询")
	@ApiOperation(value="stk_inventory-通过id查询", notes="stk_inventory-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StkInventory stkInventory = stkInventoryService.getById(id);
		if(stkInventory==null) {
			return Result.error("未找到对应数据");
		}
		stkInventory.setExecBy(sysBaseAPI.getUserById(stkInventory.getExecBy()).getUsername());
		return Result.ok(stkInventory);
	}

//    /**
//    * 导出excel
//    *
//    * @param request
//    * @param stkInventory
//    */
//    @RequestMapping(value = "/exportXls")
//    public ModelAndView exportXls(HttpServletRequest request, StkInventory stkInventory) {
//        return super.exportXls(request, stkInventory, StkInventory.class, "stk_inventory");
//    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, StkInventory.class);
    }


	@RequestMapping(value = "/send")
	public Result<?> updateInventory(@RequestParam(name="id",required=true) String id) {
		this.stkInventoryService.updateInventory(id,"send");
		return Result.ok("更新成功!");
	}

	@RequestMapping(value = "/cancel")
	public Result<?> cancelInventory(@RequestParam(name="id",required=true) String id) {
		this.stkInventoryService.updateInventory(id,"cancel");
		return Result.ok("更新成功!");
	}


	@RequestMapping(value = "/getInventoryProd")
	public Result<?> getInventoryProd(@RequestParam(name="inventoryId",required=true) String inventoryId) {
		Result<List<InventoryProdVo>> result=new Result<>();
		result.setCode(0);
		List<InventoryProdVo> list=stkInventoryService.queryProdList(inventoryId);
		result.setResult(list);
		return result;
	}


	/**
	 *  盘点结果
	 *
	 * @return
	 */
	@PutMapping(value = "/inventoryResult")
	public Result<?> inventoryResult(@RequestBody List<InventoryProdVo> inventoryProdVo) {
		stkInventoryService.dealInventoryResult(inventoryProdVo);
		return Result.ok("编辑成功!");
	}


	/**
	 * 导出excel
	 *
	 * @param request
	 * @param
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, @RequestParam(name="inventoryId",required=true) String  inventoryId) {
		List<InventoryProdVo> list=stkInventoryService.queryProdList(inventoryId);
		// Step.3 AutoPoi 导出Excel
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "stk_inventory"); //此处设置的filename无效 ,前端会重更新设置一下
		mv.addObject(NormalExcelConstants.CLASS, InventoryProdVo.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("stk_inventory" + "报表", "导出人:" + sysUser.getRealname(), "stk_inventory"));
		mv.addObject(NormalExcelConstants.DATA_LIST, list);
		return mv;
	}


	/**
	 * 盘点任务单导出
	 * @param request
	 * @param inventoryId
	 * @return
	 */
	@RequestMapping(value = "/export")
	public Result<?> export(HttpServletRequest request, @RequestParam(name="inventoryId",required=true) String  inventoryId) {
		List<InventoryProdVo> list=stkInventoryService.queryProdList(inventoryId);
		Result<List<InventoryProdVo>> result=new Result<>();
		result.setCode(0);
		result.setResult(list);
		return result;
	}
	@RequestMapping(value = "/getTotalByInventoryId")
	public Result<?> getTotalByInventoryId(@RequestParam(name="inventoryId",required=true) String inventoryId) {
		Result<List<InventoryProdVo>> result=new Result<>();
		result.setCode(0);
		List<InventoryProdVo> list=stkInventoryService.queryProdTotal(inventoryId);
		result.setResult(list);
		return result;
	}




}
