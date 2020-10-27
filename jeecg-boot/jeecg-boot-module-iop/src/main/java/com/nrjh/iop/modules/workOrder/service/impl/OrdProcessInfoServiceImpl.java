package com.nrjh.iop.modules.workOrder.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultRepairInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultTaskInfo;
import com.nrjh.iop.modules.workOrder.entity.OrdEnclosureInfo;
import com.nrjh.iop.modules.workOrder.entity.OrdProcessInfo;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import com.nrjh.iop.modules.workOrder.mapper.OrdProcessInfoMapper;
import com.nrjh.iop.modules.workOrder.mapper.OrdWorkOrderInfoMapper;
import com.nrjh.iop.modules.workOrder.service.IOrdProcessInfoService;
import com.nrjh.iop.modules.workOrder.vo.OrdProcessInfoEnclosureInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 工单处理明细
 * @Author: jeecg-boot
 * @Date:   2020-09-08
 * @Version: V1.0
 */
@Service
@DS("ora")
@Slf4j
public class OrdProcessInfoServiceImpl extends ServiceImpl<OrdProcessInfoMapper, OrdProcessInfo> implements IOrdProcessInfoService {

    @Autowired
    private  OrdProcessInfoMapper ordProcessInfoMapper;

    @Autowired
    private OrdWorkOrderInfoMapper ordWorkOrderInfoMapper;
    @Override
    public List<FaultRepairInfo> queryRepairInfo(FaultTaskInfo faultTaskInfo) {
        faultTaskInfo.setFaultName(""+faultTaskInfo.getFaultName());
        return  ordProcessInfoMapper.queryRepairInfo(faultTaskInfo);
    }

    @Override
    public OrdProcessInfo getOrdProcessInfoByWordOrderId(String workOrderId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("WORK_ORDER_ID",workOrderId);
        OrdProcessInfo ordProcessInfo = baseMapper.selectOne(queryWrapper);
        if(BeanUtil.isEmpty(ordProcessInfo)){
            log.error("查询工单处理明细结果为null,workOrderId = {}",workOrderId);
            return null;
        }
        return ordProcessInfo;
    }

    @Override
    public void saveOrdProcessAndEnclosureInfo(OrdProcessInfoEnclosureInfoVo ordProcessInfoEnclosureInfoVo) {
        OrdProcessInfo ordProcessInfo = new OrdProcessInfo();
        BeanUtils.copyProperties(ordProcessInfoEnclosureInfoVo,ordProcessInfo);
        ordProcessInfo.setReasonName(ordProcessInfoEnclosureInfoVo.getErrorReasonName());
        ordProcessInfo.setReasonInfoId(ordProcessInfoEnclosureInfoVo.getErrorReasonId());
        ordProcessInfo.setId(null);
        //判断是否是app端数据
        if(ordProcessInfoEnclosureInfoVo.getDataFrom()!= null){
            ordProcessInfo.setDataFrom(ordProcessInfoEnclosureInfoVo.getDataFrom());
        }
        ordProcessInfoMapper.insert(ordProcessInfo);

        //附件表插入数据
        OrdEnclosureInfo ordEnclosureInfo = new OrdEnclosureInfo();
        BeanUtils.copyProperties(ordProcessInfoEnclosureInfoVo,ordEnclosureInfo);
        ordEnclosureInfo.setProcessStatus("03");
        ordEnclosureInfo.setId(null);

        //更改工单状态
        OrdWorkOrderInfo ordWorkOrderInfo = new OrdWorkOrderInfo();
        ordWorkOrderInfo.setId(ordProcessInfoEnclosureInfoVo.getId());
        ordWorkOrderInfo.setProcessStatus("03");
        ordWorkOrderInfo.setProcessTime(ordProcessInfoEnclosureInfoVo.getCreateTime());
        ordWorkOrderInfo.setProcessUser(ordProcessInfoEnclosureInfoVo.getCreateId());
        ordWorkOrderInfoMapper.updateById(ordWorkOrderInfo);
    }
}
