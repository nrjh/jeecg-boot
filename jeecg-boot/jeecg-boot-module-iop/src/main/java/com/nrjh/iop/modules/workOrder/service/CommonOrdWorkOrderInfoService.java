package com.nrjh.iop.modules.workOrder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.workOrder.entity.SysUser;
import java.util.Map;

public interface CommonOrdWorkOrderInfoService {

    /**
     * 工单派工查询人员信息接口
     * @param page
     * @param dataMap
     * @return
     */
    IPage<SysUser> queryUserInfo(Page<SysUser> page, Map<String, Object> dataMap);

    SysUser getUserInfoById(String id);

}
