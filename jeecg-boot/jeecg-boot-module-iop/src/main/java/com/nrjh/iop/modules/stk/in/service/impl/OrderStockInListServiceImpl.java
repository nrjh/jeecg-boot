package com.nrjh.iop.modules.stk.in.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.stk.in.entity.OrderStockInList;
import com.nrjh.iop.modules.stk.in.mapper.OrderStockInListMapper;
import com.nrjh.iop.modules.stk.in.service.IOrderStockInListService;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: order_stock_in_list
 * @Author: jeecg-boot
 * @Date:   2020-08-18
 * @Version: V1.0
 */
@Service
@DS("iop")
public class OrderStockInListServiceImpl extends ServiceImpl<OrderStockInListMapper, OrderStockInList> implements IOrderStockInListService {
    @Autowired
    private OrderStockInListMapper mapper;

    @Override
    public List<OrderStockInList> selectByMainId(String mainId) {
        return mapper.selectByMainId(mainId);
    }

    @Override
    @Transactional(rollbackFor = JeecgBootException.class)
    public void deleteByStockOrderIdWithLogic(String stockOrderId) {
        mapper.deleteByStockOrderIdWithLogic(stockOrderId);
    }


}
