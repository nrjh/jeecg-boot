package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProducePlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: DEV_PRODUCE_PLAN
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
public interface IDevProducePlanService extends IService<DevProducePlan> {
    List<Map<String,Object>> selectNumMap(@Param("id")String id);
}
