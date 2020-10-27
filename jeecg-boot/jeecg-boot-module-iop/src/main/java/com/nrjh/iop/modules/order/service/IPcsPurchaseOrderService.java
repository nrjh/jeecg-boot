package com.nrjh.iop.modules.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.order.entity.PcsPurchaseOrder;
import com.nrjh.iop.modules.order.vo.PcsPurchaseOrderPage;
import com.nrjh.iop.modules.order.vo.ProdAndStockVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 采购单
 * @Author: jeecg-boot
 * @Date:   2020-08-15
 * @Version: V1.0
 */
public interface IPcsPurchaseOrderService extends IService<PcsPurchaseOrder> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(PcsPurchaseOrder pcsPurchaseOrder, List<ProdAndStockVo> prodAndStockVos,List<String> roles) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PcsPurchaseOrder pcsPurchaseOrder, List<ProdAndStockVo> pcsOrderLineList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);

	IPage<PcsPurchaseOrder> pageList(com.baomidou.mybatisplus.extension.plugins.pagination.Page<PcsPurchaseOrder> page, PcsPurchaseOrder pcsPurchaseOrder);

	void commpleOrderPlan(PcsPurchaseOrder pcsPurchaseOrder,List<ProdAndStockVo> prodAndStockVos);

	IPage<PcsPurchaseOrder> selectPageOrders(IPage iPage, Map<String,Object> dataMap);

	public String getBusinessApproveNextRoleId(int businessSequence,String businessId,String businessType);

	
}
