package com.nrjh.iop.modules.stk.warehouse.mapper;

import java.util.List;

import com.nrjh.iop.modules.stk.move.entity.StkMove;
import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.stk.warehouse.entity.StkWarehouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 库房
 * @Author: jeecg-boot
 * @Date: 2020-04-26
 * @Version: V1.0
 */
public interface StkWarehouseMapper extends BaseMapper<StkWarehouse> {
    public StkWarehouse selectByViewLocationId(@Param("locationId") Integer locationId);
}
