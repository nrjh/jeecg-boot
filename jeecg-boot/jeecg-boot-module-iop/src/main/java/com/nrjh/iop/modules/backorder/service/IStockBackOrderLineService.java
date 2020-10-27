package com.nrjh.iop.modules.backorder.service;

import com.nrjh.iop.modules.backorder.entity.StockBackOrderLine;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 退回入库明细
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
public interface IStockBackOrderLineService extends IService<StockBackOrderLine> {

	public List<StockBackOrderLine> selectByMainId(Integer mainId);
}
