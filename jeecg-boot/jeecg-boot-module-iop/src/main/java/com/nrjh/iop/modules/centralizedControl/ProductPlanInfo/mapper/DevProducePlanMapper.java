package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.mapper;

import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProducePlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: DEV_PRODUCE_PLAN
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Mapper
public interface DevProducePlanMapper extends BaseMapper<DevProducePlan> {
    List<Map<String,Object>> selectNumMap(@Param("id")String id);
}
