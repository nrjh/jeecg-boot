package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProduceDplan;

import java.util.List;

/**
 * @Description: DEV_PRODUCE_DPLAN
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
public interface IDevProduceDplanService extends IService<DevProduceDplan> {

    String getMaxId();

    List<DevProduceDplan> detailList(String categ,String produceCycleId);

    void saveDPlan(DevProduceDplan devProduceDplan);

}
