package com.nrjh.iop.modules.workOrder.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.alarm.bind.entity.FaultPrdProductInfo;
import com.nrjh.iop.modules.alarm.bind.service.IFaultPrdProductInfoService;
import com.nrjh.iop.modules.alarm.record.service.IMtEquipAlarmInfoService;
import com.nrjh.iop.modules.workOrder.entity.OrdPressInfo;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import com.nrjh.iop.modules.workOrder.service.IOrdPressInfoService;
import com.nrjh.iop.modules.workOrder.service.IOrdWorkOrderInfoService;
import com.nrjh.iop.modules.workOrder.vo.OrdWorkOrderListVO;
import com.nrjh.iop.modules.workOrder.vo.OrdWorkOrderMenuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by shiyiteng on 2020/9/3
 * 工单管你-工单列表详情
 */
@Api(tags="故障运维工单表")
@RestController
@RequestMapping("/workOrder/ordWorkOrderDetail")
@Slf4j
public class OrdWorkOrderDetailController extends JeecgController<OrdWorkOrderInfo, IOrdWorkOrderInfoService> {
    @Autowired
    private IOrdWorkOrderInfoService ordWorkOrderInfoService;

    @Autowired
    private IMtEquipAlarmInfoService mtEquipAlarmInfoService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private IOrdPressInfoService ordPressInfoService;


