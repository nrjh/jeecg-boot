package com.nrjh.iop.modules.backorder.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.backorder.entity.StockBackOrder;
import com.nrjh.iop.modules.backorder.entity.StockBackOrderLine;
import com.nrjh.iop.modules.backorder.service.IStockBackOrderLineService;
import com.nrjh.iop.modules.backorder.service.IStockBackOrderService;
import com.nrjh.iop.modules.backorder.vo.StockBackOrderPage;
import com.nrjh.iop.modules.order.vo.ProdAndStockVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

 /**
 * @Description: 退库单
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
@Api(tags="退库单")
@RestController
@RequestMapping("/backorder/stockBackOrder")
@Slf4j
public class StockBackOrderController {
	@Autowired
	private IStockBackOrderService stockBackOrderService;
	@Autowired
	private IStockBackOrderLineService stockBackOrderLineService;

	/**
	 * 分页列表查询
	 *
	 * @param stockBackOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "退库单-分页列表查询")
	@ApiOperation(value="退库单-分页列表查询", notes="退库单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StockBackOrder stockBackOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StockBackOrder> queryWrapper = QueryGenerator.initQueryWrapper(stockBackOrder, req.getParameterMap());
		Page<StockBackOrder> page = new Page<StockBackOrder>(pageNo, pageSize);
		queryWrapper.eq("state","draft");
		IPage<StockBackOrder> pageList = stockBackOrderService.page(page, queryWrapper);
		pageList.getRecords().forEach(item -> {
			StringBuffer str=new StringBuffer();
			List<StockBackOrderLine> stockBackOrderLines=  stockBackOrderLineService.selectByMainId(item.getId());
			for (StockBackOrderLine line : stockBackOrderLines){
				str.append(line.getProductName()+"_"+line.getAttributeCategoryId()+ "_" +line.getProductActualQty() );
				str.append(",");
			}
			item.setProdLines(str.toString());
		});
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param stockBackOrderPage
	 * @return
	 */
	@AutoLog(value = "退库单-添加")
	@ApiOperation(value="退库单-添加", notes="退库单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StockBackOrderPage stockBackOrderPage) {
		StockBackOrder stockBackOrder = new StockBackOrder();
		BeanUtils.copyProperties(stockBackOrderPage, stockBackOrder);
		stockBackOrderService.saveMain(stockBackOrder, stockBackOrderPage.getProdAndStockVoList());
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param stockBackOrderPage
	 * @return
	 */
	@AutoLog(value = "退库单-编辑")
	@ApiOperation(value="退库单-编辑", notes="退库单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StockBackOrderPage stockBackOrderPage) {
		StockBackOrder stockBackOrder = new StockBackOrder();
		BeanUtils.copyProperties(stockBackOrderPage, stockBackOrder);
		StockBackOrder stockBackOrderEntity = stockBackOrderService.getById(stockBackOrder.getId());
		if(stockBackOrderEntity==null) {
			return Result.error("未找到对应数据");
		}
		stockBackOrderService.updateMain(stockBackOrder, stockBackOrderPage.getProdAndStockVoList());
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "退库单-通过id删除")
	@ApiOperation(value="退库单-通过id删除", notes="退库单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) Integer id) {
		stockBackOrderService.delMain(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "退库单-批量删除")
	@ApiOperation(value="退库单-批量删除", notes="退库单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.stockBackOrderService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "退库单-通过id查询")
	@ApiOperation(value="退库单-通过id查询", notes="退库单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StockBackOrder stockBackOrder = stockBackOrderService.getById(id);
		if(stockBackOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(stockBackOrder);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "退回入库明细集合-通过id查询")
	@ApiOperation(value="退回入库明细集合-通过id查询", notes="退回入库明细-通过id查询")
	@GetMapping(value = "/queryStockBackOrderLineByMainId")
	public Result<?> queryStockBackOrderLineListByMainId(@RequestParam(name="id",required=true) Integer id) {
		List<StockBackOrderLine> stockBackOrderLineList = stockBackOrderLineService.selectByMainId(id);
		List<ProdAndStockVo> result=new ArrayList<>();
		if(!CollectionUtils.isEmpty(stockBackOrderLineList)){
			for(StockBackOrderLine line:stockBackOrderLineList){
				ProdAndStockVo prodAndStockVo=new ProdAndStockVo();
				prodAndStockVo.setId(line.getId().toString());
				prodAndStockVo.setOrderId(line.getOrderId());
				prodAndStockVo.setProductId(line.getProductId());
				prodAndStockVo.setName(line.getProductName());
				prodAndStockVo.setProductNo(line.getDefaultCode());
				prodAndStockVo.setAttributeCategoryId(line.getAttributeCategoryId());
				prodAndStockVo.setReturnCount(line.getReturnCount());
				if(line.getCategoryType()!=null){
					prodAndStockVo.setCategoryType(line.getCategoryType());
				}
				prodAndStockVo.setProductUomId(line.getProductUomId());
				if(line.getProductActualQty()!=null){
					prodAndStockVo.setProductUomQty(new BigDecimal(line.getProductActualQty()));
				}
				prodAndStockVo.setProductActualQty(line.getProductActualQty());
				result.add(prodAndStockVo);
			}

		}
		return Result.ok(result);
	}




}
