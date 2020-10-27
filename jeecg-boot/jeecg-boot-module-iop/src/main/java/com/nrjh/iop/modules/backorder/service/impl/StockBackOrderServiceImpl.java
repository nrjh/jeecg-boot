package com.nrjh.iop.modules.backorder.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.backorder.entity.StockBackOrder;
import com.nrjh.iop.modules.backorder.entity.StockBackOrderLine;
import com.nrjh.iop.modules.backorder.mapper.StockBackOrderLineMapper;
import com.nrjh.iop.modules.backorder.mapper.StockBackOrderMapper;
import com.nrjh.iop.modules.backorder.service.IStockBackOrderService;
import com.nrjh.iop.modules.order.vo.ProdAndStockVo;
import com.nrjh.iop.modules.stk.in.entity.OrderStockInList;
import com.nrjh.iop.modules.stk.in.mapper.OrderStockInListMapper;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderLineVo;
import com.nrjh.iop.modules.stk.spareparts.entity.StkStock;
import com.nrjh.iop.modules.stk.spareparts.service.IStkStockService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Description: 退库单
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StockBackOrderServiceImpl extends ServiceImpl<StockBackOrderMapper, StockBackOrder> implements IStockBackOrderService {

	@Autowired
	private StockBackOrderMapper stockBackOrderMapper;
	@Autowired
	private StockBackOrderLineMapper stockBackOrderLineMapper;

	@Autowired
	private OrderStockInListMapper orderStockInListMapper;

	@Autowired
	private IStkStockService  iStkStockService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(StockBackOrder stockBackOrder, List<ProdAndStockVo> prodAndStockVoList) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		stockBackOrder.setCreateOutBy(sysUser.getUsername());
		stockBackOrder.setCreateOutName(sysUser.getRealname());
		if("1".equals(stockBackOrder.getSaveFlag())){
			stockBackOrder.setState("draft");
		}else if ("2".equals(stockBackOrder.getSaveFlag())){
			stockBackOrder.setState("submit");
		}
//		stockBackOrder.setOrderBackId(OrderNoUtil.getOrderNo("THRK"));
		stockBackOrderMapper.insert(stockBackOrder);
		if(prodAndStockVoList!=null && prodAndStockVoList.size()>0) {
			for(ProdAndStockVo entity:prodAndStockVoList) {
				StockBackOrderLine stockBackOrderLine=new StockBackOrderLine();
				stockBackOrderLine.setOrderId(stockBackOrder.getId());
				stockBackOrderLine.setAttributeCategoryId(entity.getAttributeCategoryId());
				stockBackOrderLine.setDefaultCode(entity.getProductNo());
				stockBackOrderLine.setProductId(entity.getProductId());
				stockBackOrderLine.setProductName(entity.getName());
				stockBackOrderLine.setCategoryType(String.valueOf(entity.getCategoryType()));
				stockBackOrderLine.setProductUomId(entity.getProductUomId());
				stockBackOrderLine.setProductActualQty(entity.getProductActualQty());
				//todo 合同号  新增的退回入库单 时间数量=应入数量  正常入库  完成时间就是新建时间
				stockBackOrderLine.setProductQty(entity.getProductQty());
				stockBackOrderLine.setStatus("normal");
				stockBackOrderLine.setCreateTime(new Date());
				// 退回的数量
				stockBackOrderLine.setReturnCount(entity.getReturnCount());
				stockBackOrderLineMapper.insert(stockBackOrderLine);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(StockBackOrder stockBackOrder, List<ProdAndStockVo> prodAndStockVoList) {

		if("1".equals(stockBackOrder.getSaveFlag())){
			stockBackOrder.setState("draft");
		}else if ("2".equals(stockBackOrder.getSaveFlag())){
			stockBackOrder.setState("submit");
		}
		stockBackOrderMapper.updateById(stockBackOrder);

		//1.先删除子表数据
		stockBackOrderLineMapper.deleteByMainId(stockBackOrder.getId());

		//2.子表数据重新插入
		if(prodAndStockVoList!=null && prodAndStockVoList.size()>0) {
			for(ProdAndStockVo entity:prodAndStockVoList) {
				StockBackOrderLine stockBackOrderLine=new StockBackOrderLine();
				stockBackOrderLine.setOrderId(stockBackOrder.getId());
				stockBackOrderLine.setAttributeCategoryId(entity.getAttributeCategoryId());
				stockBackOrderLine.setDefaultCode(entity.getProductNo());
				stockBackOrderLine.setProductId(entity.getProductId());
				stockBackOrderLine.setProductName(entity.getName());
				stockBackOrderLine.setCategoryType(String.valueOf(entity.getCategoryId()));
				stockBackOrderLine.setProductUomId(entity.getProductUomId());
				stockBackOrderLine.setProductActualQty(entity.getProductActualQty());
				stockBackOrderLine.setReturnCount(entity.getReturnCount());
				//todo 合同号  新增的退回入库单 时间数量=应入数量  正常入库  完成时间就是新建时间
				stockBackOrderLine.setProductQty(entity.getProductQty());
				stockBackOrderLine.setStatus("normal");
				stockBackOrderLineMapper.insert(stockBackOrderLine);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(Integer id) {
		stockBackOrderLineMapper.deleteByMainId(id);
		stockBackOrderMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			stockBackOrderLineMapper.deleteByMainId(Integer.parseInt(id.toString()));
			stockBackOrderMapper.deleteById(id);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveConfirm(List<StockInOrderLineVo> stockInOrderLineList) {
		int id=0;
		if (stockInOrderLineList != null && stockInOrderLineList.size() > 0) {
			for (StockInOrderLineVo oEntity : stockInOrderLineList) {
				for(OrderStockInList stockInList: oEntity.getOrderStockInLists()){
					id=oEntity.getOrderId();
					stockInList.setStockOutOrderId(oEntity.getId());
					stockInList.setStockOrderId(oEntity.getOrderId()+"");
					stockInList.setOperType("out");
					stockInList.setStatus("1");
					stockInList.setProductActualQty(oEntity.getProductActualQty());
					orderStockInListMapper.insert(stockInList);
				}
				StockBackOrderLine stockBackOrderLine=new StockBackOrderLine();
				stockBackOrderLine.setId(oEntity.getId());
				stockBackOrderLine.setCreateTime(new Date());
				stockBackOrderLine.setStatus("done");
				stockBackOrderLineMapper.updateById(stockBackOrderLine);

				//退回入库确认 更新库存数量
				QueryWrapper<StkStock> queryWrapper=new QueryWrapper<>();
				queryWrapper.eq("product_id",oEntity.getProductId());
				StkStock stkStockOld=iStkStockService.getOne(queryWrapper);
				if(oEntity.getProductPlyQty()!=null){
					stkStockOld.setProductQty(stkStockOld.getProductQty()+oEntity.getProductPlyQty().intValue());
				}
				iStkStockService.updateById(stkStockOld);
			}
		}
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		StockBackOrder stockBackOrder=new StockBackOrder();
		stockBackOrder.setId(id);
		stockBackOrder.setState("done");
		stockBackOrder.setInBy(sysUser.getUsername());
		stockBackOrder.setInName(sysUser.getRealname());
		stockBackOrder.setInState("done");
		stockBackOrder.setInTime(new Date());
		stockBackOrderMapper.updateById(stockBackOrder);




	}

	@Override
	public void updateBack(Integer id) {
		StockBackOrder stockBackOrder=new StockBackOrder();
		stockBackOrder.setId(id);
		stockBackOrder.setState("back");
		stockBackOrder.setInState("back");
		stockBackOrderMapper.updateById(stockBackOrder);
	}

}
