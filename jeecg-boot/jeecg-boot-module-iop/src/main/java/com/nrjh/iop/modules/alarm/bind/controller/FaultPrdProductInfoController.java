package com.nrjh.iop.modules.alarm.bind.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.bind.entity.FaultGradeInfo;
import com.nrjh.iop.modules.alarm.bind.service.IEquipService;
import com.nrjh.iop.modules.alarm.bind.service.IFaultGradeInfoService;
import com.nrjh.iop.modules.alarm.bind.vo.EquipAlarmRefVO;
import com.nrjh.iop.modules.alarm.bind.vo.FaultPrdProductInfoVO;
import org.jeecg.common.api.vo.Result;
import com.nrjh.iop.modules.alarm.bind.entity.FaultPrdProductInfo;
import com.nrjh.iop.modules.alarm.bind.service.IFaultPrdProductInfoService;

import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: fault_prd_product_info
 * @Author: jeecg-boot
 * @Date: 2020-08-26
 * @Version: V1.0
 */
@Api(tags = "fault_prd_product_info")
@RestController
@RequestMapping("/alarm/bind/faultPrdProductInfo")
@Slf4j
public class FaultPrdProductInfoController extends JeecgController<FaultPrdProductInfo, IFaultPrdProductInfoService> {
    @Autowired
    private IFaultPrdProductInfoService faultPrdProductInfoService;

    @Autowired
    private IEquipService equipService;

    @Autowired
    private IFaultGradeInfoService faultGradeInfoService;

    /**
     * 列表查询
     *
     * @param faultPrdProductInfoVO
     * @param req
     * @return
     */
    @AutoLog(value = "fault_prd_product_info-列表查询")
    @ApiOperation(value = "fault_prd_product_info-列表查询", notes = "fault_prd_product_info-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(FaultPrdProductInfoVO faultPrdProductInfoVO,
                                   HttpServletRequest req) {
        List<FaultPrdProductInfoVO> list = faultPrdProductInfoService.pageAlarmByFaultTypeAndId( faultPrdProductInfoVO);
        return Result.ok(list);
    }

    @GetMapping(value = "/queryList")
    public Result<?> queryList(FaultPrdProductInfoVO faultPrdProductInfoVO,
                                   HttpServletRequest req) {
        List<FaultPrdProductInfoVO> list = faultPrdProductInfoService.pageAlarmByLineNoAndId( faultPrdProductInfoVO);
        return Result.ok(list);
    }

    /**
     * 树数据查询
     *
     * @return
     */
    @AutoLog(value = "检定线-区域-设备 树数据查询")
    @ApiOperation(value = "检定线-区域-设备 树数据查询", notes = "检定线-区域-设备 树数据查询")
    @GetMapping(value = "/tree")
    public Result<?> queryTreeList() {
        return Result.ok(equipService.getTreeList());
    }

    /**
     * 告警等级查询
     *
     * @return
     */
    @AutoLog(value = "告警等级查询")
    @ApiOperation(value = "告警等级查询", notes = "告警等级查询")
    @GetMapping(value = "/faultGrade")
    public Result<?> queryFaultGradeList() {
        List<FaultGradeInfo> list = faultGradeInfoService.list();
        return Result.ok(list);
    }

    /**
     * 设备详细查询
     *
     * @return
     */
    @AutoLog(value = "设备详细查询")
    @ApiOperation(value = "设备详细查询", notes = "设备详细查询")
    @GetMapping(value = "/equip")
    public Result<?> queryByEquipId(@RequestParam(name = "id", required = true) String id) {
        Equip equip = equipService.getById(id);
        return Result.ok(equip);
    }


    /**
     * 保存
     *
     * @param equipAlarmRefVo
     * @return
     */
    @AutoLog(value = "保存设备报警关系")
    @ApiOperation(value = "保存设备报警关系", notes = "保存设备报警关系")
    @PostMapping(value = "/save")
    public Result<?> add(@RequestBody EquipAlarmRefVO equipAlarmRefVo) {
        faultPrdProductInfoService.save(equipAlarmRefVo);
        return Result.ok("保存成功！");
    }

}
