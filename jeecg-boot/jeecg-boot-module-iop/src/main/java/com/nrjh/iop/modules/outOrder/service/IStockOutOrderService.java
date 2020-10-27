package com.nrjh.iop.modules.outOrder.service;

import com.nrjh.iop.modules.outOrder.entity.StockOutOrderLine;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.outOrder.vo.StockOutOrderVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 领料出库单
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
public interface IStockOutOrderService extends IService<StockOutOrder> {

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(StockOutOrder stockOutOrder,List<StockOutOrderLine> stockOutOrderLineList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(StockOutOrder stockOutOrder,List<StockOutOrderLine> stockOutOrderLineList);

	/**
	 * 删除一对多
	 */
	public void delMain (Integer id);

	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Integer[] idList);

    /**
     * 保存或修改货位详情
     *
     * @param stockOutOrderVO
     */
    public void saveOutOrderLocator(StockOutOrderVO stockOutOrderVO);

    /**
     *  批量退回
     * @param ids
     */
    public void backOutOrder(List<String> ids);


	/**
	 * 提交审批
	 * @param id
	 */
	void submitApproval(Integer id);

	/**
	 * 根据物料编码查询货位信息
	 * @param defaultCode
	 * @return
	 */
    List<String> queryLocation(String defaultCode);

	/**
	 * 工单处理 根据工单领料单号查询领料出库信息
	 * @param orderOutId
	 * @return
	 */
	StockOutOrder queryStockOutById(String orderOutId);

	/**
	 * 获取领料物料详情
	 * @param valueOf
	 * @return
	 */
    String getProdDetails(String valueOf);

	/**
	 * app工单管理-新增领料保存
	 * @param stockOutOrder
	 * @param stockOutOrderLineList
	 */
	void saveStockOutMessage(StockOutOrder stockOutOrder, List<StockOutOrderLine> stockOutOrderLineList);
}
