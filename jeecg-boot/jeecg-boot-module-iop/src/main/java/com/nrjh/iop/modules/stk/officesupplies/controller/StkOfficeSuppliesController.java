package com.nrjh.iop.modules.stk.officesupplies.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nrjh.iop.modules.stk.spareparts.entity.StkStock;
import com.nrjh.iop.modules.stk.spareparts.service.IStkStockService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.nrjh.iop.modules.stk.officesupplies.entity.StkSpareParts;
import com.nrjh.iop.modules.stk.officesupplies.service.IStkOfficeSuppliesService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 办公用品
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
@Api(tags="办公用品")
@RestController
@RequestMapping("/officesupplies/stkOfficeSupplies")
@Slf4j
public class StkOfficeSuppliesController extends JeecgController<StkSpareParts, IStkOfficeSuppliesService> {
	@Autowired
	private IStkOfficeSuppliesService stkOfficeSuppliesService;

	@Autowired
	private IStkStockService stkStockService;

	/**
	 * 分页列表查询
	 *
	 * @param stkSpareParts
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "办公用品-分页列表查询")
	@ApiOperation(value="办公用品-分页列表查询", notes="办公用品-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StkSpareParts stkSpareParts,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StkSpareParts> queryWrapper = QueryGenerator.initQueryWrapper(stkSpareParts, req.getParameterMap());
		Page<StkSpareParts> page = new Page<StkSpareParts>(pageNo, pageSize);
		IPage<StkSpareParts> pageList = stkOfficeSuppliesService.page(page, queryWrapper);
		for(StkSpareParts item : pageList.getRecords()){
			//查询时同步库存表信息
			item.setProductQty(stkStockService.getById(item.getStockId()).getProductQty());
			item.setStatusPro(stkStockService.getById(item.getStockId()).getStatus());
					//通过合同号查询该合同批次下的所有物品数量和价格
//					int productLotId =stkStockService.getById(item.getStockId()).getProductLotId();
//					//String productName = stkStockService.queryProductName(productLotId);
//					//没有合同号不做后续操作
//					if(productLotId == 0){
//						return;
//					}
//					List<StkStock> stkStockList = stkStockService.queryStockList(productLotId);

			//通过物料编号查询该合同批次下的所有物品数量和价格
			String productNo=stkStockService.getById(item.getStockId()).getProductNo();

			//没有物料编号不做后续操作
			if(productNo == null){
				continue;
			}
			List<StkStock> stkStockList = stkStockService.queryStockList(productNo);

					float sumPrice = 0;
					int sumQty = 0;
					for(int i = 0; i < stkStockList.size(); i++){
						sumPrice += stkStockList.get(i).getProductQty()*(stkStockList.get(i).getPrice());
						sumQty += stkStockList.get(i).getProductQty();
					}
					//当前均价
					item.setPriceAvg(new BigDecimal(sumPrice/sumQty).setScale(2,BigDecimal.ROUND_HALF_UP));
					//库存年限

		}
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param stkSpareParts
	 * @return
	 */
	@AutoLog(value = "定义办公用品-添加")
	@ApiOperation(value="定义办公用品-添加", notes="定义办公用品-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StkSpareParts stkSpareParts) {

		//添加到物品表和库存表
		stkOfficeSuppliesService.saveProdAndStock(stkSpareParts);
		//通过备品备件的属性关联库存表信息，拿到库存状态和库存数量
		stkSpareParts.setProductQty(stkStockService.getById(stkSpareParts.getStockId()).getInitQty());//新建备品备件初始数量就是库存数量

		stkSpareParts.setStatusPro(stkStockService.getById(stkSpareParts.getStockId()).getStatus());

		//添加到办公用品列表
		stkSpareParts.setFlag(2);
		stkOfficeSuppliesService.save(stkSpareParts);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param stkSpareParts
	 * @return
	 */
	@AutoLog(value = "办公用品-编辑")
	@ApiOperation(value="办公用品-编辑", notes="办公用品-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StkSpareParts stkSpareParts) {
		stkOfficeSuppliesService.updateProdAndStock(stkSpareParts);
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "办公用品-通过id删除")
	@ApiOperation(value="办公用品-通过id删除", notes="办公用品-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		stkOfficeSuppliesService.deleteProdAndStock(id);
		stkOfficeSuppliesService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "办公用品-批量删除")
	@ApiOperation(value="办公用品-批量删除", notes="办公用品-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.stkOfficeSuppliesService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "办公用品-通过id查询")
	@ApiOperation(value="办公用品-通过id查询", notes="办公用品-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StkSpareParts stkSpareParts = stkOfficeSuppliesService.getById(id);
		if(stkSpareParts==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(stkSpareParts);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param stkSpareParts
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StkSpareParts stkSpareParts) {
        return super.exportXls(request, stkSpareParts, StkSpareParts.class, "办公用品");
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
        return super.importExcel(request, response, StkSpareParts.class);
    }

}
