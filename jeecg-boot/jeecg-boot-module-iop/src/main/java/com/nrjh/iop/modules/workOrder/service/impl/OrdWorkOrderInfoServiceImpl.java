package com.nrjh.iop.modules.workOrder.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultTaskInfo;
import com.nrjh.iop.modules.alarm.alarmtype.service.IEquipldTreeService;
import com.nrjh.iop.modules.alarm.alarmtype.service.IFaultTaskInfoService;
import com.nrjh.iop.modules.alarm.alarmtype.vo.EquipIdModel;
import com.nrjh.iop.modules.alarm.bind.entity.FaultPrdProductInfo;
import com.nrjh.iop.modules.alarm.bind.service.IFaultPrdProductInfoService;
import com.nrjh.iop.modules.alarm.record.entity.MtEquipAlarmInfo;
import com.nrjh.iop.modules.alarm.record.mapper.MtEquipAlarmInfoMapper;
import com.nrjh.iop.modules.alarm.record.service.IMtEquipAlarmInfoService;
import com.nrjh.iop.modules.workOrder.entity.*;
import com.nrjh.iop.modules.workOrder.mapper.OrdEnclosureInfoMapper;
import com.nrjh.iop.modules.workOrder.mapper.OrdPressInfoMapper;
import com.nrjh.iop.modules.workOrder.mapper.OrdWorkOrderInfoMapper;
import com.nrjh.iop.modules.workOrder.service.*;
import com.nrjh.iop.modules.workOrder.vo.OrdWorkOrderEnclosureInfoVo;
import com.nrjh.iop.modules.workOrder.vo.OrdWorkOrderMenuVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Description: 故障运维工单表
 * @Author: lei-li
 * @Date:   2020-09-01
 * @Version: V1.0
 */
@Service
@DS("ora")
@Slf4j
public class OrdWorkOrderInfoServiceImpl extends ServiceImpl<OrdWorkOrderInfoMapper, OrdWorkOrderInfo> implements IOrdWorkOrderInfoService {

    @Autowired
    private OrdWorkOrderInfoMapper ordWorkOrderInfoMapper;
    @Autowired
    private OrdEnclosureInfoMapper ordEnclosureInfoMapper;
    @Autowired
    private IEquipldTreeService iEquipldTreeService;

    @Autowired
    private IFaultTaskInfoService faultTaskInfoService;

    @Autowired
    private IFaultPrdProductInfoService faultPrdProductInfoService;

    @Autowired
    private IOrdEnclosureInfoService ordEnclosureInfoService;

    @Autowired
    private IOrdProcessInfoService ordProcessInfoService;

    @Autowired
    private IOrdSuspendService ordSuspendService;
    @Autowired
    private IOrdRedeployInfoService service;

    @Autowired
    private OrdPressInfoMapper ordPressInfoMapper;
    @Autowired
    private MtEquipAlarmInfoMapper mtEquipAlarmInfoMapper;