    /**
     * 分页列表查询
     *
     * @param ordWorkOrderInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "故障运维工单表-分页列表查询")
    @ApiOperation(value="故障运维工单表-分页列表查询", notes="故障运维工单表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OrdWorkOrderInfo ordWorkOrderInfo,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<OrdWorkOrderInfo> queryWrapper = QueryGenerator.initQueryWrapper(ordWorkOrderInfo, req.getParameterMap());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //显示已处理的数据
        //01，待派工
        //02，已派工
        //03，已处理
        //04，已转派
        //09，已归档
        try{
            if(req.getParameterMap().get("startAlarmTime")!=null && req.getParameterMap().get("endAlarmTime")!=null){
                Date startAlarmTime = formatter.parse((String) req.getParameterMap().get("startAlarmTime")[0]);
                Date endAlarmTime = formatter.parse((String) req.getParameterMap().get("endAlarmTime")[0]);
                queryWrapper.between("ALARM_TIME",startAlarmTime,endAlarmTime);
            }

            if(req.getParameterMap().get("startResolveTime")!=null && req.getParameterMap().get("endResolveTime")!=null){
                Date startResolveTime = formatter.parse((String) req.getParameterMap().get("startResolveTime")[0]);
                Date endResolveTime = formatter.parse((String) req.getParameterMap().get("endResolveTime")[0]);
                queryWrapper.between("PROCESS_TIME",startResolveTime,endResolveTime);
            }
        }catch (ParseException e){
            log.error("数据转换异常");
            e.printStackTrace();
        }

        Page<OrdWorkOrderInfo> page = new Page<OrdWorkOrderInfo>(pageNo, pageSize);
        IPage<OrdWorkOrderInfo> pageList = ordWorkOrderInfoService.page(page, queryWrapper);
        List<OrdWorkOrderListVO> ordWorkOrderListVOS = pageList.getRecords().stream().map(o -> getCompleteData(o)).collect(Collectors.toList());
        Page<OrdWorkOrderListVO> ordWorkOrderListVOSPage = new Page<>();
        ordWorkOrderListVOSPage.setRecords(ordWorkOrderListVOS);
        ordWorkOrderListVOSPage.setSize(ordWorkOrderListVOS.size());
        return Result.ok(ordWorkOrderListVOSPage);
    }

    @AutoLog(value = "故障运维工单表-分页列表查询")
    @ApiOperation(value="故障运维工单表-分页列表查询", notes="故障运维工单表-分页列表查询")
    @GetMapping(value = "/page")
    public Result<?> page(OrdWorkOrderInfo ordWorkOrderInfo,
                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                          HttpServletRequest req) {
//        Map<String,String> dataMap = new HashMap<>();
//        req.getParameterMap().forEach((key,value) -> {
//            dataMap.put(key, value == null ? null : value[0]);
//        });
        QueryWrapper<OrdWorkOrderInfo> queryWrapper = QueryGenerator.initQueryWrapper(ordWorkOrderInfo, req.getParameterMap());
        //显示已处理的数据
        //01，待派工
        //02，已派工
        //03，已处理
        //04，已转派
        //09，已归档
        queryWrapper.eq("PROCESS_STATUS","03");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try{
            if(req.getParameterMap().get("startAlarmTime")!=null && req.getParameterMap().get("endAlarmTime")!=null){
                Date startAlarmTime = formatter.parse((String) req.getParameterMap().get("startAlarmTime")[0]);
                Date endAlarmTime = formatter.parse((String) req.getParameterMap().get("endAlarmTime")[0]);
                queryWrapper.between("ALARM_TIME",startAlarmTime,endAlarmTime);
            }

            if(req.getParameterMap().get("distributeStartDate")!=null && req.getParameterMap().get("distributeEndDate")!=null){
                Date distributeStartDate = formatter.parse((String) req.getParameterMap().get("distributeStartDate")[0]);
                Date distributeEndDate = formatter.parse((String) req.getParameterMap().get("distributeEndDate")[0]);
                queryWrapper.between("dispatch_time",distributeStartDate,distributeEndDate);
            }

        }catch (ParseException e){
            log.error("数据转换异常");
            e.printStackTrace();
        }
        Page<OrdWorkOrderInfo> page = new Page<>(pageNo, pageSize);
        IPage<OrdWorkOrderInfo> pageList = ordWorkOrderInfoService.page(page, queryWrapper);
        Page<OrdWorkOrderListVO> ordWorkOrderListVOSPage = new Page<>(pageNo, pageSize);
        if(CollectionUtils.isEmpty(pageList.getRecords())){
            return Result.ok(pageList);
        }
        List<OrdWorkOrderListVO> temp = CollectionUtils.isEmpty(pageList.getRecords()) ? null : pageList.getRecords().stream().map(o -> getCompleteData(o)).collect(Collectors.toList());
//        if (req.getParameterMap().get("forward")!=null) {
//            String dispatchToUserName = req.getParameterMap().get("dispatchToUserName")[0];
//            temp = temp.stream().filter(t -> dispatchToUserName.equals(t.getDispatchToUserName())).collect(Collectors.toList());
//        }
        ordWorkOrderListVOSPage.setRecords(temp);
        ordWorkOrderListVOSPage.setSize(pageList.getTotal());
        return Result.ok(ordWorkOrderListVOSPage);
    }


    /**
     * 分页列表查询
     *
     * @param ordWorkOrderInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "故障运维工单表-分页列表查询")
    @ApiOperation(value="故障运维工单表-分页列表查询", notes="故障运维工单表-分页列表查询")
    @GetMapping(value = "/endList")
    public Result<?> queryPageendList(OrdWorkOrderInfo ordWorkOrderInfo,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<OrdWorkOrderInfo> queryWrapper = QueryGenerator.initQueryWrapper(ordWorkOrderInfo, req.getParameterMap());
        Page<OrdWorkOrderInfo> page = new Page<OrdWorkOrderInfo>(pageNo, pageSize);
        IPage<OrdWorkOrderInfo> pageList = ordWorkOrderInfoService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    @AutoLog(value = "获取工单详情数据")
    @ApiOperation(value = "工单详情数据",notes = "工单详情数据")
    @GetMapping(value = "/detail")
    public Result<?> queryDetailByWorkOrderId(@RequestParam String workOrderId){
        OrdWorkOrderMenuVO ordWorkOrderDetail = ordWorkOrderInfoService.getOrdWorkOrderDetail(workOrderId);
        return Result.ok(ordWorkOrderDetail);
    }


    private OrdWorkOrderListVO getCompleteData(OrdWorkOrderInfo o){

        // 报警次数
        QueryWrapper q1Wrapper = new QueryWrapper();
        q1Wrapper.eq("EQUIP_NO",String.valueOf(o.getEquipNo()));
        int alarmCount = mtEquipAlarmInfoService.count(q1Wrapper);
        // 派发人
        LoginUser toUser = sysBaseAPI.getUserById(o.getDispatchToUser());
        if(BeanUtil.isEmpty(toUser)){
            log.error("派发人不存在，id = {}",o.getDispatchToUser());
        }
        // 督办人
        q1Wrapper = new QueryWrapper();
        q1Wrapper.eq("WORK_ORDER_ID",o.getId());
        List<OrdPressInfo> tempOrdPressInfo = ordPressInfoService.list(q1Wrapper);
        if(CollectionUtil.isEmpty(tempOrdPressInfo)){
            log.error("督办人不存在，WORK_ORDER_ID = {}",o.getId());
        }
        OrdWorkOrderListVO ordWorkOrderListVO = ConvertUtils.sourceToTarget(o, OrdWorkOrderListVO.class);
        // 故障等级 = 报警级别
        ordWorkOrderListVO.setFaultGrade(null == o.getAlarmLevel() ? null : o.getAlarmLevel());
        ordWorkOrderListVO.setAlarmAmount(alarmCount);
        ordWorkOrderListVO.setDispatchToUserName(null == toUser ? null :toUser.getRealname());
        if(!CollectionUtil.isEmpty(tempOrdPressInfo)){
            log.error("督办人不存在，WORK_ORDER_ID = {}",o.getId());
            ordWorkOrderListVO.setUrgeName(tempOrdPressInfo.get(0).getCreateName());
        }
        return ordWorkOrderListVO;
    }


}
