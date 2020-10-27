package com.nrjh.iop.modules.app.orderWorkManagement.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultReasonInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultRepairInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultTaskInfo;
import com.nrjh.iop.modules.alarm.alarmtype.service.IEquipldTreeService;
import com.nrjh.iop.modules.alarm.alarmtype.service.IFaultReasonInfoService;
import com.nrjh.iop.modules.alarm.alarmtype.vo.EquipIdModel;
import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.bind.service.IFaultPrdProductInfoService;
import com.nrjh.iop.modules.alarm.bind.vo.FaultPrdProductInfoVO;
import com.nrjh.iop.modules.alarm.record.entity.VAlarmInfo;
import com.nrjh.iop.modules.alarm.record.service.IVAlarmInfoService;
import com.nrjh.iop.modules.app.orderWorkManagement.service.CommonOrderWorkManagementService;
import com.nrjh.iop.modules.app.orderWorkManagement.service.OrderWorkManagementService;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrder;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrderLine;
import com.nrjh.iop.modules.outOrder.service.IStockOutOrderLineService;
import com.nrjh.iop.modules.outOrder.service.IStockOutOrderService;
import com.nrjh.iop.modules.outOrder.vo.StockOutOrderPage;
import com.nrjh.iop.modules.stk.stkscrap.entity.ScrapPro;
import com.nrjh.iop.modules.stk.stkscrap.service.IScrapProService;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import com.nrjh.iop.modules.workOrder.entity.SysUser;
import com.nrjh.iop.modules.workOrder.service.CommonOrdWorkOrderInfoService;
import com.nrjh.iop.modules.workOrder.service.IOrdProcessInfoService;
import com.nrjh.iop.modules.workOrder.service.IOrdWorkOrderInfoService;
import com.nrjh.iop.modules.workOrder.vo.OrdProcessInfoEnclosureInfoVo;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @description:
 * @author: lei-li
 * @create: 2020-10-16 17:16
 */
@Api(tags = "app工单管理")
@RestController
@RequestMapping("/app/orderWorkManagement")
public class OrderWorkManagementController {