    @Autowired
    private CommonOrdWorkOrderInfoService commonOrdWorkOrderInfoService;
    @Override
    @Transactional(rollbackFor = JeecgBootException.class)
    public void saveWorkOrderInfo(OrdWorkOrderEnclosureInfoVo ordWorkOrderEnclosureInfoVo) {
        //工单列表报警时间和工单创建时间一致
        OrdWorkOrderInfo ordWorkOrderInfo = new OrdWorkOrderInfo();
        BeanUtils.copyProperties(ordWorkOrderEnclosureInfoVo,ordWorkOrderInfo);
        //获取网省编码
        String provinceCode = ordWorkOrderEnclosureInfoVo.getProvinceCode();
        //获取日期
        SimpleDateFormat fmt = new SimpleDateFormat("yyMMdd");
        String date = fmt.format(new Date());
        //获取6位随机数字
        Integer randomNum = new Random().nextInt(1000000);
        String newStrNum = String.format("%06d", randomNum);
        String workOrder = provinceCode+"A"+date+newStrNum;
        ordWorkOrderInfo.setId(workOrder);
        ordWorkOrderInfo.setCreateTime(new Date());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String state = "02";
        if(state.equals(ordWorkOrderInfo.getProcessStatus())){
            ordWorkOrderInfo.setDispatchUser(sysUser.getId());
            ordWorkOrderInfo.setDispatchTime(new Date());
        }
        if(ordWorkOrderInfo.getEquipName()!=null){
            List<EquipIdModel> equipIdModel=iEquipldTreeService.selectEquipById(ordWorkOrderInfo.getEquipName());
            if(!CollectionUtils.isEmpty(equipIdModel)){
                ordWorkOrderInfo.setEquipName(equipIdModel.get(0).getTitle());
                ordWorkOrderInfo.setEquipNo(equipIdModel.get(0).getEquipNo());
                //根据设备编号和报警原因获取报警时间和报警编号
                QueryWrapper<MtEquipAlarmInfo> mtEquipAlarmInfoQueryWrapper = new QueryWrapper<>();
                mtEquipAlarmInfoQueryWrapper.eq("equip_no",equipIdModel.get(0).getEquipNo());
                mtEquipAlarmInfoQueryWrapper.eq("alarm_reason",ordWorkOrderEnclosureInfoVo.getAlarmReason());
                mtEquipAlarmInfoQueryWrapper.eq("id",ordWorkOrderEnclosureInfoVo.getAlarmReasonId());
                MtEquipAlarmInfo mtEquipAlarmInfo = mtEquipAlarmInfoMapper.selectOne(mtEquipAlarmInfoQueryWrapper);
                if(!(BeanUtil.isEmpty(mtEquipAlarmInfo))){
                    ordWorkOrderInfo.setAlarmTime(mtEquipAlarmInfo.getAlarmTime());
                    ordWorkOrderInfo.setFaultId(mtEquipAlarmInfo.getAlarmNo());
                    Date alarmTime = mtEquipAlarmInfo.getAlarmTime();
                    //工单列表报警时间和工单创建时间一致
                    ordWorkOrderInfo.setCreateTime(alarmTime != null ? alarmTime : new Date());
                }
            }
        }
        //根据工单类型设置督办标识
        if("09".equals(ordWorkOrderInfo.getOrderType())){
            ordWorkOrderInfo.setPressLabe("1");
        }else if("01".equals(ordWorkOrderInfo.getOrderType())){
            ordWorkOrderInfo.setPressLabe("0");
        }
//        ordWorkOrderInfo.setIsDelete(0);
        ordWorkOrderInfo.setCreateName(sysUser.getRealname());
        // 工单创建标识
        //soft:上位软件
        //Electrical：电测
        //artificial：人工录入
        //设置人工录入
        ordWorkOrderInfo.setAlarmSource("artificial");
        ordWorkOrderInfo.setOrderCreLabe("artificial");
        //存入工单信息
        ordWorkOrderInfoMapper.insert(ordWorkOrderInfo);

        OrdEnclosureInfo ordEnclosureInfo = new OrdEnclosureInfo();
        ordEnclosureInfo.setEnclosureName(ordWorkOrderEnclosureInfoVo.getEnclosureName());
        ordEnclosureInfo.setEnclosureUrl(ordWorkOrderEnclosureInfoVo.getEnclosureUrl());
        ordEnclosureInfo.setWorkOrderId(ordWorkOrderInfo.getId());
        ordEnclosureInfo.setProcessStatus("01");
        //附件表数据插入
        ordEnclosureInfoMapper.insert(ordEnclosureInfo);

        //督办工单提交
        if("1".equals(ordWorkOrderInfo.getPressLabe())){
            OrdPressInfo ordPressInfo = new OrdPressInfo();
            ordPressInfo.setId(ordWorkOrderInfo.getId());
            ordPressInfo.setWorkOrderId(ordWorkOrderEnclosureInfoVo.getId());
            ordPressInfo.setCreateTime(new Date());
            ordPressInfo.setCreateId(sysUser.getId());
            ordPressInfo.setCreateName(sysUser.getRealname());
            ordPressInfoMapper.insert(ordPressInfo);
        }

    }

    @Override
    public void saveDispatchingInfo(OrdWorkOrderInfo ordWorkOrderInfo) {
        //设置派工时间
        ordWorkOrderInfo.setDispatchTime(new Date());
        // 催办时间
//        ordWorkOrderInfo.setPressTime(ordWorkOrderInfo.getPressTime());
        //设置状态
        ordWorkOrderInfo.setProcessStatus("02");
        //获取派发人
//        ordWorkOrderInfo.setDispatchUser(ordWorkOrderInfo.getDispatchToUser());
        ordWorkOrderInfoMapper.updateById(ordWorkOrderInfo);
    }

    @Override
    public String getEquipNo(String id) {
        String equipNo = ordWorkOrderInfoMapper.getEquipNo(id);
        return equipNo;
    }

