package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service.IDevProduceDplanService;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProduceDplan;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.mapper.DevProduceDplanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: DEV_PRODUCE_DPLAN
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Service
@DS("ora")
public class DevProduceDplanServiceImpl extends ServiceImpl<DevProduceDplanMapper, DevProduceDplan> implements IDevProduceDplanService {

    @Autowired
    private DevProduceDplanMapper mapper;

    @Override
    public String getMaxId() {
        return mapper.getMaxId();
    }

    @Override
    public List<DevProduceDplan> detailList(String categ,String produceCycleId) {
        return mapper.detailList(categ,produceCycleId);
    }

    @Override
    public void saveDPlan(DevProduceDplan devProduceDplan) {
        mapper.insert(devProduceDplan);
    }
}
