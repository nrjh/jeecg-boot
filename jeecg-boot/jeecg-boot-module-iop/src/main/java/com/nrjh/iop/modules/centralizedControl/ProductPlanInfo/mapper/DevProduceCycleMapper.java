package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProduceCycle;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.vo.TreeModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: DEV_PRODUCE_CYCLE
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Mapper
@Repository
public interface DevProduceCycleMapper extends BaseMapper<DevProduceCycle> {

    List<TreeModel> selectYearList();

    List<TreeModel> selectMonthList();

    List<DevProduceCycle> selectMonthData();

}
