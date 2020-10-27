package com.nrjh.iop.modules.stk.goodsplaceadjustment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.entity.StockMoveOrderLine;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.service.IStockMoveOrderLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
  * 货位调整-新增物料页面
 * @Description: stock_move_order_line
 * @Author: jeecg-boot
 * @Date:   2020-10-09
 * @Version: V1.0
 */
@Api(tags="stock_move_order_line")
@RestController
@RequestMapping("/stk/goodsplaceadjustment/stockMoveOrderLine")
@Slf4j
public class StockMoveOrderLineController extends JeecgController<StockMoveOrderLine, IStockMoveOrderLineService> {
	@Autowired
	private IStockMoveOrderLineService stockMoveOrderLineService;

	/**
	 * 分页列表查询
	 *
	 * @param stockMoveOrderLine
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "stock_move_order_line-分页列表查询")
	@ApiOperation(value="stock_move_order_line-分页列表查询", notes="stock_move_order_line-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StockMoveOrderLine stockMoveOrderLine,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StockMoveOrderLine> queryWrapper = QueryGenerator.initQueryWrapper(stockMoveOrderLine, req.getParameterMap());
		Page<StockMoveOrderLine> page = new Page<StockMoveOrderLine>(pageNo, pageSize);
		IPage<StockMoveOrderLine> pageList = stockMoveOrderLineService.page(page, queryWrapper);
		return Result.ok(pageList);
	}


}
