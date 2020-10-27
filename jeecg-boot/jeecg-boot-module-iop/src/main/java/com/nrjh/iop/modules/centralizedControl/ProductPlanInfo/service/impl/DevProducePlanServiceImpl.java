package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service.IDevProducePlanService;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProducePlan;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.mapper.DevProducePlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: DEV_PRODUCE_PLAN
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Service
@DS("ora")
public class DevProducePlanServiceImpl extends ServiceImpl<DevProducePlanMapper, DevProducePlan> implements IDevProducePlanService {

    @Autowired
    private DevProducePlanMapper devProducePlanMapper;

    @Override
    public List<Map<String, Object>> selectNumMap(String id) {
        return devProducePlanMapper.selectNumMap(id);
    }

}