    @Autowired
    private IOrdWorkOrderInfoService ordWorkOrderInfoService;
    @Autowired
    private OrderWorkManagementService orderWorkManagementService;
    @Autowired
    private IOrdWorkOrderInfoService orderInfoService;
    @Autowired
    private IFaultReasonInfoService faultReasonInfoService;
    @Autowired
    private IOrdProcessInfoService ordProcessInfoService;
    @Autowired
    private IScrapProService scrapProService;
    @Autowired
    private IEquipldTreeService iEquipldTreeService;
    @Autowired
    private IStockOutOrderService stockOutOrderService;
    @Autowired
    private ISysBaseAPI iSysBaseAPI;
    @Autowired
    private CommonOrderWorkManagementService commonOrderWorkManagementService;
    @Autowired
    private CommonOrdWorkOrderInfoService commonOrdWorkOrderInfoService;
    @Autowired
    private IStockOutOrderLineService stockOutOrderLineService;
    /**
     * 分页列表查询
     *
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "app工单管理-列表查询")
    @ApiOperation(value = "app工单管理-列表查询", notes = "app工单管理-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryOrderWorkInfoList(@RequestParam String userId,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                            HttpServletRequest req) {

//        QueryWrapper<OrdWorkOrderInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("is_Del",0);
//        List<OrdWorkOrderInfo> ordWorkOrderInfos = ordWorkOrderInfoService.list(queryWrapper);
        List<OrdWorkOrderInfo> ordWorkOrderInfos = orderWorkManagementService.queryOrderWorkInfoList(userId);
//        ordWorkOrderInfos.forEach(item->{
//            if(!StringUtil.isNullOrEmpty(item.getEquipNo())){
//                List<Equip> equips=ivAlarmInfoService.queryListEquip(item.getEquipNo());
//                if (!CollectionUtil.isEmpty(equips)){
//                    FaultPrdProductInfoVO faultPrdProductInfoVO =new FaultPrdProductInfoVO();
//                    faultPrdProductInfoVO.setProductId(equips.get(0).getEquipId());
//                    faultPrdProductInfoVO.setLineNo(equips.get(0).getAreaId());
//                    List<FaultPrdProductInfoVO> faultPrdProductInfoVOList=  faultPrdProductInfoService.queryFaultPrdProduct(faultPrdProductInfoVO);
//                    if(!CollectionUtil.isEmpty(faultPrdProductInfoVOList)){
//                        item.setAlarmLevel(faultPrdProductInfoVOList.get(0).getFaultGrade());
//                    }
//                }
//            }
//        });
        return Result.ok(ordWorkOrderInfos);
    }

    /**
     * 通过id查询工单信息
     * @param orderId
     * @return
     */
    @AutoLog(value = "app工单管理-查询工单信息")
    @ApiOperation(value = "app工单管理-查询工单信息", notes = "app工单管理-查询工单信息")
    @GetMapping(value = "/queryOrderWorkInfo")
    public Result<?> queryOrderWorkInfo(@RequestParam(name = "id", required = true) String orderId) {
        QueryWrapper<OrdWorkOrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId);
        List<OrdWorkOrderInfo> list = orderInfoService.list(queryWrapper);
        if(!CollectionUtil.isEmpty(list)){
            OrdWorkOrderInfo ordWorkOrderInfo = list.get(0);
            return Result.ok(ordWorkOrderInfo);
        }else{
            return Result.error("此故障报警信息没有生成工单");
        }
    }

    /**
     * 通过检定线id和设备编号查询故障原因
     * @param lineNo  检定线id
     * @param equipNo 设备编号
     * @return
     */
    @AutoLog(value = "app工单管理-查询故障原因")
    @ApiOperation(value = "app工单管理-查询故障原因", notes = "app工单管理-查询故障原因")
    @GetMapping(value = "/queryFaultReason")
    public Result<?> queryFaultReason(@RequestParam(name="lineNo",required=true) String lineNo,@RequestParam(name = "equipNo",required = true)String equipNo) {
        List<FaultReasonInfo> faultReasonInfoList = faultReasonInfoService.errorReasonNameAndId(lineNo,equipNo);
        if(faultReasonInfoList.size() == 0) {
            return Result.error("未找到故障原因数据");
        }
        return Result.ok(faultReasonInfoList);
    }

    /**
     * 通过报警名称和设备编号查询修复方法
     * @param faultTaskInfo
     * @return
     */
    @AutoLog(value = "app工单管理-查询修复方法")
    @ApiOperation(value = "app工单管理-查询修复方法", notes = "app工单管理-查询修复方法")
    @PostMapping(value = "/queryRepairInfo")
    public Result<?> queryRepairInfo(@RequestBody FaultTaskInfo faultTaskInfo) {
        List<FaultRepairInfo> faultRepairInfoList = ordProcessInfoService.queryRepairInfo(faultTaskInfo);
        if (faultRepairInfoList.size() == 0) {
            return Result.error("未找到修复方法数据");
        }
        return Result.ok(faultRepairInfoList);
    }

    /**
     * 查询物料信息
     * @param
     * @return
     */
    @AutoLog(value = "app工单管理-查询物料信息")
    @ApiOperation(value = "app工单管理-查询物料信息", notes = "app工单管理-查询物料信息")
    @GetMapping(value = "/queryItemList")
    public Result<?> queryWlPageList( ScrapPro scrapPro,
                                      @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                      @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        Result<List<ScrapPro>> result=new Result<>();
        result.setCode(0);
        List<ScrapPro> records=scrapProService.queryProdList(scrapPro);
        for(int i = 0; i<records.size(); i++){
            if(records.get(i).getVirtualQty() == 0){
                records.remove(i);
            }
        }
        result.setResult(records);
        return result;

    }

    /**
     * 新增领料保存
     *
     * @param stockOutOrderPage
     * @return
     */
    @AutoLog(value = "app工单管理-新增领料保存")
    @ApiOperation(value = "app工单管理-新增领料保存", notes = "app工单管理-新增领料保存")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody StockOutOrderPage stockOutOrderPage) {
        StockOutOrder stockOutOrder = new StockOutOrder();
        BeanUtils.copyProperties(stockOutOrderPage, stockOutOrder);
        if(stockOutOrderPage.getEquipName()!=null) {
            List<EquipIdModel> equipIdModel = iEquipldTreeService.selectEquipById(stockOutOrderPage.getEquipName());
            if(!CollectionUtils.isEmpty(equipIdModel)){
                stockOutOrder.setEquipName(equipIdModel.get(0).getTitle());
                stockOutOrder.setEquipId(equipIdModel.get(0).getEquipId());
            }
        }
        stockOutOrder.setState("approve");
        stockOutOrderService.saveStockOutMessage(stockOutOrder, stockOutOrderPage.getStockOutOrderLineList());
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param stockOutOrderPage
     * @return
     */
    @AutoLog(value = "app工单管理-领料出库单修改")
    @ApiOperation(value = "app工单管理-领料出库单修改", notes = "app工单管理-领料出库单修改")
    @PostMapping(value = "/stockOutEdit")
    public Result<?> stockOutEdit(@RequestBody StockOutOrderPage stockOutOrderPage) {
        StockOutOrder stockOutOrder = new StockOutOrder();
        BeanUtils.copyProperties(stockOutOrderPage, stockOutOrder);
        StockOutOrder stockOutOrderEntity = stockOutOrderService.getById(stockOutOrder.getId());
        if (stockOutOrderEntity == null) {
            return Result.error("未找到对应数据");
        }
        stockOutOrderService.updateMain(stockOutOrder, stockOutOrderPage.getStockOutOrderLineList());
        return Result.ok("编辑成功!");
    }


    /**
     *  修改
     *
     * @param ordWorkOrderInfo
     * @return
     */
    @AutoLog(value = "app工单管理-工单信息修改")
    @ApiOperation(value="app工单管理-工单信息修改", notes="app工单管理-工单信息修改")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody OrdWorkOrderInfo ordWorkOrderInfo) {
        ordWorkOrderInfoService.updateById(ordWorkOrderInfo);
        return Result.ok("编辑成功!");
    }

    /**
     *   app工单管理-工单处理提交
     *
     * @param ordProcessInfoEnclosureInfoVo
     * @return
     */
    @AutoLog(value = "app工单管理-工单处理提交")
    @ApiOperation(value="app工单管理-工单处理提交", notes="app工单管理-工单处理提交")
    @PostMapping(value = "/workOrderAdd")
    public Result<?> workOrderAdd(@RequestBody OrdProcessInfoEnclosureInfoVo ordProcessInfoEnclosureInfoVo) {
//        LoginUser loginUser = iSysBaseAPI.getUserById(ordProcessInfoEnclosureInfoVo.getCreateId());
//        if (loginUser != null) {
//            ordProcessInfoEnclosureInfoVo.setCreateName(loginUser.getRealname());
//        }
        ordProcessInfoEnclosureInfoVo.setDataFrom("02");
        ordProcessInfoService.saveOrdProcessAndEnclosureInfo(ordProcessInfoEnclosureInfoVo);
        return Result.ok("添加成功！");
    }

    @AutoLog(value = "app工单管理-转派查询所有用户信息")
    @ApiOperation(value="app工单管理-转派查询所有用户信息", notes="app工单管理-转派查询所有用户信息")
    @GetMapping(value = "/queryUserMessage")
    public Result<?> queryUserMessage(SysUser sysUser){

        List<SysUser> users = commonOrderWorkManagementService.queryUserMessage(sysUser);

        if(CollectionUtils.isEmpty(users)){
            return Result.error("没有人员信息");
        }else{
            users.forEach(item->{
                QueryWrapper queryWrapper = new QueryWrapper<SysUser> ();
                queryWrapper.eq("DISPATCH_TO_USER",item.getId());
                queryWrapper.eq("PROCESS_STATUS","04");
                item.setSize(ordWorkOrderInfoService.count(queryWrapper));
            });
        }
        return Result.ok(users);
    }

    /**
     *  转派
     *
     * @param ordWorkOrderInfo
     * @return
     */
    @AutoLog(value = "app工单管理-转派")
    @ApiOperation(value="转派", notes="转派")
    @PostMapping(value = "/WorkOrderRedeploy")
    public Result<?> WorkOrderRedeploy(@RequestBody OrdWorkOrderInfo ordWorkOrderInfo) {
        if (ordWorkOrderInfo == null){
            throw new JeecgBootException("转派信息为空");
        }
//        SysUser sysUser = commonOrdWorkOrderInfoService.getUserInfoById(ordWorkOrderInfo.getDispatchToUser());
//        if(BeanUtil.isEmpty(sysUser)){
//            return Result.error("人员信息错误");
//        }
        orderWorkManagementService.WorkOrderRedeploy(ordWorkOrderInfo);
        return Result.ok("添加成功！");
    }


    /**
     * 通过工单领料出库单号查询领料出库信息
     *
     * @param orderOutId
     * @return
     */
    @AutoLog(value = "app工单管理-通过领料单号查询领料出库信息")
    @ApiOperation(value = "app工单管理-通过领料单号查询领料出库信息", notes = "app工单管理-通过领料单号查询领料出库信息")
    @GetMapping(value = "/queryStockOutByOutId")
    public Result<?> queryStockOutByOutId(@RequestParam(name = "orderOutId", required = true) String orderOutId) {
        StockOutOrder stockOutOrder = stockOutOrderService.queryStockOutById(orderOutId);
        if (stockOutOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(stockOutOrder);
    }

    /**
     * 通过id查询出库明细
     *
     * @param id
     * @return
     */
    @AutoLog(value = "app工单管理-通过id查询出库明细")
    @ApiOperation(value = "app工单管理-通过id查询出库明细", notes = "app工单管理-通过id查询出库明细")
    @GetMapping(value = "/queryStockOutOrderLineByMainId")
    public Result<?> queryStockOutOrderLineListByMainId(@RequestParam(name = "id", required = true) Integer id) {
        List<StockOutOrderLine> stockOutOrderLineList = stockOutOrderLineService.selectByMainId(id);
        return Result.ok(stockOutOrderLineList);
    }

}
