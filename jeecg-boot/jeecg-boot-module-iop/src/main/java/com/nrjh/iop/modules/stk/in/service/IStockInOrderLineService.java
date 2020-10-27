package com.nrjh.iop.modules.stk.in.service;

import com.nrjh.iop.modules.order.vo.ProdAndStockVo;
import com.nrjh.iop.modules.stk.in.entity.StockInOrderLine;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderLineVo;

import java.util.List;

/**
 * @Description: 入库明细
 * @Author: jeecg-boot
 * @Date:   2020-08-16
 * @Version: V1.0
 */
public interface IStockInOrderLineService extends IService<StockInOrderLine> {

	public List<StockInOrderLineVo> selectByMainId(String mainId);

	/**
	 * 采购入库-入库-物料信息回显
	 * @param id
	 * @return
	 */
	List<ProdAndStockVo> selectStockByMainId(String id);
}
