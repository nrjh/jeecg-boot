package com.nrjh.iop.modules.backorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.backorder.entity.StockBackOrder;
import com.nrjh.iop.modules.order.vo.ProdAndStockVo;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderLineVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 退库单
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
public interface IStockBackOrderService extends IService<StockBackOrder> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(StockBackOrder stockBackOrder, List<ProdAndStockVo> stockBackOrderLineList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(StockBackOrder stockBackOrder,  List<ProdAndStockVo> stockBackOrderLineList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(Integer id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);

	void saveConfirm(List<StockInOrderLineVo> stockInOrderLineList);

	void updateBack(Integer id);
	
}
