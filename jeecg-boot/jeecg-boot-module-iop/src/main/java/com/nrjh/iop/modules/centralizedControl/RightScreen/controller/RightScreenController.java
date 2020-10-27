package com.nrjh.iop.modules.centralizedControl.RightScreen.controller;

import com.nrjh.iop.modules.centralizedControl.RightScreen.service.IRightScreenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.DictModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO
 * 右屏信息展示
 * @author Kou Shenhai 2413176044@leimingtech.com
 * @version 1.0
 * @date 2020/9/16 0016 上午 9:34
 */
@RestController
@RequestMapping("/centralized/rightScreen")
@Slf4j
@Api("右屏信息展示")
public class RightScreenController {

    @Autowired
    private ISysBaseAPI iSysBaseAPI;

    @Autowired
    private IRightScreenService iRightScreenService;

    @AutoLog(value = "在检信息列表-右屏左侧")
    @ApiOperation(value="在检信息列表-右屏左侧", notes="在检信息列表-右屏左侧")
    @GetMapping(value = "/checkList")
    public Result<?> checkList(@RequestParam(value = "areaId")String[] areaId){
        return iRightScreenService.checkList(areaId);
    }

    @AutoLog(value = "配送任务列表-右屏右侧")
    @ApiOperation(value="配送任务列表-右屏右侧", notes="配送任务列表-右屏右侧")
    @GetMapping(value = "/taskList")
    public Result<?> taskList(){
        return iRightScreenService.taskList();
    }

    @AutoLog(value = "通知公告列表-右屏右侧")
    @ApiOperation(value="通知公告列表-右屏右侧", notes="通知公告列表-右屏右侧")
    @GetMapping(value = "/announcementList")
    public Result<?> announcementList(){
        return iRightScreenService.announcementList();
    }

    @AutoLog(value = "报警信息-右屏中侧下方")
    @ApiOperation(value="报警信息-右屏中侧下方", notes="报警信息-右屏中侧下方")
    @GetMapping(value = "/alarmList")
    public Result<?> alarmList(@RequestParam("areaId")String[] areaId){
        return iRightScreenService.alarmList(areaId);
    }

    @AutoLog(value = "故障设备报警TOP8-右屏中侧下方")
    @ApiOperation(value="故障设备报警TOP8-右屏中侧下方", notes="故障设备报警TOP8-右屏中侧下方")
    @GetMapping(value = "/equipAlarmList")
    public Result <?> equipAlarmList(@RequestParam("areaId")String[] areaId){
        return iRightScreenService.equipAlarmList(areaId);
    }

    @AutoLog(value = "统计-右屏中侧上方")
    @ApiOperation(value="统计-右屏中侧上方", notes="统计-右屏中侧上方")
    @GetMapping(value = "/detectCountList")
    public Result<?> detectCountList(){
        return iRightScreenService.detectCountList();
    }

    @AutoLog(value = "故障类型统计-右屏中侧中三")
    @ApiOperation(value="故障类型统计-右屏中侧中三", notes="故障类型统计-右屏中侧中三")
    @GetMapping(value = "/faultTypeList")
    public Result<?> faultTypeList(@RequestParam("areaId")String[] areaId){
        List<DictModel> dictModels = iSysBaseAPI.queryDictItemsByCode("Fault_Type");
        System.out.println("..................................");
        System.out.println(dictModels);
        System.out.println("--------------------");
        return iRightScreenService.faultTypeList(areaId,dictModels);
    }

    @AutoLog(value = "流水故障占比-右屏中侧中一")
    @ApiOperation(value="流水故障占比-右屏中侧中一", notes="流水故障占比-右屏中侧中一")
    @GetMapping(value = "/lineFaultList")
    public Result<?> lineFaultList(@RequestParam("areaId")String[] areaId){
        return iRightScreenService.lineFaultList(areaId);
    }

    @AutoLog(value = "故障区域占比-右屏中侧中二")
    @ApiOperation(value="故障区域占比-右屏中侧中二", notes="故障区域占比-右屏中侧中二")
    @GetMapping(value = "/faultAreaList")
    public Result<?> faultAreaList(@RequestParam("areaId") String[] areaId){
        return iRightScreenService.faultAreaList(areaId);
    }

    @AutoLog(value = "当日检测走势-右屏中间")
    @ApiOperation(value="当日检测走势-右屏中间", notes="当日检测走势-右屏中间")
    @GetMapping(value = "/detectTrendList")
    public Result<?> detectTrendList(@RequestParam("areaId")String[] areaId){
        return iRightScreenService.detectTrendList(areaId);
    }

    @AutoLog(value = "切换模式-右屏中侧中二")
    @ApiOperation(value="切换模式-右屏中侧中二", notes="切换模式-右屏中侧中二")
    @GetMapping(value = "/switchMode")
    public Result <?> switchMode() {
        return iRightScreenService.switchMode();
    }

    @AutoLog(value = "出入库信息-右屏右侧")
    @ApiOperation(value="出入库信息-右屏右侧", notes="出入库信息-右屏右侧")
    @GetMapping(value = "/inOutEquipList")
    public Result<?> inOutEquipList(@RequestParam("areaId")String areaId){
        return iRightScreenService.inOutEquipList(areaId);
    }
}
