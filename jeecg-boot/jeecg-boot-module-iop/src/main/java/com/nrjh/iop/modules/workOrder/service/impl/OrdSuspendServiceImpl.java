package com.nrjh.iop.modules.workOrder.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.workOrder.entity.OrdSuspend;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import com.nrjh.iop.modules.workOrder.mapper.OrdSuspendMapper;
import com.nrjh.iop.modules.workOrder.mapper.OrdWorkOrderInfoMapper;
import com.nrjh.iop.modules.workOrder.service.IOrdSuspendService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

/**
 * @Description: 工单挂单表
 * @Author: lei-li
 * @Date:   2020-09-03
 * @Version: V1.0
 */
@Service
@DS("ora")
public class OrdSuspendServiceImpl extends ServiceImpl<OrdSuspendMapper, OrdSuspend> implements IOrdSuspendService {

    @Autowired
    private OrdSuspendMapper ordSuspendMapper;
    @Autowired
    private OrdWorkOrderInfoMapper ordWorkOrderInfoMapper;
    @Override
    public void Suspend(OrdSuspend ordSuspend) {
        ordSuspend.setCreateTime(new Date());
        //获取当前登录用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ordSuspend.setCreateName(sysUser.getRealname());
        ordSuspend.setCreateId(sysUser.getId());
        //挂起
        ordSuspendMapper.insert(ordSuspend);
        //改工单表挂起状态
        OrdWorkOrderInfo ordWorkOrderInfo = new OrdWorkOrderInfo();
        ordWorkOrderInfo.setId(ordSuspend.getWorkOrderId());
        ordWorkOrderInfo.setIsSuspend(ordSuspend.getSuspendStauts());
        ordWorkOrderInfoMapper.updateById(ordWorkOrderInfo);
    }
}
