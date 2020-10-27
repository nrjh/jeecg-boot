package com.nrjh.iop.modules.workOrder.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.bind.service.IFaultPrdProductInfoService;
import com.nrjh.iop.modules.alarm.bind.vo.FaultPrdProductInfoVO;
import com.nrjh.iop.modules.workOrder.entity.OrdPressInfo;
import com.nrjh.iop.modules.alarm.record.service.IVAlarmInfoService;
import com.nrjh.iop.modules.workOrder.entity.OrdPressInfo;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import com.nrjh.iop.modules.workOrder.entity.SysUser;
import com.nrjh.iop.modules.workOrder.service.CommonOrdWorkOrderInfoService;
import com.nrjh.iop.modules.workOrder.service.IOrdPressInfoService;
import com.nrjh.iop.modules.workOrder.service.IOrdWorkOrderInfoService;
import com.nrjh.iop.modules.workOrder.vo.OrdWorkOrderEnclosureInfoVo;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.iop.pub.pubuser.entity.PubUser2;
import org.jeecg.modules.iop.pub.pubuser.mapper.PubUserMapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 工单待反馈
 * @author: lei-li
 * @create: 2020-09-07 15:13
 */
@Api(tags = "故障运维工单表")
@RestController
@RequestMapping("/workOrder/ordWorkOrderFeedBack")
@Slf4j
public class OrdWorkOrderFeedBackController {
    @Autowired
    private IOrdWorkOrderInfoService ordWorkOrderInfoService;
    @Autowired
    private CommonOrdWorkOrderInfoService commonOrdWorkOrderInfoService;
    @Autowired
    private IOrdPressInfoService iOrdPressInfoService;


    @Autowired
    private IVAlarmInfoService ivAlarmInfoService;

    @Autowired
    private IOrdPressInfoService infoService;
    @Autowired
    private IFaultPrdProductInfoService faultPrdProductInfoService;

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
    @ApiOperation(value = "故障运维工单表-分页列表查询", notes = "故障运维工单表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OrdWorkOrderInfo ordWorkOrderInfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        if (ordWorkOrderInfo.getOrderType() != null) {
            JSONObject jsonObject = JSONObject.parseObject(ordWorkOrderInfo.getOrderType());
            Object o = jsonObject.get("key");
            ordWorkOrderInfo.setOrderType((String) o);
        }
        QueryWrapper<OrdWorkOrderInfo> queryWrapper = QueryGenerator.initQueryWrapper(ordWorkOrderInfo, req.getParameterMap());
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            if (req.getParameterMap().get("startApplyDate") != null && req.getParameterMap().get("endApplyDate") != null) {
                Date startApplyTime = formatter.parse(req.getParameterMap().get("startApplyDate")[0]);
                Date endApplyDate = formatter.parse(req.getParameterMap().get("endApplyDate")[0]);
                queryWrapper.between("CREATE_TIME",startApplyTime,endApplyDate);
            }
        }catch (JeecgBootException | ParseException e){
            e.printStackTrace();
        }
        queryWrapper.eq("PROCESS_STATUS", "02");
//        queryWrapper.eq("is_delete",0);
        Page<OrdWorkOrderInfo> page = new Page<OrdWorkOrderInfo>(pageNo, pageSize);
        IPage<OrdWorkOrderInfo> pageList = ordWorkOrderInfoService.page(page, queryWrapper);
        pageList.getRecords().forEach(item ->{
            QueryWrapper<OrdPressInfo> qw = new QueryWrapper<OrdPressInfo>();
            qw.eq("ID",item.getId());
            qw.orderByDesc("CREATE_TIME");
            List<OrdPressInfo> ordPressInfoList=  iOrdPressInfoService.list(qw);
            if(!CollectionUtil.isEmpty(ordPressInfoList)){
                item.setOrdPressBy(ordPressInfoList.get(0).getCreateId());
            }
            if(!StringUtil.isNullOrEmpty(item.getEquipNo())){
                List<Equip> equips=ivAlarmInfoService.queryListEquip(item.getEquipNo());
                if (!CollectionUtil.isEmpty(equips)){
//					item.setEquipName(equips.get(0).getName());
                    FaultPrdProductInfoVO faultPrdProductInfoVO =new FaultPrdProductInfoVO();
                    faultPrdProductInfoVO.setProductId(equips.get(0).getEquipId());
                    faultPrdProductInfoVO.setLineNo(equips.get(0).getAreaId());
                    List<FaultPrdProductInfoVO> faultPrdProductInfoVOList=  faultPrdProductInfoService.queryFaultPrdProduct(faultPrdProductInfoVO);
                    //todo 存在多个设备绑定不同的等级怎么处理？
                    if(!CollectionUtil.isEmpty(faultPrdProductInfoVOList)){
                        item.setAlarmLevel(faultPrdProductInfoVOList.get(0).getFaultGrade());
                    }
                }
            }
        });
        return Result.ok(pageList);
    }

    //获取派发人
/*    private OrdWorkOrderInfo getDispatchUser(OrdWorkOrderInfo ordWorkOrderInfo){
        String dispatchUser = ordWorkOrderInfo.getDispatchUser();
        PubUser2 user = iPubUserService2.getById(dispatchUser);
        ordWorkOrderInfo.setDispatchUser(user.getName());
        return ordWorkOrderInfo;
    }*/


    @AutoLog(value = "根据人员id查询人员信息")
    @ApiOperation(value = "故障运维工单表-分页列表查询", notes = "故障运维工单表-分页列表查询")
    @GetMapping(value = "/queryUserInfoById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysUser sysUser = commonOrdWorkOrderInfoService.getUserInfoById(id);
        if (sysUser == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(sysUser);
    }

    /**
     * 待反馈督办提交
     *
     * @param ordWorkOrderEnclosureInfoVo
     * @return
     */
    @AutoLog(value = "故障运维工单表-待反馈督办提交")
    @ApiOperation(value = "故障运维工单表-待反馈督办提交", notes = "故障运维工单表-待反馈督办提交")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OrdWorkOrderEnclosureInfoVo ordWorkOrderEnclosureInfoVo) {

        ordWorkOrderInfoService.saveWorkOrderInfo(ordWorkOrderEnclosureInfoVo);

        return Result.ok("添加成功！");
    }
}
