package com.nrjh.iop.modules.app.orderWorkManagement.mapper;

import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderWorkManagementMapper {
    /**
     * 查询app工单列表数据
     * @return
     */
    List<OrdWorkOrderInfo> queryOrderWorkInfoList(@Param("userId") String userId);
}
