package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.controller;

import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto.CentralizedControlMonitorDTO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.serivce.DetectTaskListService;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.strategy.ChartsContext;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.DetectTaskListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/25 11:44
 */
@RestController
@RequestMapping("/centralized/centralizedMonitoring")
@Slf4j
@Api("集控监控概览")
public class CentralizedControlMonitorController {

    @Autowired
    private ChartsContext chartsContext;
    @Autowired
    private DetectTaskListService detectTaskListService;

    @AutoLog(value = "生产任务跟踪图信息")
    @ApiOperation(value="生产任务跟踪图信息", notes="生产任务跟踪图信息")
    @GetMapping(value = "/prdTaskTrace")
    public Result<?> prdTaskTraceList(CentralizedControlMonitorDTO centralizedControlMonitorDTO) {
        if(centralizedControlMonitorDTO.getEquipCateg()!=null && centralizedControlMonitorDTO.getEquipCateg().equals("-1")){
            centralizedControlMonitorDTO.setEquipCateg(null);
        }
        if(centralizedControlMonitorDTO.getEquipSpecCode()!=null && centralizedControlMonitorDTO.getEquipSpecCode().equals("-1")){
            centralizedControlMonitorDTO.setEquipSpecCode(null);
        }
        // 设置图形类型
        centralizedControlMonitorDTO.setChartType("PrdTaskTrace");
        Map<String, List<Object>> resultMap = chartsContext.executeStrategy(centralizedControlMonitorDTO);
        return Result.ok(resultMap);
    }

    @AutoLog(value = "运行情况统计")
    @ApiOperation(value="运行情况统计", notes="运行情况统计")
    @GetMapping(value = "/operationStatus")
    public Result<?> operationStatus(CentralizedControlMonitorDTO centralizedControlMonitorDTO) {
        if(centralizedControlMonitorDTO.getEquipCateg()!=null &&centralizedControlMonitorDTO.getEquipCateg().equals("-1")){
            centralizedControlMonitorDTO.setEquipCateg(null);
        }
        if(centralizedControlMonitorDTO.getEquipSpecCode()!=null && centralizedControlMonitorDTO.getEquipSpecCode().equals("-1")){
            centralizedControlMonitorDTO.setEquipSpecCode(null);
        }
        // 设置图形类型
        centralizedControlMonitorDTO.setChartType("OperationStatus");
        Map<String, List<Object>> resultMap = chartsContext.executeStrategy(centralizedControlMonitorDTO);
        return Result.ok(resultMap);
    }

    @AutoLog(value = "检定合格率统计")
    @ApiOperation(value = "检定合格率统计",notes = "检定合格率统计")
    @GetMapping(value = "/checkPassRate")
    public Result<?> checkPassRate(CentralizedControlMonitorDTO centralizedControlMonitorDTO){
        if(centralizedControlMonitorDTO.getEquipCateg()!=null &&centralizedControlMonitorDTO.getEquipCateg().equals("-1")){
            centralizedControlMonitorDTO.setEquipCateg(null);
        }
        if(centralizedControlMonitorDTO.getEquipSpecCode()!=null && centralizedControlMonitorDTO.getEquipSpecCode().equals("-1")){
            centralizedControlMonitorDTO.setEquipSpecCode(null);
        }
        // 设置图形类型
        centralizedControlMonitorDTO.setChartType("CheckPassRate");
        Map<String, List<Object>> resultMap = chartsContext.executeStrategy(centralizedControlMonitorDTO);
        return Result.ok(resultMap);
    }

    @AutoLog(value = "检定设备占比")
    @ApiOperation(value = "检定设备占比统计",notes = "检定设备占比统计")
    @GetMapping(value = "/checkEquipOccupyRate")
    public Result<?> checkEquipOccupyRate(CentralizedControlMonitorDTO centralizedControlMonitorDTO){
        if(centralizedControlMonitorDTO.getEquipCateg()!=null &&centralizedControlMonitorDTO.getEquipCateg().equals("-1")){
            centralizedControlMonitorDTO.setEquipCateg(null);
        }
        if(centralizedControlMonitorDTO.getEquipSpecCode()!=null && centralizedControlMonitorDTO.getEquipSpecCode().equals("-1")){
            centralizedControlMonitorDTO.setEquipSpecCode(null);
        }
        // 设置图形类型
        centralizedControlMonitorDTO.setChartType("checkEquipOccupyRate");
        Map<String, List<Object>> resultMap = chartsContext.executeStrategy(centralizedControlMonitorDTO);
        return Result.ok(resultMap);
    }

    @AutoLog(value = "检定任务列表")
    @ApiOperation(value = "检定任务列表",notes = "检定任务列表")
    @GetMapping(value = "/detectTaskList")
    public Result<?> detectTaskList(CentralizedControlMonitorDTO centralizedControlMonitorDTO){
        if(centralizedControlMonitorDTO.getEquipCateg()!=null &&centralizedControlMonitorDTO.getEquipCateg().equals("-1")){
            centralizedControlMonitorDTO.setEquipCateg(null);
        }
        if(centralizedControlMonitorDTO.getEquipSpecCode()!=null && centralizedControlMonitorDTO.getEquipSpecCode().equals("-1")){
            centralizedControlMonitorDTO.setEquipSpecCode(null);
        }
        List<DetectTaskListVO> detectTaskList = detectTaskListService.getDetectTaskList(centralizedControlMonitorDTO);
        return Result.ok(detectTaskList);
    }

}
