package com.nrjh.iop.modules.stk.in.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.nrjh.iop.modules.order.entity.StkSuggest;
import com.nrjh.iop.modules.order.service.IStkSuggestService;
import com.nrjh.iop.modules.order.vo.ProdAndStockVo;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderLineVo;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderVo;
import com.nrjh.iop.modules.stk.location.entity.StkLocation;
import com.nrjh.iop.modules.stk.location.service.IStkLocationService;
import com.nrjh.iop.modules.sys.dict.service.IIopSysDictService;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.stk.in.entity.StockInOrderLine;
import com.nrjh.iop.modules.stk.in.entity.StockInOrder;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderPage;
import com.nrjh.iop.modules.stk.in.service.IStockInOrderService;
import com.nrjh.iop.modules.stk.in.service.IStockInOrderLineService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 入库单
 * @Author: jeecg-boot
 * @Date: 2020-08-16
 * @Version: V1.0
 */
@Api(tags = "入库单")
@RestController
@RequestMapping("/stk/in/stockInOrder")
@Slf4j
public class StockInOrderController {
    @Autowired
    private IStockInOrderService stockInOrderService;
    @Autowired
    private IStockInOrderLineService stockInOrderLineService;

    @Autowired
    private IStkLocationService stkLocationService;
    @Autowired
    private IStkSuggestService stkSuggestService;
    @Autowired
    private ISysBaseAPI iSysBaseAPI;

    /**
     * 分页列表查询
     *
     * @param stockInOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "入库单-分页列表查询")
    @ApiOperation(value = "入库单-分页列表查询", notes = "入库单-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(StockInOrder stockInOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(name = "productName", required = false) String productName,
                                   @RequestParam(name = "defaultCode",required = false) String defaultCode,
                                   @RequestParam(name = "createTime_begin", required = false) String createTimeBegin,
                                   @RequestParam(name = "createTime_end", required = false) String createTimeEnd,
                                   HttpServletRequest req) {
        if (StrUtil.isAllEmpty(productName, defaultCode)) {
            QueryWrapper<StockInOrder> queryWrapper = QueryGenerator.initQueryWrapper(stockInOrder, req.getParameterMap());
            if (createTimeBegin != null && createTimeEnd != null) {
                queryWrapper.between("create_time", createTimeBegin, createTimeEnd);
            }
            List<StockInOrder> stockInOrderList = stockInOrderService.list(queryWrapper);
            List<StockInOrderVo> list = new ArrayList<>();
            if (CollectionUtils.isEmpty(stockInOrderList)){
                return Result.ok(null);
            }
            stockInOrderList.forEach(item -> {
                StockInOrderVo stockInOrderVo = new StockInOrderVo();
                BeanUtils.copyProperties(item, stockInOrderVo);
                stockInOrderVo.setStockInOrderLineList(stockInOrderLineService.selectByMainId(String.valueOf(item.getId())));
                list.add(stockInOrderVo);
            });
            Map<String,Object> map = new HashMap<>();
            map.put("records",list);
            map.put("total",CollectionUtils.isEmpty(list) ? 0 : list.size());
            return Result.ok(map);
        } else {
            //TODO 物资物料查询
            Map map = com.alibaba.fastjson.JSONObject.parseObject(com.alibaba.fastjson.JSONObject.toJSONString(stockInOrder), Map.class);
            map.put("productName", productName);
            map.put("defaultCode", defaultCode);
            map.put("createTimeBegin", createTimeBegin);
            map.put("createTimeEnd", createTimeEnd);
            List<StockInOrder> pageList = stockInOrderService.pageByInOrderAndDetail(map);
            Map<String,Object> datMap = new HashMap<>();
            datMap.put("records",pageList);
            datMap.put("total",CollectionUtils.isEmpty(pageList) ? 0 : pageList.size());
            return Result.ok(datMap);
        }
    }

    /**
     * 添加
     *
     * @param stockInOrderVo
     * @return
     */
    @AutoLog(value = "入库单-添加")
    @ApiOperation(value = "入库单-添加", notes = "入库单-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody StockInOrderVo stockInOrderVo) {
        String partnerName = stockInOrderService.queryDictTextByKey("vendorCode", String.valueOf(stockInOrderVo.getPartnerId()));
        stockInOrderVo.setPartnerName(partnerName);
        stockInOrderService.saveMain(stockInOrderVo);
        return Result.ok("添加成功！");
    }

    /**
     * 查询仓库列表
     *
     * @return
     */
    @AutoLog(value = "入库单-查询仓库列表")
    @ApiOperation(value = "入库单-查询仓库列表", notes = "入库单-查询仓库列表")
    @GetMapping(value = "/locList")
    public Result<?> locList() {
        List<StkLocation> list = stkLocationService.list();
        return Result.ok(list);
    }

