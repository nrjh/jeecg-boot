package com.nrjh.iop.modules.app.orderWorkManagement.service;

import com.nrjh.iop.modules.outOrder.entity.StockOutOrder;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrderLine;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import com.nrjh.iop.modules.workOrder.entity.SysUser;

import java.util.List;

public interface OrderWorkManagementService {
    /**
     * 查询app工单列表数据
     * @return
     */
    List<OrdWorkOrderInfo> queryOrderWorkInfoList(String userId);

    /**
     * app工单转派
     * @param ordWorkOrderInfo
     * @param
     */
    void WorkOrderRedeploy(OrdWorkOrderInfo ordWorkOrderInfo);


}
