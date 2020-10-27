package com.nrjh.iop.modules.stk.spareparts.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nrjh.iop.modules.stk.spareparts.entity.StkStock;
import com.nrjh.iop.modules.stk.spareparts.mapper.StkStockMapper;
import com.nrjh.iop.modules.stk.spareparts.service.IStkStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 库存信息表
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkStockServiceImpl extends ServiceImpl<StkStockMapper, StkStock> implements IStkStockService {

    @Autowired
    private StkStockMapper stkStockMapper;

    @Override
    public String queryProductName(int productLotId) {
        return stkStockMapper.queryProductName(productLotId);
    }

    @Override
    public List<StkStock> queryStockList(String productNo) {
        return stkStockMapper.queryStockList(productNo);
    }

    @Override
    public Integer productStockQty(Integer productId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id",productId);
        StkStock stkStock = baseMapper.selectOne(queryWrapper);
        return stkStock.getVirtualQty();
    }

}
