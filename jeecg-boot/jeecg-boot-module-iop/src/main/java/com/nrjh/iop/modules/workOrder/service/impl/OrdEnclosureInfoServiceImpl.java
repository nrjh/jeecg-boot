package com.nrjh.iop.modules.workOrder.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.workOrder.entity.OrdEnclosureInfo;
import com.nrjh.iop.modules.workOrder.mapper.OrdEnclosureInfoMapper;
import com.nrjh.iop.modules.workOrder.service.IOrdEnclosureInfoService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Description: 工单附件表
 * @Author: lei-li
 * @Date:   2020-09-02
 * @Version: V1.0
 */
@Service
@DS("ora")
public class OrdEnclosureInfoServiceImpl extends ServiceImpl<OrdEnclosureInfoMapper, OrdEnclosureInfo> implements IOrdEnclosureInfoService {

    @Autowired
    private OrdEnclosureInfoMapper ordEnclosureInfoMapper;
    @Override
    public OrdEnclosureInfo queryEnclosureInfo(String workOrderId) {
        QueryWrapper<OrdEnclosureInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_order_id",workOrderId);
        return ordEnclosureInfoMapper.selectOne(queryWrapper);
    }
}
