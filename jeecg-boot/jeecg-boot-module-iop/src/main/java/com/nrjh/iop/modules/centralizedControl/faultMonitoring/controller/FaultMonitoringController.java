package com.nrjh.iop.modules.centralizedControl.faultMonitoring.controller;

import com.nrjh.iop.modules.centralizedControl.faultMonitoring.service.FaultMonitoringService;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringResultVo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringVo;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.DictModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 故障监控图形数据
 * @author: lei-li
 * @create: 2020-09-25 11:30
 */
@RestController
@RequestMapping("/faultMonitoring/faultMonitoring")
public class FaultMonitoringController {

    @Autowired
    private FaultMonitoringService faultMonitoringService;

    @Autowired
    private ISysBaseAPI iSysBaseAPI;

    @AutoLog(value = "故障设备汇总柱图数据")
    @ApiOperation(value = "故障设备汇总柱图数据", notes = "故障设备汇总柱图数据")
    @GetMapping("/list")
    public Result<?> getFaultEquipMentBarList(FaultMonitoringVo faultMonitoringVo){

        List<FaultMonitoringResultVo> list = faultMonitoringService.getFaultEquipMentBarList(faultMonitoringVo);
        return Result.ok(list);
    }

    @AutoLog(value = "流水线故障占比饼图数据")
    @ApiOperation(value = "流水线故障占比饼图数据", notes = "流水线故障占比饼图数据")
    @GetMapping("/areaLinePie")
    public Result<?> getAreaLinePieList(FaultMonitoringVo faultMonitoringVo){

        FaultMonitoringResultVo result = faultMonitoringService.getAreaLinePieList(faultMonitoringVo);
        return Result.ok(result);
    }

    @AutoLog(value = "故障设备饼图数据")
    @ApiOperation(value = "故障设备饼图数据", notes = "故障设备饼图数据")
    @GetMapping("/failureEquipmentPie")
    public Result<?> getFailureEquipmentPieList(FaultMonitoringVo faultMonitoringVo){

        FaultMonitoringResultVo result = faultMonitoringService.getFailureEquipmentPieList(faultMonitoringVo);
        return Result.ok(result);
    }

    @AutoLog(value = "智能分析故障设备类型饼图数据")
    @ApiOperation(value = "智能分析故障设备类型饼图数据", notes = "智能分析故障设备类型饼图数据")
    @GetMapping("/getFailureEquipTypePieByDay")
    public Result<?> getFailureEquipTypePieByDay(FaultMonitoringVo faultMonitoringVo){

        FaultMonitoringResultVo result = faultMonitoringService.getFailureEquipTypePieByDay(faultMonitoringVo);
        return Result.ok(result);
    }

    @AutoLog(value = "智能分析故障设备类型不同区域柱状图")
    @ApiOperation(value = "智能分析故障设备类型不同区域柱状图", notes = "智能分析故障设备类型不同区域柱状图")
    @GetMapping("/getFailureEquipTypeBarByDay")
    public Result<?> getFailureEquipTypeBarByDay(FaultMonitoringVo faultMonitoringVo){

        FaultMonitoringResultVo result = faultMonitoringService.getFailureEquipTypeBarByDay(faultMonitoringVo);
        return Result.ok(result);
    }
    @AutoLog(value = "智能分析同类设备故障对比柱状图")
    @ApiOperation(value = "智能分析同类设备故障对比柱状图", notes = "智能分析同类设备故障对比柱状图")
    @GetMapping("/getFailureEquipNoBarByEquipType")
    public Result<?> getFailureEquipNoBarByEquipType(FaultMonitoringVo faultMonitoringVo){

        FaultMonitoringResultVo result = faultMonitoringService.getFailureEquipNoBarByEquipType(faultMonitoringVo);
        return Result.ok(result);
    }

    @AutoLog(value = "故障类型饼图数据")
    @ApiOperation(value = "流水线故障类型饼图数据", notes = "流水线故障类型饼图数据")
    @GetMapping("/faultType")
    public Result<?> getFaultTypeList(FaultMonitoringVo faultMonitoringVo){

        FaultMonitoringResultVo result = faultMonitoringService.getFaultTypePieList(faultMonitoringVo);
        return Result.ok(result);
    }

