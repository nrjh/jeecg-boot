package com.nrjh.iop.modules.workOrder.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.workOrder.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * @Description: 工单附件表
 * @Author: lei-li
 * @Date:   2020-09-03
 * @Version: V1.0
 */
public interface CommonOrdWorkOrderInfoMapper {

    IPage<SysUser> queryUserInfo(Page<SysUser> page, Map<String, Object> dataMap);

    SysUser getUserInfoById(String id);

}
