package com.nrjh.iop.modules.app.faultManagement.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.alarm.alarmtype.service.IEquipldTreeService;
import com.nrjh.iop.modules.alarm.alarmtype.vo.EquipIdModel;
import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.bind.service.IFaultPrdProductInfoService;
import com.nrjh.iop.modules.alarm.bind.vo.FaultPrdProductInfoVO;
import com.nrjh.iop.modules.alarm.record.entity.MtEquipAlarmInfo;
import com.nrjh.iop.modules.alarm.record.entity.VAlarmInfo;
import com.nrjh.iop.modules.alarm.record.service.IMtEquipAlarmInfoService;
import com.nrjh.iop.modules.alarm.record.service.IVAlarmInfoService;
import com.nrjh.iop.modules.app.faultManagement.entity.Oarea;
import com.nrjh.iop.modules.app.faultManagement.service.FaultManagementService;
import com.nrjh.iop.modules.app.faultManagement.vo.EquipAndAreaVo;
import com.nrjh.iop.modules.outOrder.vo.StockOutOrderLineVO;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import com.nrjh.iop.modules.workOrder.service.IOrdWorkOrderInfoService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.geom.Area;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-10-15 14:42
 */
@Api(tags = "app故障管理")
@RestController
@RequestMapping("/app/faultManagement")
public class FaultManagementController {

    @Autowired
    private IVAlarmInfoService ivAlarmInfoService;
    @Autowired
    private IFaultPrdProductInfoService faultPrdProductInfoService;
    @Autowired
    private FaultManagementService faultManagementService;
    @Autowired
    private IEquipldTreeService iEquipldTreeService;
    @Autowired
    private IMtEquipAlarmInfoService mtEquipAlarmInfoService;
    @Autowired
    private IOrdWorkOrderInfoService orderInfoService;


    /**
     * 分页列表查询
     *
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "app故障管理-分页列表查询")
    @ApiOperation(value = "app故障管理-分页列表查询", notes = "app故障管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryFaultMessageList(VAlarmInfo vAlarmInfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
//        QueryWrapper<VAlarmInfo> queryWrapper = new QueryWrapper<>(vAlarmInfo);
//        Page<VAlarmInfo> page = new Page<VAlarmInfo>(pageNo, pageSize);
//        queryWrapper.isNotNull("ALARM_TIME");
//        queryWrapper.orderByDesc("ALARM_TIME");
//        List<VAlarmInfo> list = ivAlarmInfoService.list(queryWrapper);
        List<VAlarmInfo> list = faultManagementService.queryFaultMessageList();
//        IPage<VAlarmInfo> pageList = ivAlarmInfoService.page(page, queryWrapper);
//        list.forEach(item ->{
//            if(!StringUtil.isNullOrEmpty(item.getEquipNo())){
//                List<Equip> equips=ivAlarmInfoService.queryListEquip(item.getEquipNo());
//                if (!CollectionUtil.isEmpty(equips)){
//                    item.setEquipName(equips.get(0).getName());
//                    FaultPrdProductInfoVO faultPrdProductInfoVO =new FaultPrdProductInfoVO();
//                    faultPrdProductInfoVO.setProductId(equips.get(0).getEquipId());
//                    if(!StringUtils.isEmpty(item.getLineNo())){
//                        String lineNo=faultPrdProductInfoService.queryExplanInfo(item.getLineNo());
//                        faultPrdProductInfoVO.setLineNo(lineNo);
//                        item.setLineNo(lineNo);
//                    }
//                    List<FaultPrdProductInfoVO> faultPrdProductInfoVOList=  faultPrdProductInfoService.queryFaultPrdProduct(faultPrdProductInfoVO);
//                    if(!CollectionUtil.isEmpty(faultPrdProductInfoVOList)){
//                        item.setAlarmLevel(faultPrdProductInfoVOList.get(0).getFaultGrade());
//                    }
//                }
//                item.setAlarmAmount(ivAlarmInfoService.alarmAmount(item.getEquipNo()));
//            }
//        });
        return Result.ok(list);
    }

    /**
     * 添加
     *
     * @param mtEquipAlarmInfo
     * @return
     */
    @AutoLog(value = "app故障管理-添加")
    @ApiOperation(value = "app故障管理-添加", notes = "app故障管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody MtEquipAlarmInfo mtEquipAlarmInfo) {
        mtEquipAlarmInfo.setAlarmSource("人工录入");
        mtEquipAlarmInfo.setWriteTime(new Date());
//        List<EquipIdModel> equipIdModel=iEquipldTreeService.selectEquipById(mtEquipAlarmInfo.getEquipName());
//        if(!CollectionUtils.isEmpty(equipIdModel)){
//            mtEquipAlarmInfo.setEquipName(equipIdModel.get(0).getTitle());
//            mtEquipAlarmInfo.setEquipNo(equipIdModel.get(0).getEquipNo());
//        }
        mtEquipAlarmInfo.setAlarmTime(new Date());
        mtEquipAlarmInfo.setDataFrom("02");
        mtEquipAlarmInfoService.save(mtEquipAlarmInfo);
        return Result.ok("添加成功！");
    }

