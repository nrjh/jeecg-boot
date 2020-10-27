package com.nrjh.iop.modules.outOrder.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.order.entity.StkSuggest;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrder;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrderLine;
import com.nrjh.iop.modules.outOrder.service.IStockOutOrderLineService;
import com.nrjh.iop.modules.outOrder.service.IStockOutOrderService;
import com.nrjh.iop.modules.outOrder.service.StockOutOrderApprovalService;
import com.nrjh.iop.modules.outOrder.vo.StockOutOrderPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @Description: 领料出库单审批
* @Author: lei li
* @Date:   2020-08-22
* @Version: V1.0
*/
@Api(tags="领料出库单审批")
@RestController
@RequestMapping("/outOrder/stockOutOrderApproval")
@Slf4j
public class StockOutOrderApprovalController {
   @Autowired
   private IStockOutOrderService stockOutOrderService;
   @Autowired
   private IStockOutOrderLineService stockOutOrderLineService;
   @Autowired
   private StockOutOrderApprovalService stockOutOrderApprovalService;
   @Autowired
   private ISysBaseAPI iSysBaseAPI;

    /**
     *  审批完成
     *
     * @param stkSuggest
     * @return
     */
    @AutoLog(value = "领料出库单审批-审批完成")
    @ApiOperation(value="领料出库单审批-审批完成", notes="领料出库单审批-审批完成")
    @PostMapping(value = "/completeStockApproval")
    public Result<?> completeStockApproval(@RequestBody StkSuggest stkSuggest) {
        stockOutOrderApprovalService.completeStockApproval(stkSuggest);
        return Result.ok("已审批!");
    }

   /**
    * 分页列表查询
    *
    * @param stockOutOrder
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "领料出库单-分页列表查询")
   @ApiOperation(value="领料出库单-分页列表查询", notes="领料出库单-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(StockOutOrder stockOutOrder,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
//       QueryWrapper<StockOutOrder> queryWrapper = QueryGenerator.initQueryWrapper(stockOutOrder, req.getParameterMap());
       QueryWrapper<StockOutOrder> queryWrapper = new QueryWrapper<>();
       Page<StockOutOrder> page = new Page<StockOutOrder>(pageNo, pageSize);
       if(stockOutOrder.getOrderOutId() !=null && stockOutOrder.getOrderOutId() != ""){
           queryWrapper.eq("order_out_id",stockOutOrder.getOrderOutId());
       }
       if(stockOutOrder.getCreateOutName() !=null && stockOutOrder.getCreateOutName() != ""){
           queryWrapper.eq("create_out_name",stockOutOrder.getCreateOutName());
       }
       if(stockOutOrder.getState() !=null && stockOutOrder.getState() != ""){
           queryWrapper.eq("state",stockOutOrder.getState());
       }
       if(stockOutOrder.getLineId() !=null && stockOutOrder.getLineId() != ""){
           queryWrapper.eq("line_id",stockOutOrder.getLineId());
       }
       if(stockOutOrder.getTeam() !=null && stockOutOrder.getTeam() != ""){
           queryWrapper.eq("team",stockOutOrder.getTeam());
       }
       if(stockOutOrder.getStartDate() !=null && stockOutOrder.getStartDate() != ""){
           queryWrapper.gt("create_time",stockOutOrder.getStartDate());
       }
       if(stockOutOrder.getEndDate() !=null && stockOutOrder.getEndDate() != ""){
           queryWrapper.lt("create_time",stockOutOrder.getEndDate());
       }
       queryWrapper.eq("state","submit");
       LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       // 获取当前用户的所有角色id
       List<String> roleIds=iSysBaseAPI.getRoleIdsByUsername(sysUser.getUsername());

       queryWrapper.in("NEXT_ROLE_ID",roleIds);

       queryWrapper.in("BUSINESS_SEQUENCE","1","2");
       queryWrapper.orderByDesc("create_time");
       IPage<StockOutOrder> pageList = stockOutOrderService.page(page, queryWrapper);
       pageList.getRecords().forEach(item ->{
           item.setProdDetails(stockOutOrderService.getProdDetails(String.valueOf(item.getId())));
       });
       return Result.ok(pageList);
   }

   /**
    *   添加
    *
    * @param stockOutOrderPage
    * @return
    */
   @AutoLog(value = "领料出库单-添加")
   @ApiOperation(value="领料出库单-添加", notes="领料出库单-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody StockOutOrderPage stockOutOrderPage) {
       StockOutOrder stockOutOrder = new StockOutOrder();
       BeanUtils.copyProperties(stockOutOrderPage, stockOutOrder);
       stockOutOrderService.saveMain(stockOutOrder, stockOutOrderPage.getStockOutOrderLineList());
       return Result.ok("添加成功！");
   }