    @AutoLog(value = "获取故障总时长图形数据")
    @ApiOperation(value = "获取故障总时长图形数据", notes = "获取故障总时长图形数据")
    @GetMapping("/totalTime")
    public Result<?> getFailureTotalTimeLineData(FaultMonitoringVo faultMonitoringVo){
        faultMonitoringVo.setLineCode("totalTime");
        List<FaultMonitoringResultVo> result = faultMonitoringService.getFailureTotalTimeLineData(faultMonitoringVo);
        return Result.ok(result);
    }

    @AutoLog(value = "获取故障总时长图形数据")
    @ApiOperation(value = "获取故障总时长图形数据", notes = "获取故障总时长图形数据")
    @GetMapping("/timeRate")
    public Result<?> getFailureHandlingTimeRateLineData(FaultMonitoringVo faultMonitoringVo){
        faultMonitoringVo.setLineCode("timeRate");
        List<FaultMonitoringResultVo> resultRate = faultMonitoringService.getFailureTotalTimeLineData(faultMonitoringVo);
        resultRate.forEach(item->{
            if(item.getMapData()!=null){
                Double totalAvg = 0.00;
                for(int i = 0;i<item.getMapData().size();i++){
                    if(item.getMapData().get(i).get("ALARMLEVEL")!=null){
                        String level = item.getMapData().get(i).get("ALARMLEVEL").toString();
                        String totalTime = item.getMapData().get(i).get("PROCESSTIME").toString();
                        //获取标准处理时长集合
                        List<DictModel>  areaResult = iSysBaseAPI.queryDictItemsByCode("WorkingOrderProcessingTime");
                        for(int j = 0;j<areaResult.size();j++){
                            if(level.equals(areaResult.get(j).getText())){
                                //计算故障及时率
                                if(totalTime!=null &&  !"0".equals(totalTime)){
                                    Double avg = Double.valueOf(areaResult.get(j).getValue())/Double.valueOf(totalTime);
                                    totalAvg+=avg;
                                }
                            }
                        }
                    }
                }
                //计算总的平均故障及时率
                Double avgs = totalAvg/item.getMapData().size();
                item.setAvgs(avgs);
            }
        });
        return Result.ok(resultRate);
    }

    @ApiOperation(value = "区域内设备故障分布", notes = "区域内设备故障分布")
    @GetMapping("/detailEquipTypeByAreaId")
    public Result<?> detailEquipTypeByAreaId(FaultMonitoringVo faultMonitoringVo){
        return Result.ok(faultMonitoringService.detailEquipTypeByAreaId(faultMonitoringVo));
    }

    @ApiOperation(value = "同类设备故障对比", notes = "同类设备故障对比")
    @GetMapping("/detailEquipByAreaIdAndEquipNO")
    public Result<?> detailEquipByAreaIdAndEquipNO(FaultMonitoringVo faultMonitoringVo){

        return Result.ok(faultMonitoringService.detailEquipByAreaIdAndEquipNO(faultMonitoringVo));
    }

    @ApiOperation(value = "单一设备故障分布", notes = "单一设备故障分布")
    @GetMapping("/detailFaultByEquipNOAndAreaId")
    public Result<?> detailFaultByEquipNOAndAreaId(FaultMonitoringVo faultMonitoringVo){

        return Result.ok(faultMonitoringService.detailFaultByEquipNOAndAreaId(faultMonitoringVo));
    }

    @ApiOperation(value = "设备故障纵向对比", notes = "设备故障纵向对比")
    @GetMapping("/detailFaultComparisonByAreaIdAndEquipNO")
    public Result<?> detailFaultComparisonByAreaIdAndEquipNO(FaultMonitoringVo faultMonitoringVo){

        return Result.ok(faultMonitoringService.detailFaultComparisonByAreaIdAndEquipNO(faultMonitoringVo));
    }

    @ApiOperation(value = "流水线同比", notes = "流水线同比")
    @GetMapping("/detailFaultComparisonByDay")
    public Result<?> detailFaultComparisonByDay(){
        return Result.ok(faultMonitoringService.detailFaultComparisonByDay());
    }

}