    /**
     * 报警信息
     * @param ordWorkId
     * @return
     */
    @Override
    public OrdWorkOrderMenuVO getAlarmInfo(Integer ordWorkId) {
        OrdWorkOrderInfo ordWorkOrderInfo = baseMapper.selectById(ordWorkId);
        if(BeanUtil.isEmpty(ordWorkOrderInfo)){
            log.error("工单不存在，查询Id = {}",ordWorkId);
            throw new JeecgBootException("工单不存在");
        }
        OrdWorkOrderMenuVO ordWorkOrderMenuVO = ConvertUtils.sourceToTarget(ordWorkOrderInfo, OrdWorkOrderMenuVO.class);
        // 故障等级
        FaultPrdProductInfo faultPrdProductInfo = faultPrdProductInfoService.getById(String.valueOf(ordWorkOrderInfo.getFaultId()));
        // 故障类型
        QueryWrapper q1 = new QueryWrapper<>();
        q1.eq("FAULT_TYPE",ordWorkOrderInfo.getEquipNo());
        q1.eq("FAULT_NAME",ordWorkOrderInfo.getAlarmReason());
        FaultTaskInfo faultTaskInfo = faultTaskInfoService.getOne(q1);
        ordWorkOrderMenuVO.setFaultGrade(faultPrdProductInfo.getFaultGrade());
        ordWorkOrderMenuVO.setFaultType(faultTaskInfo.getFaultLabe());
        return ordWorkOrderMenuVO;
    }

    @Override
    public OrdWorkOrderMenuVO getOrdWorkOrderDetail(String ordWorkId) {
        OrdWorkOrderInfo ordWorkOrderInfo = baseMapper.selectById(ordWorkId);
        /**
         * 报警时间
         * 检定线
         * 设备名称
         * 故障等级
         * 故障类型
         * 报警类容
         * 工单处理人：处理人
         * 工单编号
         * 工单名称 - 报警原因
         */
        if(BeanUtil.isEmpty(ordWorkOrderInfo)){
            log.error("工单不存在，查询Id = {}",ordWorkId);
            throw new JeecgBootException("工单不存在");
        }
        OrdWorkOrderMenuVO ordWorkOrderMenuVO = ConvertUtils.sourceToTarget(ordWorkOrderInfo, OrdWorkOrderMenuVO.class);

        // 故障类型
        QueryWrapper q1 = new QueryWrapper<>();
        FaultTaskInfo faultTaskInfo=new FaultTaskInfo();
        if(!StringUtils.isEmpty(ordWorkOrderInfo.getEquipNo()) &&  !StringUtils.isEmpty(ordWorkOrderInfo.getAlarmReason())){
            q1.eq("FAULT_TYPE",ordWorkOrderInfo.getEquipNo());
            q1.eq("FAULT_NAME",""+ordWorkOrderInfo.getAlarmReason());
             faultTaskInfo = faultTaskInfoService.getOne(q1);
        }
        // 故障等级 = 报警级别
        ordWorkOrderMenuVO.setFaultGrade(null == ordWorkOrderInfo.getAlarmLevel() ? null :ordWorkOrderInfo.getAlarmLevel());
        ordWorkOrderMenuVO.setFaultType(null == faultTaskInfo ? null : faultTaskInfo.getFaultLabe());


        /**
         * 描述
         * 附件
         * 上报时间
         * 班组
         * -- 先不写
         * 上报人
         * 联系方式
         * 登记人
         * 联系方式
         */
        OrdEnclosureInfo ordEnclosureInfo = ordEnclosureInfoService.queryEnclosureInfo(ordWorkId);
        ordWorkOrderMenuVO.setEnclosurenName(null == ordEnclosureInfo ? null : ordEnclosureInfo.getEnclosureName());
        ordWorkOrderMenuVO.setNotifyTime(null == ordEnclosureInfo ? null :ordWorkOrderInfo.getCreateTime());
        ordWorkOrderMenuVO.setDispatchTeam(null == ordEnclosureInfo ? null :ordWorkOrderInfo.getDispatchTeam());

        /**
         * 处理结果
         * 修复方法
         * 处理信息备注
         */
        OrdProcessInfo ordProcessInfo = ordProcessInfoService.getOrdProcessInfoByWordOrderId(ordWorkId);
        ordWorkOrderMenuVO.setSolveResult(null == ordProcessInfo ? null :  ordProcessInfo.getResult());
        ordWorkOrderMenuVO.setRepairName(null == ordProcessInfo ? null : ordProcessInfo.getRepairName());
        ordWorkOrderMenuVO.setResolveReason(null == ordProcessInfo ? null : ordProcessInfo.getReason());

        // 转派
        q1 = new QueryWrapper();
        q1.eq("WORK_ORDER_ID",ordWorkId);
        OrdRedeployInfo ordRedeployInfo = service.getOne(q1);
        ordWorkOrderMenuVO.setTurnResolvePerson(null == ordRedeployInfo ? null :ordRedeployInfo.getRedepUser());

        // 结单
        ordWorkOrderMenuVO.setUpdateTime(ordWorkOrderInfo.getUpdateTime());
        ordWorkOrderMenuVO.setOrderType(ordWorkOrderInfo.getOrderType());
        // 工单时长 = 工单催办时间- 创建时间
        if(ordWorkOrderInfo.getPressTime() == null || null == ordWorkOrderInfo.getCreateTime()){
            ordWorkOrderMenuVO.setHowTime("0天");
        }else {
            int totalTime = caculateTotalTime(ordWorkOrderInfo.getCreateTime(),ordWorkOrderInfo.getPressTime());
            ordWorkOrderMenuVO.setHowTime(totalTime+"天");
        }

        // 挂单
        List<OrdSuspend> list= ordSuspendService.list(q1);
        OrdSuspend ordSuspend=new OrdSuspend();
        if(!CollectionUtil.isEmpty(list)){
            ordSuspend=list.get(0);
        }
        ordWorkOrderMenuVO.setListTime(null == ordSuspend? null :ordSuspend.getSuspendBeginTime());
        ordWorkOrderMenuVO.setUntieTime(null == ordSuspend? null :ordSuspend.getSuspendEndTime());
        ordWorkOrderMenuVO.setListContent(null == ordSuspend? null :ordSuspend.getSuspendReason());
        ordWorkOrderMenuVO.setUntieContent(null == ordSuspend? null :ordSuspend.getResumeReason());
        return ordWorkOrderMenuVO;
    }

