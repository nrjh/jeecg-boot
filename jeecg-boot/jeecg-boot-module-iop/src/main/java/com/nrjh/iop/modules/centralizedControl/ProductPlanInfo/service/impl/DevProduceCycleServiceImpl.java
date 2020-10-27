package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProduceCycle;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.vo.TreeModel;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.mapper.DevProduceCycleMapper;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service.IDevProduceCycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: DEV_PRODUCE_CYCLE
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Service("devProduceCycleService")
@DS("ora")
public class DevProduceCycleServiceImpl extends ServiceImpl<DevProduceCycleMapper, DevProduceCycle> implements IDevProduceCycleService {

    @Autowired
    private DevProduceCycleMapper mapper;

    @Override
    public List<TreeModel> selectYearList() {
        return mapper.selectYearList();
    }

    @Override
    public List<TreeModel> selectMonthList() {
        return mapper.selectMonthList();
    }

    @Override
    public List<DevProduceCycle> selectMonthData() {
        return mapper.selectMonthData();
    }


}
