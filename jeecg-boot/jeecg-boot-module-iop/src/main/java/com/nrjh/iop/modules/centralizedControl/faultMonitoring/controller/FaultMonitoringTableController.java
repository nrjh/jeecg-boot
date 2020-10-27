package com.nrjh.iop.modules.centralizedControl.faultMonitoring.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.service.FaultMonitoringTableService;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringResultVo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringTableResultVo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringVo;
import com.nrjh.iop.modules.workOrder.entity.SysUser;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: 故障设备报警表数据
 * @author: lei-li
 * @create: 2020-10-08 18:07
 */
@RestController
@RequestMapping("/faultMonitoring/faultMonitoringTable")
public class FaultMonitoringTableController {

    @Autowired
    private FaultMonitoringTableService faultMonitoringTableService;

    @AutoLog(value = "故障类型表数据")
    @ApiOperation(value = "故障类型表数据", notes = "故障类型表数据")
    @GetMapping("/faultTypeTable")
    public Result<?> getFaultTypeTableList(FaultMonitoringVo faultMonitoringVo){

        List<FaultMonitoringTableResultVo> list = faultMonitoringTableService.getFaultTypeTableList(faultMonitoringVo);
        return Result.ok(list);
    }

    @AutoLog(value = "故障设备报警表数据")
    @ApiOperation(value = "故障设备报警表数据", notes = "故障设备报警表数据")
    @GetMapping("/faultEquipmentAlarmTable")
    public Result<?> getFaultEquipmentAlarmTableList(FaultMonitoringVo faultMonitoringVo){

        List<FaultMonitoringTableResultVo> list = faultMonitoringTableService.getFaultEquipmentAlarmTableList(faultMonitoringVo);
        return Result.ok(list);
    }
}
