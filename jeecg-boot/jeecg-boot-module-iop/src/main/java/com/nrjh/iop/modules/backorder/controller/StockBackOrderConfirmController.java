package com.nrjh.iop.modules.backorder.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.backorder.entity.StockBackOrder;
import com.nrjh.iop.modules.backorder.entity.StockBackOrderLine;
import com.nrjh.iop.modules.backorder.service.IStockBackOrderLineService;
import com.nrjh.iop.modules.backorder.service.IStockBackOrderService;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderLineVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by shiyiteng on 2020/8/20.
 */
@Api(tags="退库单确认")
@RestController
@RequestMapping("/backorder/stockBackConfirmOrder")
@Slf4j
public class StockBackOrderConfirmController {

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
    @AutoLog(value = "退库单确认-分页列表查询")
    @ApiOperation(value="退库单确认-分页列表查询", notes="退库单确认-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(StockBackOrder stockBackOrder,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<StockBackOrder> queryWrapper = QueryGenerator.initQueryWrapper(stockBackOrder, req.getParameterMap());
        Page<StockBackOrder> page = new Page<StockBackOrder>(pageNo, pageSize);
        queryWrapper.ne("state","draft");
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

    @AutoLog(value = "退库单确认")
    @ApiOperation(value="退库单确认", notes="退库单确认")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody List<StockInOrderLineVo> stockInOrderLineList) {
        stockBackOrderService.saveConfirm(stockInOrderLineList);
        return Result.ok("添加成功！");
    }

    /**
     *   通过id作废
     *
     * @param id
     * @return
     */
    @AutoLog(value = "退库单-通过id作废")
    @ApiOperation(value="退库单-通过id作废", notes="退库单-通过id作废")
    @DeleteMapping(value = "/back")
    public Result<?> delete(@RequestParam(name="id",required=true) Integer id) {
        stockBackOrderService.updateBack(id);
        return Result.ok("作废成功!");
    }
}
