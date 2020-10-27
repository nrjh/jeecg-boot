package com.nrjh.iop.modules.app.orderWorkManagement.service.impl;

import com.nrjh.iop.modules.app.orderWorkManagement.mapper.CommonOrderWorkManagementMapper;
import com.nrjh.iop.modules.app.orderWorkManagement.service.CommonOrderWorkManagementService;
import com.nrjh.iop.modules.workOrder.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-10-20 14:54
 */
@Service
public class CommonOrderWorkManagementServiceImpl implements CommonOrderWorkManagementService {
    @Autowired
    private CommonOrderWorkManagementMapper commonOrderWorkManagementMapper;

    @Override
    public List<SysUser> queryUserMessage(SysUser sysUser) {

        return commonOrderWorkManagementMapper.queryUserMessage(sysUser);
    }
}
