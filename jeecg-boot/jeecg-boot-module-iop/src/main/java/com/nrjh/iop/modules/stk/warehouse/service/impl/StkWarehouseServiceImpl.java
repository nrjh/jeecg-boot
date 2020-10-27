package com.nrjh.iop.modules.stk.warehouse.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.stk.location.entity.StkLocation;
import com.nrjh.iop.modules.stk.location.entity.StkLocationEnum;
import com.nrjh.iop.modules.stk.location.service.IStkLocationService;
import com.nrjh.iop.modules.stk.warehouse.entity.StkWarehouse;
import com.nrjh.iop.modules.stk.warehouse.mapper.StkWarehouseMapper;
import com.nrjh.iop.modules.stk.warehouse.service.IStkWarehouseService;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 库房
 * @Author: jeecg-boot
 * @Date:   2020-04-26
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkWarehouseServiceImpl extends ServiceImpl<StkWarehouseMapper, StkWarehouse> implements IStkWarehouseService {
    @Autowired
    private IStkLocationService stkLocationService;

    @Override
    @Transactional(rollbackFor = JeecgBootException.class)
    public void deleteById(String id){
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = JeecgBootException.class)
    public void saveWithChildLocation(StkWarehouse stkWarehouse) {
        baseMapper.insert(stkWarehouse);
//        //  增加其下默认位置 并保存到相应位置
//        addChildLocation(stkWarehouse);
    }

    private void addChildLocation(StkWarehouse stkWarehouse) {
        // 视图位置
        StkLocation local1 = saveLocation("1", stkWarehouse.getName(), StkLocationEnum.LOCATION.getUsageType(), stkWarehouse.getActive());
        stkWarehouse.setViewLocationId(local1.getId());
        // 库存位置  stocklocation
        StkLocation local2 = saveLocation(local1.getId().toString(), StkLocationEnum.STOCK.getName(), StkLocationEnum.STOCK.getUsageType(), stkWarehouse.getActive());
        stkWarehouse.setStockLocationId(local2.getId());
        // 入库区 whInput
        StkLocation local3 = saveLocation(local1.getId().toString(), StkLocationEnum.STOCK_INPUT.getName(), StkLocationEnum.STOCK_INPUT.getUsageType(), stkWarehouse.getActive());
        stkWarehouse.setWhInputStockLotId(local3.getId());
        // 质检区 whQc
        StkLocation local4 = saveLocation(local1.getId().toString(), StkLocationEnum.STOCK_QC.getName(), StkLocationEnum.STOCK_QC.getUsageType(), stkWarehouse.getActive());
        stkWarehouse.setWhQcStockLocId(local4.getId());
        // 出库区  whOut
        StkLocation local5 = saveLocation(local1.getId().toString(), StkLocationEnum.STOCK_OUT.getName(), StkLocationEnum.STOCK_OUT.getUsageType(), stkWarehouse.getActive());
        stkWarehouse.setWhOutputStockLocId(local5.getId());
        // 打包qu  whPack
        StkLocation local6 = saveLocation(local1.getId().toString(), StkLocationEnum.STOCK_PACK.getName(), StkLocationEnum.STOCK_PACK.getUsageType(), stkWarehouse.getActive());
        stkWarehouse.setWhPackStockLocId(local6.getId());

        // 作业类型  默认
        stkWarehouse.setInTypeId(1) ; // 入库类型
        stkWarehouse.setIntTypeId(2); // 内部调拨类型
        stkWarehouse.setOutTypeId(3); // 出库类型
        this.updateById(stkWarehouse);
    }

    private StkLocation saveLocation(String pid, String name, String usageType, int active) {
        StkLocation stkLocation = new StkLocation();
        stkLocation.setName(name);
        stkLocation.setUsageType(usageType);
        stkLocation.setPid(pid);
        stkLocation.setActive(active);
        stkLocationService.addStkLocation(stkLocation);
        stkLocationService.updateComplateName(stkLocation);
        return stkLocation;
    }
}