    /**
     * 通过equipNo查询设备名称
     * @param equipNo
     * @return
     */
    @AutoLog(value = "app故障管理-通过设备编号查设备名称")
    @ApiOperation(value = "app故障管理-通过设备编号查设备名称", notes = "app故障管理-通过设备编号查设备名称")
    @GetMapping(value = "/queryEquipAndAreaById")
    public Result<?> queryEquipAndAreaById(@RequestParam(name = "id", required = true) String equipNo) {
        EquipAndAreaVo equipAndAreaVo = faultManagementService.queryEquipAndAreaById(equipNo);
        return Result.ok(equipAndAreaVo);
    }

    /**
     * 通过检定线Id查询下面所有设备
     * @param lineId
     * @return
     */
    @AutoLog(value = "app故障管理-通过检定线Id查询下面所有设备")
    @ApiOperation(value = "app故障管理-通过检定线Id查询下面所有设备", notes = "app故障管理-通过检定线Id查询下面所有设备")
    @GetMapping(value = "/queryEquipByLineId")
    public Result<?> queryEquipByLineId(@RequestParam(name = "id", required = true) String lineId) {
        List<Equip> equips = faultManagementService.queryEquipByLineId(lineId);
        return Result.ok(equips);
    }

    /**
     * 查询所有检定线信息
     * @param
     * @return
     */
    @AutoLog(value = "app故障管理-查询检定线信息")
    @ApiOperation(value = "app故障管理-查询检定线信息", notes = "app故障管理-查询检定线信息")
    @GetMapping(value = "/queryAreaInfo")
    public Result<?> queryAreaInfo() {
        List<Oarea> oareas = faultManagementService.queryAreaInfo();
        return Result.ok(oareas);
    }

    /**
     * 通过报警编号查询工单信息
     * @param faultId
     * @return
     */
    @AutoLog(value = "app故障管理-查询工单信息")
    @ApiOperation(value = "app故障管理-查询工单信息", notes = "app故障管理-查询工单信息")
    @GetMapping(value = "/queryOrderWorkInfo")
    public Result<?> queryOrderWorkInfo(@RequestParam(name = "id", required = true) String faultId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("FAULT_ID",faultId);
        List<OrdWorkOrderInfo> list = orderInfoService.list(queryWrapper);
        if(!CollectionUtil.isEmpty(list)){
            OrdWorkOrderInfo ordWorkOrderInfo = list.get(0);
            return Result.ok(ordWorkOrderInfo);
        }else{
            return Result.error("此故障报警信息没有生成工单");
        }
    }

    /**
     * 通过检定线id和设备id查询设备绑定故障信息
     * @param faultPrdProductInfoVO
     * @return
     */
    @AutoLog(value = "app故障管理-查询设备绑定故障信息")
    @ApiOperation(value = "app故障管理-查询设备绑定故障信息", notes = "app故障管理-查询设备绑定故障信息")
    @GetMapping(value = "/queryFaultTaskInfoList")
    public Result<?> queryFaultTaskInfoList(FaultPrdProductInfoVO faultPrdProductInfoVO) {
        List<FaultPrdProductInfoVO> list = faultPrdProductInfoService.pageAlarmByLineNoAndId( faultPrdProductInfoVO);
        return Result.ok(list);
    }
}
