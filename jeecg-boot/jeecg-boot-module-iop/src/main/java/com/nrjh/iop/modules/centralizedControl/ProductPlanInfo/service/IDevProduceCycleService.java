package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProduceCycle;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.vo.TreeModel;

import java.util.List;

/**
 * @Description: DEV_PRODUCE_CYCLE
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
public interface IDevProduceCycleService extends IService<DevProduceCycle> {
    List<TreeModel> selectYearList();
    List<TreeModel> selectMonthList();
    List<DevProduceCycle> selectMonthData();
}
