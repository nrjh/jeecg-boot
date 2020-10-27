package com.nrjh.iop.modules.stk.in.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.stk.in.entity.OrderStockInList;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderLineVo;

import java.util.List;

/**
 * @Description: order_stock_in_list
 * @Author: jeecg-boot
 * @Date:   2020-08-18
 * @Version: V1.0
 */
public interface IOrderStockInListService extends IService<OrderStockInList> {

    public List<OrderStockInList> selectByMainId(String mainId);

    void deleteByStockOrderIdWithLogic(String stockOrderId);
}
