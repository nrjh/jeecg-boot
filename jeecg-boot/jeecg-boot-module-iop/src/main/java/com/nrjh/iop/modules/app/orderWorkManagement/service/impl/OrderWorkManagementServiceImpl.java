package com.nrjh.iop.modules.app.orderWorkManagement.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.google.j2objc.annotations.AutoreleasePool;
import com.nrjh.iop.modules.app.orderWorkManagement.mapper.OrderWorkManagementMapper;
import com.nrjh.iop.modules.app.orderWorkManagement.service.OrderWorkManagementService;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrder;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrderLine;
import com.nrjh.iop.modules.outOrder.mapper.StockOutOrderLineMapper;
import com.nrjh.iop.modules.outOrder.mapper.StockOutOrderMapper;
import com.nrjh.iop.modules.workOrder.entity.OrdRedeployInfo;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import com.nrjh.iop.modules.workOrder.entity.SysUser;
import com.nrjh.iop.modules.workOrder.mapper.OrdWorkOrderInfoMapper;
import com.nrjh.iop.modules.workOrder.service.IOrdRedeployInfoService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-10-16 17:17
 */
@Service
@DS("ora")
public class OrderWorkManagementServiceImpl implements OrderWorkManagementService {

    @Autowired
    private OrderWorkManagementMapper orderWorkManagementMapper;
    @Autowired
    private OrdWorkOrderInfoMapper ordWorkOrderInfoMapper;
    @Autowired
    private IOrdRedeployInfoService iOrdRedeployInfoService;


    @Override
    public List<OrdWorkOrderInfo> queryOrderWorkInfoList(String userId) {
        return orderWorkManagementMapper.queryOrderWorkInfoList(userId);
    }

    @Override
    public void WorkOrderRedeploy(OrdWorkOrderInfo ordWorkOrderInfo) {
        //04，已转派
        ordWorkOrderInfo.setProcessStatus("04");
        //设置派工时间
        ordWorkOrderInfo.setDispatchTime(new Date());
        ordWorkOrderInfoMapper.updateById(ordWorkOrderInfo);
        OrdRedeployInfo redeployInfo = new OrdRedeployInfo();
        redeployInfo.setWorkOrderId(ordWorkOrderInfo.getId());
        redeployInfo.setRedepUserId(ordWorkOrderInfo.getDispatchToUser());
        redeployInfo.setRedepUser(ordWorkOrderInfo.getRedepUser());
        redeployInfo.setRedepDate(new Date());
        redeployInfo.setCreateId(ordWorkOrderInfo.getCreateId());
        redeployInfo.setCreateName(ordWorkOrderInfo.getCreateName());
        redeployInfo.setCreateTime(ordWorkOrderInfo.getCreateTime());
        redeployInfo.setUpdateName(ordWorkOrderInfo.getUpdateName());
        redeployInfo.setUpdateTime(ordWorkOrderInfo.getUpdateTime());
        redeployInfo.setUpdateBy(ordWorkOrderInfo.getUpdateBy());
        iOrdRedeployInfoService.save(redeployInfo);
    }

}
