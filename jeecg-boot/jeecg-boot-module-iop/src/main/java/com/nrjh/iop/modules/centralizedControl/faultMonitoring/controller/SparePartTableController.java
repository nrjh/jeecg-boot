package com.nrjh.iop.modules.centralizedControl.faultMonitoring.controller;

import com.nrjh.iop.modules.centralizedControl.faultMonitoring.service.SparePartTableService;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringTableResultVo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringVo;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-10-09 22:54
 */
@RestController
@RequestMapping("/faultMonitoring/faultMonitoringTable")
public class SparePartTableController {

    @Autowired
    private SparePartTableService sparePartTableService;

    @AutoLog(value = "备品备件表数据")
    @ApiOperation(value = "备品备件表数据", notes = "备品备件表数据")
    @GetMapping("/sparePartTable")
    public Result<?> getSparePartTableList(FaultMonitoringVo faultMonitoringVo){

        List<FaultMonitoringTableResultVo> list = sparePartTableService.getSparePartTableList(faultMonitoringVo);
        return Result.ok(list);
    }
}
