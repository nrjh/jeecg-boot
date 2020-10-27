package com.nrjh.iop.modules.outOrder.service;

import com.nrjh.iop.modules.outOrder.entity.StockOutOrderLine;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.outOrder.vo.StockOutOrderLineVO;

import java.util.List;

/**
 * @Description: 出库明细表
 * @Author: jeecg-boot
 * @Date: 2020-08-19
 * @Version: V1.0
 */
public interface IStockOutOrderLineService extends IService<StockOutOrderLine> {

    public List<StockOutOrderLine> selectByMainId(Integer mainId);

    public List<StockOutOrderLineVO> selectVoByMainId(Integer mainId);

    public List<StockOutOrderLineVO> getGoodsLocation(Integer outOrderId,Integer productId,String productCode);
}