   /**
    *  编辑
    *
    * @param stockOutOrderPage
    * @return
    */
   @AutoLog(value = "领料出库单-编辑")
   @ApiOperation(value="领料出库单-编辑", notes="领料出库单-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody StockOutOrderPage stockOutOrderPage) {
       StockOutOrder stockOutOrder = new StockOutOrder();
       BeanUtils.copyProperties(stockOutOrderPage, stockOutOrder);
       StockOutOrder stockOutOrderEntity = stockOutOrderService.getById(stockOutOrder.getId());
       if(stockOutOrderEntity==null) {
           return Result.error("未找到对应数据");
       }
       stockOutOrderService.updateMain(stockOutOrder, stockOutOrderPage.getStockOutOrderLineList());
       return Result.ok("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "领料出库单-通过id删除")
   @ApiOperation(value="领料出库单-通过id删除", notes="领料出库单-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) Integer id) {
       stockOutOrderService.delMain(id);
       return Result.ok("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "领料出库单-批量删除")
   @ApiOperation(value="领料出库单-批量删除", notes="领料出库单-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) Integer[] ids) {
//		this.stockOutOrderService.delBatchMain(Arrays.asList(ids.split(",")));
       this.stockOutOrderService.delBatchMain(ids);
       return Result.ok("批量删除成功！");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "领料出库单-通过id查询")
   @ApiOperation(value="领料出库单-通过id查询", notes="领料出库单-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       StockOutOrder stockOutOrder = stockOutOrderService.getById(id);
       if(stockOutOrder==null) {
           return Result.error("未找到对应数据");
       }
       return Result.ok(stockOutOrder);

   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "出库明细表集合-通过id查询")
   @ApiOperation(value="出库明细表集合-通过id查询", notes="出库明细表-通过id查询")
   @GetMapping(value = "/queryStockOutOrderLineByMainId")
   public Result<?> queryStockOutOrderLineListByMainId(@RequestParam(name="id",required=true) Integer id) {
       List<StockOutOrderLine> stockOutOrderLineList = stockOutOrderLineService.selectByMainId(id);
       return Result.ok(stockOutOrderLineList);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param stockOutOrder
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, StockOutOrder stockOutOrder) {
     // Step.1 组装查询条件查询数据
     QueryWrapper<StockOutOrder> queryWrapper = QueryGenerator.initQueryWrapper(stockOutOrder, request.getParameterMap());
     LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

     //Step.2 获取导出数据
     List<StockOutOrder> queryList = stockOutOrderService.list(queryWrapper);
     // 过滤选中数据
     String selections = request.getParameter("selections");
     List<StockOutOrder> stockOutOrderList = new ArrayList<StockOutOrder>();
     if(oConvertUtils.isEmpty(selections)) {
         stockOutOrderList = queryList;
     }else {
         List<String> selectionList = Arrays.asList(selections.split(","));
         stockOutOrderList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
     }

     // Step.3 组装pageList
     List<StockOutOrderPage> pageList = new ArrayList<StockOutOrderPage>();
     for (StockOutOrder main : stockOutOrderList) {
         StockOutOrderPage vo = new StockOutOrderPage();
         BeanUtils.copyProperties(main, vo);
         List<StockOutOrderLine> stockOutOrderLineList = stockOutOrderLineService.selectByMainId(main.getId());
         vo.setStockOutOrderLineList(stockOutOrderLineList);
         pageList.add(vo);
     }

     // Step.4 AutoPoi 导出Excel
     ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
     mv.addObject(NormalExcelConstants.FILE_NAME, "领料出库单列表");
     mv.addObject(NormalExcelConstants.CLASS, StockOutOrderPage.class);
     mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("领料出库单数据", "导出人:"+sysUser.getRealname(), "领料出库单"));
     mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
     return mv;
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
     MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
     Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
     for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
         MultipartFile file = entity.getValue();// 获取上传文件对象
         ImportParams params = new ImportParams();
         params.setTitleRows(2);
         params.setHeadRows(1);
         params.setNeedSave(true);
         try {
             List<StockOutOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), StockOutOrderPage.class, params);
             for (StockOutOrderPage page : list) {
                 StockOutOrder po = new StockOutOrder();
                 BeanUtils.copyProperties(page, po);
                 stockOutOrderService.saveMain(po, page.getStockOutOrderLineList());
             }
             return Result.ok("文件导入成功！数据行数:" + list.size());
         } catch (Exception e) {
             log.error(e.getMessage(),e);
             return Result.error("文件导入失败:"+e.getMessage());
         } finally {
             try {
                 file.getInputStream().close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }
     return Result.ok("文件导入失败！");
   }

}
