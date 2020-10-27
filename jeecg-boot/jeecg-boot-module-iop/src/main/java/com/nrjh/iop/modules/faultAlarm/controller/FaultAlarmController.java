package com.nrjh.iop.modules.faultAlarm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.alarm.record.entity.VAlarmInfo;
import com.nrjh.iop.modules.alarm.record.service.IVAlarmInfoService;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import com.nrjh.iop.modules.workOrder.service.IOrdWorkOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 故障报警
 * @author: lei-li
 * @create: 2020-09-10 18:13
 */
@Api(tags="退库单")
@RestController
@RequestMapping("/faultAlarm/vAlarmInfo")
public class FaultAlarmController {
    @Autowired
    private IVAlarmInfoService ivAlarmInfoService;
    @Autowired
    private IOrdWorkOrderInfoService ordWorkOrderInfoService;

    @AutoLog(value = "故障报警视图表-分页列表查询")
    @ApiOperation(value="故障报警视图表-分页列表查询", notes="故障报警视图表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(VAlarmInfo vAlarmInfo,

                                   HttpServletRequest req) {
//        QueryWrapper<VAlarmInfo> queryWrapper = QueryGenerator.initQueryWrapper(vAlarmInfo, req.getParameterMap());

        QueryWrapper<VAlarmInfo> queryWrapper = new QueryWrapper<>();

        if(vAlarmInfo.getAlarmLevel()!=null){
            queryWrapper.eq("ALARM_LEVEL",vAlarmInfo.getAlarmLevel());
        }
        if(vAlarmInfo.getAlarmTimeStart()!=null){
            queryWrapper.gt("ALARM_TIME",vAlarmInfo.getAlarmTimeStart());
        }
        if(vAlarmInfo.getAlarmTimeEnd()!=null){
            queryWrapper.lt("ALARM_TIME",vAlarmInfo.getAlarmTimeEnd());
        }
        queryWrapper.isNotNull("ALARM_TIME");
        queryWrapper.orderByDesc("ALARM_TIME");
        Page<VAlarmInfo> page = new Page<VAlarmInfo>();
        IPage<VAlarmInfo> pageList = ivAlarmInfoService.page(page, queryWrapper);
        pageList.getRecords().forEach(item ->{
            if(!StringUtils.isEmpty(item.getEquipNo())){
                item.setAlarmAmount(ivAlarmInfoService.alarmAmount(item.getEquipNo()));
            }
        });
        return Result.ok(pageList);
    }

    /**
     * 故障报警模块分页列表查询
     *
     * @param ordWorkOrderInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "故障运维工单表-故障报警模块分页列表查询")
    @ApiOperation(value="故障运维工单表-故障报警模块分页列表查询", notes="故障运维工单表-故障报警模块分页列表查询")
    @GetMapping(value = "/workOrderList")
    public Result<?> queryPageList(OrdWorkOrderInfo ordWorkOrderInfo,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
//        QueryWrapper<OrdWorkOrderInfo> queryWrapper = QueryGenerator.initQueryWrapper(ordWorkOrderInfo, req.getParameterMap());
        QueryWrapper<OrdWorkOrderInfo> queryWrapper = new QueryWrapper<>();
        if(ordWorkOrderInfo.getAlarmLevel()!=null){
            queryWrapper.eq("ALARM_LEVEL",ordWorkOrderInfo.getAlarmLevel());
        }
        if(ordWorkOrderInfo.getEquipNo()!=null){
            queryWrapper.eq("EQUIP_NO",ordWorkOrderInfo.getEquipNo());
        }
        if(ordWorkOrderInfo.getProcessStatus()!=null){
            queryWrapper.eq("PROCESS_STATUS",ordWorkOrderInfo.getProcessStatus());
        }
        if(ordWorkOrderInfo.getStartTime()!=null){
            queryWrapper.gt("CREATE_TIME",ordWorkOrderInfo.getStartTime());
        }
        if(ordWorkOrderInfo.getEndTime()!=null){
            queryWrapper.lt("CREATE_TIME",ordWorkOrderInfo.getEndTime());
        }
        Page<OrdWorkOrderInfo> page = new Page<OrdWorkOrderInfo>(pageNo, pageSize);
        queryWrapper.eq("is_del",0);
        IPage<OrdWorkOrderInfo> pageList = ordWorkOrderInfoService.page(page, queryWrapper);
        return Result.ok(pageList);
    }
}
