package com.nrjh.iop.modules.stk.goodsplaceadjustment.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.dto.GeneralGoodsPlaceAdjustmentDTO;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.dto.GoodsPlaceAdjustmentDTO;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.entity.StockMoveOrder;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.entity.StockMoveOrderLine;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.service.IStockMoveOrderLineService;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.service.IStockMoveOrderService;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.vo.GoodsPlaceAdjustmentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: stock_move_order
 * @Author: jeecg-boot
 * @Date: 2020-10-09
 * @Version: V1.0
 */
@Api(tags = "货位调整")
@RestController
@RequestMapping("/stk/goodsplaceadjustment")
@Slf4j
public class GoodsPlaceAdjustmentController  {
    @Autowired
    private IStockMoveOrderService stockMoveOrderService;

	@Autowired
	private IStockMoveOrderLineService stockMoveOrderLineService;
    /**
     * 分页列表查询
     *
     * @param dto
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "调整货位-分页列表查询")
    @ApiOperation(value = "调整货位-分页列表查询", notes = "调整货位-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GoodsPlaceAdjustmentDTO dto,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<StockMoveOrder> stockMoveOrderQueryWrapper = new QueryWrapper<>();
        if (dto != null) {
			// 调整人
			if(dto.getCreateOutName() != null && !dto.getCreateOutName().equals("")){
				stockMoveOrderQueryWrapper.eq("create_out_name",dto.getCreateOutName());
			}
			if(dto.getStartAdjustTime() != null && dto.getEndAdjustTime()!=null){
				stockMoveOrderQueryWrapper.between("create_out_time",dto.getStartAdjustTime(),dto.getEndAdjustTime());
			}
			// 状态
			if(dto.getState() != null && !dto.getState().equals("")){
				stockMoveOrderQueryWrapper.eq("state",dto.getState());
			}
        }
        Page<StockMoveOrder> page = new Page<StockMoveOrder>(pageNo, pageSize);
        IPage<StockMoveOrder> pageList = stockMoveOrderService.page(page, stockMoveOrderQueryWrapper);
		List<StockMoveOrder> records = pageList.getRecords();
        List<GoodsPlaceAdjustmentVO> goodsPlaceAdjustmentVOList = records.stream().map(s -> {
            QueryWrapper<StockMoveOrderLine> stockMoveOrderLineQueryWrapper = new QueryWrapper<>();
            if (dto != null) {
                // 货位调整单号
                if (dto.getOrderMoveId() != null) {
                    stockMoveOrderLineQueryWrapper.eq("order_id", dto.getOrderMoveId());
                }
                // 物料名称
                if (dto.getProductName() != null && !dto.getProductName().equals("")) {
                    stockMoveOrderLineQueryWrapper.eq("product_name", dto.getProductName());
                }
                // 物料编号
                if (dto.getProductId() != null) {
                    stockMoveOrderLineQueryWrapper.eq("product_id", dto.getProductId());
                }
            }
            // 根据《库存移库单》表中的移库单号来获取《移库信息明细》信息
            if ((dto == null) || (dto!= null && dto.getOrderMoveId() == null )) {
                stockMoveOrderLineQueryWrapper.eq(true,"order_id",s.getOrderMoveId());
            }
            // 满足条件查询的《移库信息明细》条数
            int count = stockMoveOrderLineService.count(stockMoveOrderLineQueryWrapper);
            GoodsPlaceAdjustmentVO goodsPlaceAdjustmentVO = null;
            if(count > 0){
                goodsPlaceAdjustmentVO = new GoodsPlaceAdjustmentVO();
                BeanUtil.copyProperties(s, goodsPlaceAdjustmentVO);
            }
            return goodsPlaceAdjustmentVO;
        }).filter(g -> g!=null).collect(Collectors.toList());
        IPage<GoodsPlaceAdjustmentVO> resultPageList = new Page<>();
        resultPageList.setRecords(goodsPlaceAdjustmentVOList);
        resultPageList.setTotal(goodsPlaceAdjustmentVOList.size());
        return Result.ok(resultPageList);
    }

    /**
     * 根据货位调整单号添加
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "根据货位调整单号添加")
    @ApiOperation(value = "根据货位调整单号添加", notes = "根据货位调整单号添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GeneralGoodsPlaceAdjustmentDTO dto) {
        stockMoveOrderService.saveWithStockMoveOrderLine(dto);
        return Result.ok("添加成功！");
    }


    /**
     * 根据货位调整单号修改
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "根据货位调整单号修改")
    @ApiOperation(value = "根据货位调整单号修改", notes = "根据货位调整单号修改")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GeneralGoodsPlaceAdjustmentDTO dto) {
        stockMoveOrderService.updateWithStockMoveOrderLine(dto);
        return Result.ok("修改成功！");
    }


    /**
     * 根据货位调整单号删除
     *
     * @param orderMoveId
     * @return
     */
    @AutoLog(value = "根据货位调整单号删除")
    @ApiOperation(value = "根据货位调整单号删除", notes = "根据货位调整单号删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "orderMoveId", required = true) String orderMoveId) {
        stockMoveOrderService.deleteWithStockMoveOrderLine(orderMoveId);
        return Result.ok("删除成功!");
    }

    /**
     * 根据货位调整单号批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "根据货位调整单号批量删除")
    @ApiOperation(value = "根据货位调整单号批量删除", notes = "根据货位调整单号批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> orderMoveIdList = Arrays.asList(ids.split(","));
        this.stockMoveOrderService.deleteBatchWithStockMoveOrderLine(orderMoveIdList);
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过移库单号查询
     *
     * @param orderMoveId
     * @return
     */
    @AutoLog(value = "通过移库单号查询")
    @ApiOperation(value = "通过移库单号查询", notes = "通过移库单号查询")
    @GetMapping(value = "/queryByOrderMoveId")
    public Result<?> queryById(@RequestParam(name = "orderMoveId", required = true) String orderMoveId) {
        GeneralGoodsPlaceAdjustmentDTO generalGoodsPlaceAdjustmentDTO = new GeneralGoodsPlaceAdjustmentDTO();
        // 获取《库存移库单》信息
        QueryWrapper<StockMoveOrder> stockMoveOrderQueryWrapper = new QueryWrapper<>();
        stockMoveOrderQueryWrapper.eq("order_move_id",orderMoveId);
        StockMoveOrder stockMoveOrder = stockMoveOrderService.getOne(stockMoveOrderQueryWrapper);
        // 把StockMoveOrder的信息存入到GeneralGoodsPlaceAdjustmentDTO中
        BeanUtils.copyProperties(stockMoveOrder,generalGoodsPlaceAdjustmentDTO);
        // 获取《移库信息明细》信息
        QueryWrapper<StockMoveOrderLine> stockMoveOrderLineQueryWrapper = new QueryWrapper<>();
        stockMoveOrderLineQueryWrapper.eq("order_id",orderMoveId);
        List<StockMoveOrderLine> stockMoveOrderLineList = stockMoveOrderLineService.list(stockMoveOrderLineQueryWrapper);
        // 把StockMoveOrderLine的信息存入到GeneralGoodsPlaceAdjustmentDTO中
        generalGoodsPlaceAdjustmentDTO.setStockMoveOrderLineList(stockMoveOrderLineList);
        return Result.ok(generalGoodsPlaceAdjustmentDTO);
    }

}
