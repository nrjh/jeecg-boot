package com.nrjh.iop.modules.backorder.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.backorder.entity.StockBackOrderLine;
import com.nrjh.iop.modules.backorder.mapper.StockBackOrderLineMapper;
import com.nrjh.iop.modules.backorder.service.IStockBackOrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 退回入库明细
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StockBackOrderLineServiceImpl extends ServiceImpl<StockBackOrderLineMapper, StockBackOrderLine> implements IStockBackOrderLineService {
	
	@Autowired
	private StockBackOrderLineMapper stockBackOrderLineMapper;
	
	@Override
	public List<StockBackOrderLine> selectByMainId(Integer mainId) {
		return stockBackOrderLineMapper.selectByMainId(mainId);
	}
}
