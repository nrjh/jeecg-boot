package com.nrjh.iop.modules.workOrder.mapper;

import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 故障运维工单表
 * @Author: jeecg-boot
 * @Date:   2020-09-01
 * @Version: V1.0
 */
public interface OrdWorkOrderInfoMapper extends BaseMapper<OrdWorkOrderInfo> {

    String selectProvinceCode(String provinceCode);

    String getEquipNo(String id);

    IPage<OrdWorkOrderInfo> page(IPage<OrdWorkOrderInfo> page, @Param("dataMap") Map<String, String> dataMap);
}
