package com.nrjh.iop.modules.stk.picking.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.order.vo.OrderPlanVo;
import com.nrjh.iop.modules.order.vo.ProdAndStockVo;
import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.nrjh.iop.modules.stk.picking.entity.StkPicking;
import org.jeecg.modules.online.config.exception.BusinessException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 调拨单
 * @Author: jeecg-boot
 * @Date:   2020-05-18
 * @Version: V1.0
 */
public interface IStkPickingService extends IService<StkPicking> {

	/**
	 * 添加一对多
	 * 
	 */
	 void saveMain(StkPicking stkPicking, List<StkMove> stkMoveList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	 void updateMain(StkPicking stkPicking, List<StkMove> stkMoveList);
	
	/**
	 * 删除一对多
	 */
	 void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	 void delBatchMain(Collection<? extends Serializable> idList);


	/**
	 * 采购计划管理 保存采购计划
	 */
	 void saveOrderPlan(OrderPlanVo orderPlanVo) throws BusinessException;

	 String getProdDetails(String orderPlanId);

	/**
	 * 修改采购计划
	 * @param orderPlanVo
	 */
	 void updateOrderPlan(OrderPlanVo orderPlanVo);

	//查询物料信息列表
	List<ProdAndStockVo> queryProdList(ProdAndStockVo vo);

	/**
	 * 传采购计划id
	 * @return
	 */
	StkPicking getStkPickingById(String id);

	IPage<StkPicking> selectStkPickingPage(IPage ipage,StkPicking stkPicking);
}
