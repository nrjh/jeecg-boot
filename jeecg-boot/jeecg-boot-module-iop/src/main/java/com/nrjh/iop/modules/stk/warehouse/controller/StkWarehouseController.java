package com.nrjh.iop.modules.stk.warehouse.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nrjh.iop.modules.stk.location.entity.StkLocation;
import com.nrjh.iop.modules.stk.location.entity.StkLocationEnum;
import com.nrjh.iop.modules.stk.location.entity.StkUsageTypeEnum;
import com.nrjh.iop.modules.stk.location.service.IStkLocationService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.nrjh.iop.modules.stk.warehouse.entity.StkWarehouse;
import com.nrjh.iop.modules.stk.warehouse.service.IStkWarehouseService;

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
 * @Description: 库房
 * @Author: jeecg-boot
 * @Date: 2020-04-26
 * @Version: V1.0
 */
@Api(tags = "库房")
@RestController
@RequestMapping("/iop/stk/warehouse")
@Slf4j
public class StkWarehouseController extends JeecgController<StkWarehouse, IStkWarehouseService> {
    @Autowired
    private IStkWarehouseService stkWarehouseService;
    @Autowired
    private IStkLocationService stkLocationService;

    /**
     * 分页列表查询
     *
     * @param stkWarehouse
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "库房-分页列表查询")
    @ApiOperation(value = "库房-分页列表查询", notes = "库房-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(StkWarehouse stkWarehouse,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<StkWarehouse> queryWrapper = QueryGenerator.initQueryWrapper(stkWarehouse, req.getParameterMap());
        Page<StkWarehouse> page = new Page<StkWarehouse>(pageNo, pageSize);
        IPage<StkWarehouse> pageList = stkWarehouseService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param stkWarehouse
     * @return
     */
    @AutoLog(value = "库房-添加")
    @ApiOperation(value = "库房-添加", notes = "库房-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody StkWarehouse stkWarehouse) {
        stkWarehouseService.save(stkWarehouse);
        //  增加其下默认位置 并保存到相应位置
        addChildLocation(stkWarehouse);
        return Result.ok("添加成功！");
    }

    private void addChildLocation(StkWarehouse stkWarehouse) {
        // 视图位置
        StkLocation local1 = saveLocation("1", stkWarehouse.getName(), StkLocationEnum.LOCATION.getUsageType(), stkWarehouse.getActive());
        stkWarehouse.setViewLocationId(local1.getId());
        // 库存位置  stocklocation
        StkLocation local2 = saveLocation(local1.getId().toString(), StkLocationEnum.STOCK.getName(), StkLocationEnum.STOCK.getUsageType(), stkWarehouse.getActive());
        stkWarehouse.setStockLocationId(local2.getId());
        // 入库区 whInput
        StkLocation local3 = saveLocation(local1.getId().toString(), StkLocationEnum.STOCK_INPUT.getName(), StkLocationEnum.STOCK_INPUT.getUsageType(), stkWarehouse.getActive());
        stkWarehouse.setWhInputStockLotId(local3.getId());
        // 质检区 whQc
        StkLocation local4 = saveLocation(local1.getId().toString(), StkLocationEnum.STOCK_QC.getName(), StkLocationEnum.STOCK_QC.getUsageType(), stkWarehouse.getActive());
        stkWarehouse.setWhQcStockLocId(local4.getId());
        // 出库区  whOut
        StkLocation local5 = saveLocation(local1.getId().toString(), StkLocationEnum.STOCK_OUT.getName(), StkLocationEnum.STOCK_OUT.getUsageType(), stkWarehouse.getActive());
        stkWarehouse.setWhOutputStockLocId(local5.getId());
        // 打包qu  whPack
        StkLocation local6 = saveLocation(local1.getId().toString(), StkLocationEnum.STOCK_PACK.getName(), StkLocationEnum.STOCK_PACK.getUsageType(), stkWarehouse.getActive());
        stkWarehouse.setWhPackStockLocId(local6.getId());

        // 作业类型  默认
        stkWarehouse.setInTypeId(1) ; // 入库类型
        stkWarehouse.setIntTypeId(2); // 内部调拨类型
        stkWarehouse.setOutTypeId(3); // 出库类型
        stkWarehouseService.updateById(stkWarehouse);
    }

    private StkLocation saveLocation(String pid, String name, String usageType, int active) {
        StkLocation stkLocation = new StkLocation();
        stkLocation.setName(name);
        stkLocation.setUsageType(usageType);
        stkLocation.setPid(pid);
        stkLocation.setActive(active);
        stkLocationService.addStkLocation(stkLocation);
        stkLocationService.updateComplateName(stkLocation);
        return stkLocation;
    }

    /**
     * 编辑
     *
     * @param stkWarehouse
     * @return
     */
    @AutoLog(value = "库房-编辑")
    @ApiOperation(value = "库房-编辑", notes = "库房-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody StkWarehouse stkWarehouse) {
        stkWarehouseService.updateById(stkWarehouse);
        // 更新位置内容
        updateChildLocation(stkWarehouse);
        return Result.ok("编辑成功!");
    }

    private void updateChildLocation(StkWarehouse stkWarehouse) {
        // 视图位置
        updateLocation(stkWarehouse.getViewLocationId().toString(), stkWarehouse.getName(), stkWarehouse.getActive());
        // 库存位置  stocklocation
        updateLocation(stkWarehouse.getStockLocationId().toString(), null, stkWarehouse.getActive());
        // 入库区 whInput
        updateLocation(stkWarehouse.getWhInputStockLotId().toString(), null, stkWarehouse.getActive());
        // 质检区 whQc
        updateLocation(stkWarehouse.getWhQcStockLocId().toString(), null, stkWarehouse.getActive());
        // 出库区  whOut
        updateLocation(stkWarehouse.getWhOutputStockLocId().toString(), null, stkWarehouse.getActive());
        // 打包qu  whPack
        updateLocation(stkWarehouse.getWhPackStockLocId().toString(), null, stkWarehouse.getActive());
    }

    private StkLocation updateLocation(String id, String name, int active) {
        StkLocation stkLocation = stkLocationService.getById(id);
        if (name != null) {
            stkLocation.setName(name);
        }
        stkLocation.setActive(active);
        // 更新全称
        stkLocationService.updateComplateName(stkLocation);
        return stkLocation;
    }


    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "库房-通过id删除")
    @ApiOperation(value = "库房-通过id删除", notes = "库房-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        stkWarehouseService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "库房-批量删除")
    @ApiOperation(value = "库房-批量删除", notes = "库房-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.stkWarehouseService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "库房-通过id查询")
    @ApiOperation(value = "库房-通过id查询", notes = "库房-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        StkWarehouse stkWarehouse = stkWarehouseService.getById(id);
        if (stkWarehouse == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(stkWarehouse);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param stkWarehouse
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StkWarehouse stkWarehouse) {
        return super.exportXls(request, stkWarehouse, StkWarehouse.class, "库房");
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
        return super.importExcel(request, response, StkWarehouse.class);
    }

}
