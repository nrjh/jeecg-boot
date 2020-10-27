package com.nrjh.iop.modules.app.orderWorkManagement.service;

import com.nrjh.iop.modules.workOrder.entity.SysUser;

import java.util.List;

/**
 * 公共接口
 */
public interface CommonOrderWorkManagementService {

    List<SysUser> queryUserMessage(SysUser sysUser);
}
