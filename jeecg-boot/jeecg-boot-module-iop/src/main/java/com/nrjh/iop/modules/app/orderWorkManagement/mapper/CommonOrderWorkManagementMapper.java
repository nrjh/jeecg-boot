package com.nrjh.iop.modules.app.orderWorkManagement.mapper;

import com.nrjh.iop.modules.workOrder.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommonOrderWorkManagementMapper {
    /**
     * 查询所有用于信息
     * @param sysUser
     * @return
     */
    List<SysUser> queryUserMessage(@Param("sysUser") SysUser sysUser);
}