    // 计算两个日期相差的天数
    public int caculateTotalTime(Date startTime,Date endTime) {
        Long l = 0L;
        try {
            long ts = endTime.getTime();
            long ts1 = startTime.getTime();
            l = (ts - ts1) / (1000 * 60 * 60 * 24);

        } catch (Exception e) {
            log.error("日期运算异常");
            e.printStackTrace();
        }
        return l.intValue();
    }


    @Override
    @Transactional
    public void saveTransferInfo(OrdWorkOrderInfo ordWorkOrderInfo,SysUser sysUser) {
        //04，已转派
        ordWorkOrderInfo.setProcessStatus("04");
        //设置派工时间
        ordWorkOrderInfo.setDispatchTime(new Date());
        ordWorkOrderInfoMapper.updateById(ordWorkOrderInfo);
        OrdRedeployInfo redeployInfo = new OrdRedeployInfo();
        redeployInfo.setWorkOrderId(ordWorkOrderInfo.getId());
        redeployInfo.setRedepUserId(ordWorkOrderInfo.getDispatchToUser());
        redeployInfo.setRedepUser(sysUser.getRealname());
        redeployInfo.setRedepDate(new Date());
        redeployInfo.setCreateId(ordWorkOrderInfo.getCreateId());
        redeployInfo.setCreateName(ordWorkOrderInfo.getCreateName());
        redeployInfo.setCreateTime(ordWorkOrderInfo.getCreateTime());
        redeployInfo.setUpdateName(ordWorkOrderInfo.getUpdateName());
        redeployInfo.setUpdateTime(ordWorkOrderInfo.getUpdateTime());
        redeployInfo.setUpdateBy(ordWorkOrderInfo.getUpdateBy());
        service.save(redeployInfo);
    }

    @Override
    public IPage<OrdWorkOrderInfo> page(IPage<OrdWorkOrderInfo> page, Map<String, String> dataMap) {
        return ordWorkOrderInfoMapper.page(page,dataMap);
    }

    @Override
    public void deleteById(String id) {
//        OrdWorkOrderInfo ordWorkOrderInfo = new OrdWorkOrderInfo();
//        ordWorkOrderInfo.setId(id);
//        ordWorkOrderInfo.setIsDelete(1);
//        ordWorkOrderInfoMapper.updateById(ordWorkOrderInfo);
        baseMapper.deleteById(id);
    }

}
