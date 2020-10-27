package com.nrjh.iop.modules.stk.picking.service.impl;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.order.entity.PcsOrderLine;
import com.nrjh.iop.modules.order.entity.PcsPurchaseOrder;
import com.nrjh.iop.modules.order.mapper.PcsOrderLineMapper;
import com.nrjh.iop.modules.order.mapper.PcsPurchaseOrderMapper;
import com.nrjh.iop.modules.order.vo.OrderPlanVo;
import com.nrjh.iop.modules.order.vo.ProdAndStockVo;
import com.nrjh.iop.modules.stk.location.entity.StkLocation;
import com.nrjh.iop.modules.stk.location.mapper.StkLocationMapper;
import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.nrjh.iop.modules.stk.move.mapper.StkMoveMapper;
import com.nrjh.iop.modules.stk.move.service.IStkMoveService;
import com.nrjh.iop.modules.stk.picking.entity.StkPicking;
import com.nrjh.iop.modules.stk.picking.mapper.StkPickingMapper;
import com.nrjh.iop.modules.stk.picking.service.IStkPickingService;
import com.nrjh.iop.modules.stk.pickingtype.entity.StkPickingType;
import com.nrjh.iop.modules.stk.pickingtype.service.IStkPickingTypeService;
import com.nrjh.iop.modules.stk.warehouse.entity.StkWarehouse;
import com.nrjh.iop.modules.stk.warehouse.mapper.StkWarehouseMapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 调拨单
 * @Author: jeecg-boot
 * @Date:   2020-05-18
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkPickingServiceImpl extends ServiceImpl<StkPickingMapper, StkPicking> implements IStkPickingService {

	@Autowired
	private StkPickingMapper stkPickingMapper;
	@Autowired
	private StkMoveMapper stkMoveMapper;
	@Autowired
	private StkLocationMapper stkLocationMapper;
	@Autowired
	private StkWarehouseMapper stkWarehouseMapper;

	@Autowired
	private PcsPurchaseOrderMapper pcsPurchaseOrderMapper;

	@Autowired
	private IStkPickingTypeService stkPickingTypeService;

	@Autowired
	private IStkMoveService stkMoveService;

	@Autowired
	private PcsOrderLineMapper pcsOrderLineMapper;

	@Override
	@Transactional
	public void saveMain(StkPicking stkPicking, List<StkMove> stkMoveList) {
		stkPickingMapper.insert(stkPicking);
		// 调拨单生成是  创建移库单
		if(stkMoveList!=null && stkMoveList.size()>0) {
            Integer warehouseId = getWareHouseByDestId(stkPicking.getLocationDestId());
			for(StkMove entity:stkMoveList) {
				//外键设置
				entity.setPickingId(stkPicking.getId());
				// 属性继承
				entity.setPickingTypeId(stkPicking.getPickingTypeId());
				entity.setState(stkPicking.getState());
				entity.setCompanyId(stkPicking.getCompanyId());
				entity.setWarehouseId(warehouseId);
				entity.setLocationSrcId(stkPicking.getLocationSrcId());
				entity.setLocationDestId(stkPicking.getLocationDestId());
				entity.setDateExpected(stkPicking.getScheduledDate());
				entity.setDateDone(stkPicking.getDateDone());
				entity.setOrigin(stkPicking.getOrign());
				entity.setNote(stkPicking.getNote());
				stkMoveMapper.insert(entity);
			}
		}
	}



	@Override
	@Transactional
	public void updateMain(StkPicking stkPicking,List<StkMove> stkMoveList) {
		stkPickingMapper.updateById(stkPicking);

		//1.先删除子表数据
		stkMoveMapper.deleteByMainId(stkPicking.getId().toString());

		//2.子表数据重新插入
		if(stkMoveList!=null && stkMoveList.size()>0) {
			Integer warehouseId = getWareHouseByDestId(stkPicking.getLocationDestId());
			for(StkMove entity:stkMoveList) {
				//外键设置
				entity.setPickingId(stkPicking.getId());
				// 属性继承
				entity.setPickingTypeId(stkPicking.getPickingTypeId());
				entity.setState(stkPicking.getState());
				entity.setCompanyId(stkPicking.getCompanyId());
				entity.setWarehouseId(warehouseId);
				entity.setLocationSrcId(stkPicking.getLocationSrcId());
				entity.setLocationDestId(stkPicking.getLocationDestId());
				entity.setDateExpected(stkPicking.getScheduledDate());
				entity.setDateDone(stkPicking.getDateDone());
				entity.setOrigin(stkPicking.getOrign());
				entity.setNote(stkPicking.getNote());
				stkMoveMapper.insert(entity);
			}
		}
	}

	private Integer getWareHouseByDestId(Integer destId ){
		StkLocation stkLocation = stkLocationMapper.selectById(destId);
		String[] destArr = stkLocation.getParentPath().split("/");
		if(destArr!=null&&destArr.length>1){
			Integer viewLocationId = Integer.parseInt(destArr[1]);
			StkWarehouse stkWarehouse = stkWarehouseMapper.selectByViewLocationId(viewLocationId);
			return stkWarehouse.getId();
		}else{
			return null;
		}
	}


	@Override
	@Transactional
	public void delMain(String id) {
		//删除调拨单数据
		StkPicking stkPicking = new StkPicking();
		stkPicking.setId(Integer.valueOf(id));
		stkPicking.setIsDel(1);
		stkPickingMapper.updateById(stkPicking);

		//删除采购订单信息
		StkPicking stk = stkPickingMapper.selectById(id);
		QueryWrapper<PcsPurchaseOrder> pcsPurchaseOrderQueryWrapper = new QueryWrapper<>();
		pcsPurchaseOrderQueryWrapper.eq("orign",stk.getPickingNo());
		PcsPurchaseOrder pcsPurchaseOrder = new PcsPurchaseOrder();
		pcsPurchaseOrder.setIsDel(1);
		pcsPurchaseOrderMapper.update(pcsPurchaseOrder,pcsPurchaseOrderQueryWrapper);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			stkMoveMapper.deleteByMainId(id.toString());
			stkPickingMapper.deleteById(id);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrderPlan(OrderPlanVo orderPlanVo) throws BusinessException {
		try{
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			int pickingTypeId=0;
			Map<String, Object> var1=new HashMap<String, Object>();
			var1.put("code","orderplan");
			Collection<StkPickingType> list= stkPickingTypeService.listByMap(var1);
			StkPicking stkPicking =new StkPicking();
			BeanUtils.copyProperties(orderPlanVo, stkPicking);
//			stkPicking.setCreateBy(sysUser.getRealname());
			stkPicking.setState("draft");
			if(!CollectionUtils.isEmpty(list)){
				for(StkPickingType stkPickingType:list){
					pickingTypeId=stkPickingType.getId();
					break;
				}
			}
			/**
			 * todo 班组 车间组织信息
			 */
//			stkPicking.setGroupNo("");
//			String pickingNo= OrderNoUtil.getOrderNo("CGJH");
//			stkPicking.setPickingNo(pickingNo);
			stkPicking.setPickingTypeId(pickingTypeId);
			stkPicking.setIsDel(0);
			this.save(stkPicking);
			//采购订单
			PcsPurchaseOrder purchased = new PcsPurchaseOrder();
			//创建人
			purchased.setCreateName(stkPicking.getCreateBy() == null ? sysUser.getRealname() : stkPicking.getCreateBy());
			purchased.setCreateBy(stkPicking.getCreateBy() == null ? sysUser.getRealname() : stkPicking.getCreateBy());
			//创建时间
			purchased.setCreateTime(stkPicking.getCreateTime() == null ? new Date() : stkPicking.getCreateTime());
			//修改时间
			purchased.setUpdateBy(stkPicking.getUpdateBy() == null ? sysUser.getRealname() : stkPicking.getUpdateBy());
			purchased.setUpdateName(stkPicking.getUpdateBy() == null ? sysUser.getRealname() : stkPicking.getUpdateBy());
			//修改时间
			purchased.setUpdateTime(stkPicking.getUpdateTime() == null ? new Date() :  stkPicking.getUpdateTime());
			//订单编号
			//purchased.setPickingNo(stkPicking.getPickingNo() == null ? "" : stkPicking.getPickingNo());
			//采购计划-编号
			purchased.setOrign(stkPicking.getPickingNo() == null ? "" : stkPicking.getPickingNo());
			//预计到货时间
			purchased.setDatePlanned(DateUtils.formatDate(stkPicking.getScheduledDate() == null ? new Date() : stkPicking.getScheduledDate()));
			//下单时间
			purchased.setDateOrder(stkPicking.getCreateTime() == null ? new Date() : stkPicking.getCreateTime());
			//单据级别
			purchased.setOrderType(stkPicking.getOrderType() == null ? "normal" : stkPicking.getOrderType());
			//状态
			purchased.setState(stkPicking.getState() == null ? "draft" : stkPicking.getState());
			//数量
			//purchased.setNumberTotal(stkPicking.getSumPrice() == null ? 0 : stkPicking.getPrice() == null ? 0 : stkPicking.getSumPrice().divide(stkPicking.getPrice()).intValue());
			purchased.setIsDel(0);
			pcsPurchaseOrderMapper.insert(purchased);
			log.debug("pickingNo:"+stkPicking.getPickingNo());
			log.debug("采购计划id:"+stkPicking.getId());
			int number = 0;
			for (ProdAndStockVo prodVo: orderPlanVo.getPrdProduct()){

				StkMove stkMove=new StkMove();
				stkMove.setPickingId(stkPicking.getId());
				stkMove.setCreateBy(sysUser.getRealname());
				stkMove.setCreateTime(new Date());
				stkMove.setState("draft");
				stkMove.setPickingTypeId(pickingTypeId);
				stkMove.setProductUomQty(prodVo.getProductUomQty());
				stkMove.setName(prodVo.getRpoductName());
				stkMove.setVendorId(prodVo.getPartnerId());
				if (prodVo.getProductId() == null){
					continue;
				}
				stkMove.setProductId(String.valueOf(prodVo.getProductId()));
				stkMove.setProductUomId(prodVo.getProductUomId());
				if (purchased.getPartnerId() == null){
					purchased.setPartnerId(prodVo.getProductId() == null ? null : prodVo.getProductId());
				}
				int num = stkMove.getProductUomQty() == null ? 0 : stkMove.getProductUomQty().intValue();
				number += num;
				stkMoveService.save(stkMove);
				//外键设置
				PcsOrderLine pcsOrderLine = new PcsOrderLine();
				BeanUtils.copyProperties(prodVo,pcsOrderLine);
				pcsOrderLine.setOrderId(purchased.getId());
				pcsOrderLine.setContract(purchased.getContract());
				pcsOrderLine.setDefaultCode(prodVo.getProductId().toString());
				pcsOrderLine.setProductId(prodVo.getProductId());
				pcsOrderLine.setPartnerId(prodVo.getPartnerId());
				pcsOrderLine.setCreateBy(sysUser.getRealname());
				pcsOrderLine.setCreateTime(new Date());
				pcsOrderLine.setProductName(prodVo.getName());
				pcsOrderLine.setProductUomId(prodVo.getProductUomId());
				pcsOrderLine.setStatus("draft");
				if(prodVo.getProductUomQty()!=null){
					pcsOrderLine.setProductPlyQty(prodVo.getProductUomQty().intValue());
				}
				pcsOrderLineMapper.insert(pcsOrderLine);

				//插入物品和规格对应信息
                stkPickingMapper.setCateGory(prodVo);
			}
			purchased.setNumberTotal(number);
			//总价
			purchased.setAmountTotal(stkPicking.getSumPrice() == null ? BigDecimal.ZERO : stkPicking.getSumPrice().multiply(new BigDecimal(purchased.getNumberTotal())));
			pcsPurchaseOrderMapper.updateById(purchased);
		}catch (Exception ex){
			ex.printStackTrace();
			throw new BusinessException("采购计划保存业务异常");
		}

	}

	@Override
	public String getProdDetails(String orderPlanId) {
		List<Map<String,Object>>  list=stkPickingMapper.getProdDetails(orderPlanId);
		StringBuffer resultBuff=new StringBuffer();
		for (int i=0;i<list.size();i++){
			Map<String,Object> result=list.get(i);
			if(!CollectionUtils.isEmpty(result)){
				resultBuff.append(result.get("prodName") == null ? "" : result.get("prodName"));
				resultBuff.append("-");
				if(result.get("cateName")!=null){
					resultBuff.append(result.get("cateName") == null ? "" : result.get("cateName"));
					resultBuff.append("-");
				}
				if(result.get("product_uom_qty")!=null){
					resultBuff.append(result.get("product_uom_qty"));
				}else {
					resultBuff.append(0);
				}
                if(result.get("uuName")!=null){
                    resultBuff.append(result.get("uuName"));
                }
				resultBuff.append(";");
			}
		}
		if(StringUtils.isEmpty(String.valueOf(resultBuff))){
			return "";
		}else {
			return resultBuff.substring(0,resultBuff.length()-1);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateOrderPlan(OrderPlanVo orderPlanVo) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		StkPicking stkPicking =new StkPicking();
		BeanUtils.copyProperties(orderPlanVo, stkPicking);
		stkPicking.setUpdateBy(sysUser.getRealname());
		stkPicking.setUpdateTime(new Date());
		this.updateById(stkPicking);

		//1.先删除子表数据
		stkMoveMapper.deleteByMainId(stkPicking.getId().toString());

		for (ProdAndStockVo prodVo: orderPlanVo.getPrdProduct()){
			StkMove stkMove=new StkMove();
			stkMove.setPickingId(stkPicking.getId());
			stkMove.setCreateBy(sysUser.getRealname());
			stkMove.setCreateTime(new Date());
			stkMove.setState("draft");
			stkMove.setPickingTypeId(stkPicking.getPickingTypeId());
			stkMove.setProductUomQty(prodVo.getProductUomQty());
			stkMove.setName(prodVo.getName());
			stkMove.setProductId(String.valueOf(prodVo.getProductId()));
			stkMove.setProductUomId(prodVo.getProductUomId());
			stkMove.setVendorId(prodVo.getPartnerId());
			stkMoveService.save(stkMove);
		}
		//根据采购计划单号查询是否生成采购订单
		QueryWrapper<PcsPurchaseOrder> pcsPurchaseOrderQueryWrapper = new QueryWrapper<>();
		pcsPurchaseOrderQueryWrapper.eq("orign",orderPlanVo.getPickingNo());
		PcsPurchaseOrder pcsPurchaseOrder = pcsPurchaseOrderMapper.selectOne(pcsPurchaseOrderQueryWrapper);
		if(pcsPurchaseOrder.getPickingNo()==null){
			//修改采购订单
			PcsPurchaseOrder pcsPurOrder = new PcsPurchaseOrder();
			pcsPurOrder.setUpdateBy(sysUser.getUsername());
			pcsPurOrder.setUpdateName(sysUser.getRealname());
			pcsPurOrder.setUpdateTime(new Date());
			pcsPurOrder.setDatePlanned(DateUtils.formatDate(stkPicking.getScheduledDate()));
			pcsPurOrder.setOrderType(orderPlanVo.getOrderType());
			pcsPurOrder.setNote(orderPlanVo.getNote());
			pcsPurOrder.setId(pcsPurchaseOrder.getId());
			pcsPurchaseOrderMapper.updateById(pcsPurOrder);
//			//修改采购订单明细
//			PcsOrderLine pcsOrderLine = new PcsOrderLine();
//			for(ProdAndStockVo prodAndStockVo : orderPlanVo.getPrdProduct()){
//				BeanUtils.copyProperties(prodAndStockVo, pcsOrderLine);
//				pcsOrderLine.setPartnerId(prodAndStockVo.getVendorId());
//				pcsOrderLine.setProductName(prodAndStockVo.getRpoductName());
//				pcsOrderLineMapper
//			}
		}
	}

	@Override
	public List<ProdAndStockVo> queryProdList(ProdAndStockVo vo) {
		return stkPickingMapper.queryProdList(vo);
	}

	@Override
	public StkPicking getStkPickingById(String id) {
		return stkPickingMapper.getStkPickingById(id);
	}

	@Override
	public IPage<StkPicking> selectStkPickingPage(IPage ipage, StkPicking stkPicking) {
		return stkPickingMapper.selectStkPickingPage(ipage,stkPicking);
	}

}
