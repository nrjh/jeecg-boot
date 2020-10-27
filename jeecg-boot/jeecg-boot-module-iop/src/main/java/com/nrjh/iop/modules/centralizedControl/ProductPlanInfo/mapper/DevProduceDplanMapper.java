package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.mapper;

import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProduceDplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: DEV_PRODUCE_DPLAN
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Mapper
@Repository
public interface DevProduceDplanMapper extends BaseMapper<DevProduceDplan> {
    String getMaxId();
    List<DevProduceDplan> detailList(@Param("categ") String categ,@Param("produceCycleId") String produceCycleId);
}
