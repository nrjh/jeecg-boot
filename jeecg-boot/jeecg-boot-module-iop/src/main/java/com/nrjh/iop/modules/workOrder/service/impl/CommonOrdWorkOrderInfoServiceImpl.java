package com.nrjh.iop.modules.workOrder.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.workOrder.entity.SysUser;
import com.nrjh.iop.modules.workOrder.mapper.CommonOrdWorkOrderInfoMapper;
import com.nrjh.iop.modules.workOrder.service.CommonOrdWorkOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-09-03 19:54
 */
@Service
public class CommonOrdWorkOrderInfoServiceImpl implements CommonOrdWorkOrderInfoService {

    @Autowired
    private CommonOrdWorkOrderInfoMapper commonOrdWorkOrderInfoMapper;

    @Override
    public IPage<SysUser> queryUserInfo(Page<SysUser> page, Map<String, Object> dataMap) {

        return commonOrdWorkOrderInfoMapper.queryUserInfo(page,dataMap);
    }

    @Override
    public SysUser getUserInfoById(String id) {

        return commonOrdWorkOrderInfoMapper.getUserInfoById(id);
    }

}