    /**
     * 编辑
     *
     * @param stockInOrderVo
     * @return
     */
    @AutoLog(value = "入库单-编辑")
    @ApiOperation(value = "入库单-编辑", notes = "入库单-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody StockInOrderVo stockInOrderVo) {
        StockInOrder stockInOrderEntity = stockInOrderService.getById(stockInOrderVo.getId());
        if (stockInOrderEntity == null) {
            return Result.error("未找到对应数据");
        }
        String partnerName = stockInOrderService.queryDictTextByKey("vendorCode", String.valueOf(stockInOrderVo.getPartnerId()));
        stockInOrderVo.setPartnerName(partnerName);
        stockInOrderService.updateMain(stockInOrderVo);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "入库单-通过id删除")
    @ApiOperation(value = "入库单-通过id删除", notes = "入库单-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        stockInOrderService.delMain(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "入库单-批量删除")
    @ApiOperation(value = "入库单-批量删除", notes = "入库单-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.stockInOrderService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "入库单-通过id查询")
    @ApiOperation(value = "入库单-通过id查询", notes = "入库单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        StockInOrder stockInOrder = stockInOrderService.getById(id);
        if (stockInOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(stockInOrder);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "入库明细集合-通过id查询")
    @ApiOperation(value = "入库明细集合-通过id查询", notes = "入库明细-通过id查询")
    @GetMapping(value = "/queryStockInOrderLineByMainId")
    public Result<?> queryStockInOrderLineListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<StockInOrderLineVo> stockInOrderLineList = stockInOrderLineService.selectByMainId(id);
        return Result.ok(stockInOrderLineList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "采购入库入库物料信息回显-通过id查询")
    @ApiOperation(value = "采购入库入库物料信息回显-通过id查询", notes = "采购入库入库物料信息回显-通过id查询")
    @GetMapping(value = "/queryStockInOrderLineEcho")
    public Result<?> queryStockInOrderLineEcho(@RequestParam(name = "id", required = true) String id) {
        List<ProdAndStockVo> prodAndStockVoList = stockInOrderLineService.selectStockByMainId(id);
        return Result.ok(prodAndStockVoList);
    }

    @AutoLog(value = "入库审核-通过id查询审核内容")
    @ApiOperation(value = "入库审核-通过id查询审核内容", notes = "入库审核-通过id查询审核内容")
    @GetMapping(value = "/check")
    public Result<?> queryStkSuggestByMainId(@RequestParam(name = "id", required = true) String id) {
        QueryWrapper<StkSuggest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", id);
        List<StkSuggest> stkSuggests = stkSuggestService.list(queryWrapper);
        return Result.ok(stkSuggests);
    }

    @AutoLog(value = "入库审核-添加入库审核")
    @ApiOperation(value = "入库审核-添加入库审核", notes = "入库审核-添加入库审核")
    @PostMapping(value = "/check")
    public Result<?> checkStockInOrder(@RequestBody StkSuggest stkSuggest) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        stkSuggest.setCreateBy(sysUser.getRealname());
        stkSuggest.setCreateTime(new Date());
        stockInOrderService.saveSuggest(stkSuggest);
        return Result.ok("审批成功!!");
    }


    /**
     * 审批入库单
     *
     * @param stockInOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "审批入库单-分页列表查询")
    @ApiOperation(value = "审批入库单-分页列表查询", notes = "审批入库单-分页列表查询")
    @GetMapping(value = "/approveList")
    public Result<?> approveList(StockInOrder stockInOrder,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(name = "productName", required = false) String productName,
                                   @RequestParam(name = "defaultCode",required = false) String defaultCode,
                                   @RequestParam(name = "createTime_begin", required = false) String createTimeBegin,
                                   @RequestParam(name = "createTime_end", required = false) String createTimeEnd,
                                   HttpServletRequest req) {
        if (StrUtil.isAllEmpty(productName, defaultCode)) {
            QueryWrapper<StockInOrder> queryWrapper = QueryGenerator.initQueryWrapper(stockInOrder, req.getParameterMap());
            if (createTimeBegin != null && createTimeEnd != null) {
                queryWrapper.between("create_time", createTimeBegin, createTimeEnd);
            }
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            // 获取当前用户的所有角色id
            List<String> roleIds=iSysBaseAPI.getRoleIdsByUsername(sysUser.getUsername());
            queryWrapper.in("next_role_id",roleIds);
            queryWrapper.in("businessSequence",1,2);
            List<StockInOrder> stockInOrderList = stockInOrderService.list(queryWrapper);
            List<StockInOrderVo> list = new ArrayList<>();
            if (CollectionUtils.isEmpty(stockInOrderList)){
                return Result.ok(null);
            }
            stockInOrderList.forEach(item -> {
                StockInOrderVo stockInOrderVo = new StockInOrderVo();
                BeanUtils.copyProperties(item, stockInOrderVo);
                stockInOrderVo.setStockInOrderLineList(stockInOrderLineService.selectByMainId(String.valueOf(item.getId())));
                list.add(stockInOrderVo);
            });
            Map<String,Object> map = new HashMap<>();
            map.put("records",list);
            map.put("total",CollectionUtils.isEmpty(list) ? 0 : list.size());
            return Result.ok(map);
        } else {
            //TODO 物资物料查询
            Map map = com.alibaba.fastjson.JSONObject.parseObject(com.alibaba.fastjson.JSONObject.toJSONString(stockInOrder), Map.class);
            map.put("productName", productName);
            map.put("defaultCode", defaultCode);
            map.put("createTimeBegin", createTimeBegin);
            map.put("createTimeEnd", createTimeEnd);
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            // 获取当前用户的所有角色id
            List<String> roleIds=iSysBaseAPI.getRoleIdsByUsername(sysUser.getUsername());
            map.put("roleIds",roleIds);
            map.put("businessSequence","true");
            List<StockInOrder> pageList = stockInOrderService.pageByInOrderAndDetail(map);
            Map<String,Object> datMap = new HashMap<>();
            datMap.put("records",pageList);
            datMap.put("total",CollectionUtils.isEmpty(pageList) ? 0 : pageList.size());
            return Result.ok(datMap);
        }
    }

}
