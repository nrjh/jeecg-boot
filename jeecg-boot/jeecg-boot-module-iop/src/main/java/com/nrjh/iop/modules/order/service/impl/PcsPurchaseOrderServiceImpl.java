package com.nrjh.iop.modules.order.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.order.entity.BusinessTurnProcess;
import com.nrjh.iop.modules.order.entity.PcsOrderLine;
import com.nrjh.iop.modules.order.entity.PcsPurchaseOrder;
import com.nrjh.iop.modules.order.mapper.BusinessTurnProcessMapper;
import com.nrjh.iop.modules.order.mapper.PcsOrderLineMapper;
import com.nrjh.iop.modules.order.mapper.PcsPurchaseOrderMapper;
import com.nrjh.iop.modules.order.service.IBusinessTurnProcessService;
import com.nrjh.iop.modules.order.service.IPcsPurchaseOrderService;
import com.nrjh.iop.modules.order.vo.PcsPurchaseOrderPage;
import com.nrjh.iop.modules.order.vo.ProdAndStockVo;
import com.nrjh.iop.modules.stk.in.service.IStockInOrderService;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderLineVo;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderVo;
import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.nrjh.iop.modules.stk.move.mapper.StkMoveMapper;
import com.nrjh.iop.modules.stk.move.service.IStkMoveService;
import com.nrjh.iop.modules.stk.picking.entity.StkPicking;
import com.nrjh.iop.modules.stk.picking.mapper.StkPickingMapper;
import com.nrjh.iop.modules.stk.picking.service.IStkPickingService;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @Description: 采购单
 * @Author: jeecg-boot
 * @Date:   2020-08-15
 * @Version: V1.0
 */
@Service
@DS("iop")
public class PcsPurchaseOrderServiceImpl extends ServiceImpl<PcsPurchaseOrderMapper, PcsPurchaseOrder> implements IPcsPurchaseOrderService {

	@Autowired
	private PcsPurchaseOrderMapper pcsPurchaseOrderMapper;
	@Autowired
	private PcsOrderLineMapper pcsOrderLineMapper;

	@Autowired
	private IStkPickingService stkPickingService;
	@Autowired
	private IStkMoveService stkMoveService;
	@Autowired
    private StkPickingMapper stkPickingMapper;

	@Autowired
	private IStockInOrderService stockInOrderService;
	@Autowired
	private IBusinessTurnProcessService iBusinessTurnProcessService;
	@Autowired
	private ISysBaseAPI iSysBaseAPI;


	@Override
    @Transactional(rollbackFor = Exception.class)
	public void saveMain(PcsPurchaseOrder pcsPurchaseOrder, List<ProdAndStockVo> prodAndStockVos,List<String> roleIds) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pcsPurchaseOrder.setCreateBy(sysUser.getUsername());
		pcsPurchaseOrder.setCreateName(sysUser.getRealname());
		pcsPurchaseOrder.setCreateTime(new Date());
		pcsPurchaseOrder.setDateOrder(new Date());
//		if("urgency".equals(pcsPurchaseOrder.getOrderType())){
//			pcsPurchaseOrder.setState("done");
//		}else {
//			pcsPurchaseOrder.setState("draft");
//		}
        pcsPurchaseOrder.setState("draft");
		Integer number_total=0;
		BigDecimal amount_tota = new BigDecimal(0);

		//新增时候 采购计划单号和订单号一样

//		String orderId= OrderNoUtil.getOrderNo("CGDD");
//		pcsPurchaseOrder.setPickingNo(orderId);
		pcsPurchaseOrder.setOrign(pcsPurchaseOrder.getPickingNo());
		pcsPurchaseOrder.setIsDel(0);
//		/**
//		 * 增加审批流程扭转
//		 * 1.自定义审批人的角色业务表business_turn_process
//		 * 2.每次新增获取当前角色（可能多个），加自定义业务表中的顺序号 查询
//		 * 3.新增没人顺序为1，审批的时候自动增一
//		 * 1001 订单审批业务id，business_type
//		 */
//		// 获取当前用户的所有角色id
//			QueryWrapper<BusinessTurnProcess> params=new QueryWrapper<BusinessTurnProcess>();
//			params.eq("BUSINESS_SEQUENCE",2);
//			params.eq("BUSINESS_ID","1001");
//			params.eq("BUSINESS_TYPE",pcsPurchaseOrder.getOrderType());
//			params.orderByAsc("BUSINESS_SEQUENCE");
//			List<BusinessTurnProcess>  businessTurnProcesses=iBusinessTurnProcessService.list(params);
//			if(!CollectionUtil.isEmpty(businessTurnProcesses)){
//				pcsPurchaseOrder.setNextRoleId(businessTurnProcesses.get(0).getRoleId());
//			}else {
//				log.error("查询审批扭转业务流程下一个角色失败@@@@");
//			}
		String nextRoleId=getBusinessApproveNextRoleId(1,"1001",pcsPurchaseOrder.getOrderType());
		pcsPurchaseOrder.setNextRoleId(nextRoleId);
		pcsPurchaseOrder.setBusinessSequence(1);
		pcsPurchaseOrderMapper.insert(pcsPurchaseOrder);
		if(prodAndStockVos!=null && prodAndStockVos.size()>0) {
			for(ProdAndStockVo entity:prodAndStockVos) {
				PcsOrderLine pcsOrderLine=new PcsOrderLine();
				//外键设置
				pcsOrderLine.setOrderId(pcsPurchaseOrder.getId());
                pcsOrderLine.setAttributeCategoryId(entity.getAttributeCategoryId());
                pcsOrderLine.setContract(pcsPurchaseOrder.getContract());
                pcsOrderLine.setDefaultCode(entity.getProductNo());
                pcsOrderLine.setProductId(entity.getProductId());
                pcsOrderLine.setPriceUnit(entity.getPriceUnit());
                pcsOrderLine.setPartnerId(entity.getPartnerId());
                pcsOrderLine.setPriceTotal(entity.getPrice());
				pcsOrderLine.setCreateBy(sysUser.getRealname());
				pcsOrderLine.setCreateTime(new Date());
				pcsOrderLine.setRemark(entity.getRemark());
				pcsOrderLine.setProductName(entity.getRpoductName());
				pcsOrderLine.setCategoryType(entity.getCategoryType());
				pcsOrderLine.setProductUomId(entity.getProductUomId());
				pcsOrderLine.setStatus("draft");
				pcsOrderLine.setProductPlyQty(entity.getProductQty() == null ? 0 : entity.getProductQty());
                number_total= entity.getProductPlyQty() == null ? 0 : entity.getProductPlyQty() + number_total;
                amount_tota= entity.getPrice() == null ? BigDecimal.ZERO : entity.getPrice().add(amount_tota);
				pcsOrderLineMapper.insert(pcsOrderLine);
			}
		}
        PcsPurchaseOrder pc=new PcsPurchaseOrder();
        pc.setId(pcsPurchaseOrder.getId());

        pc.setNumberTotal(number_total);
        pc.setAmountTotal(amount_tota);
        pcsPurchaseOrderMapper.updateById(pc);
        // 紧急单-直接可以入库
//		if("urgency".equals(pcsPurchaseOrder.getOrderType())){
//			urgencyOrderIn(pcsPurchaseOrder);
//		}
	}

	@Override
	@Transactional
	public void updateMain(PcsPurchaseOrder pcsPurchaseOrder,List<ProdAndStockVo> prodAndStockVo) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pcsPurchaseOrderMapper.updateById(pcsPurchaseOrder);
		BigDecimal number_total=new BigDecimal(0);
		BigDecimal amount_tota = new BigDecimal(0);

		//1.先删除子表数据
		pcsOrderLineMapper.deleteByMainId(pcsPurchaseOrder.getId());

		//2.子表数据重新插入
		if(prodAndStockVo!=null && prodAndStockVo.size()>0) {
			for(ProdAndStockVo entity:prodAndStockVo) {
				PcsOrderLine pcsOrderLine=new PcsOrderLine();
				//外键设置
				pcsOrderLine.setOrderId(pcsPurchaseOrder.getId());
				pcsOrderLine.setAttributeCategoryId(entity.getAttributeCategoryId());
				pcsOrderLine.setContract(pcsPurchaseOrder.getContract());
				pcsOrderLine.setDefaultCode(entity.getProductNo());
				pcsOrderLine.setProductId(entity.getProductId());
				pcsOrderLine.setPriceUnit(entity.getPriceUnit());
				pcsOrderLine.setPriceTotal(entity.getPrice());
				pcsOrderLine.setCreateBy(sysUser.getRealname());
				pcsOrderLine.setCreateTime(new Date());
				pcsOrderLine.setRemark(entity.getRemark());
				pcsOrderLine.setProductName(entity.getName());
				pcsOrderLine.setCategoryType(entity.getCategoryType());
				pcsOrderLine.setProductUomId(entity.getProductUomId());
				pcsOrderLine.setPartnerId(entity.getPartnerId());
				pcsOrderLine.setStatus("draft");
				BigDecimal productUomQty = entity.getProductUomQty();
				int qty = productUomQty == null ? 0 : productUomQty.intValue();
				pcsOrderLine.setProductPlyQty(qty);
				number_total = productUomQty == null ? BigDecimal.ZERO : productUomQty.add(number_total);
				BigDecimal price = entity.getPrice();
				amount_tota = price == null ? BigDecimal.ZERO : price.add(amount_tota);
				pcsOrderLineMapper.insert(pcsOrderLine);
			}
		}
		PcsPurchaseOrder pc=new PcsPurchaseOrder();
		pc.setId(pcsPurchaseOrder.getId());

		pc.setNumberTotal(number_total.intValue());
		pc.setAmountTotal(amount_tota);
		pcsPurchaseOrderMapper.updateById(pc);
	}

	@Override
	@Transactional
	public void delMain(String id) {
//		pcsOrderLineMapper.deleteByMainId(id);
		PcsPurchaseOrder pcsPurchaseOrder = new PcsPurchaseOrder();
		pcsPurchaseOrder.setId(id);
		pcsPurchaseOrder.setIsDel(1);
		pcsPurchaseOrderMapper.updateById(pcsPurchaseOrder);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			pcsOrderLineMapper.deleteByMainId(id.toString());
			pcsPurchaseOrderMapper.deleteById(id);
		}
	}

	@Override
	public IPage<PcsPurchaseOrder> pageList(com.baomidou.mybatisplus.extension.plugins.pagination.Page<PcsPurchaseOrder> page, PcsPurchaseOrder pcsPurchaseOrder) {
		IPage<PcsPurchaseOrder> list=pcsPurchaseOrderMapper.selectPage(page,pcsPurchaseOrder);
		return list;
	}

	@Override
	@Transactional
	public void commpleOrderPlan(PcsPurchaseOrder pcsPurchaseOrder,List<ProdAndStockVo> prodAndStockVos) {
		//合并采购计划
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		pcsPurchaseOrder.setCreateBy(sysUser.getUsername());
		pcsPurchaseOrder.setCreateName(sysUser.getRealname());
		pcsPurchaseOrder.setCreateTime(new Date());
		pcsPurchaseOrder.setDateOrder(new Date());
		pcsPurchaseOrder.setOrderType("normal");
		pcsPurchaseOrder.setState("draft");
		Integer number_total=0;
		BigDecimal amount_tota = new BigDecimal(0);
		pcsPurchaseOrder.setOrign(pcsPurchaseOrder.getOrign());
		pcsPurchaseOrder.setIsDel(0);
//		/**
//		 * 增加审批流程扭转
//		 * 1.自定义审批人的角色业务表business_turn_process
//		 * 2.每次新增获取当前角色（可能多个），加自定义业务表中的顺序号 查询
//		 * 3.新增没人顺序为1，审批的时候自动增一
//		 * 1001 订单审批业务id，business_type
//		 */
//		// 获取当前用户的所有角色id
//		QueryWrapper<BusinessTurnProcess> params=new QueryWrapper<BusinessTurnProcess>();
//		params.eq("BUSINESS_SEQUENCE",2);
//		params.eq("BUSINESS_ID","1001");
//		params.eq("BUSINESS_TYPE",pcsPurchaseOrder.getOrderType());
//		params.orderByAsc("BUSINESS_SEQUENCE");
//		List<BusinessTurnProcess>  businessTurnProcesses=iBusinessTurnProcessService.list(params);
//		if(!CollectionUtil.isEmpty(businessTurnProcesses)){
//			pcsPurchaseOrder.setNextRoleId(businessTurnProcesses.get(0).getRoleId());
//		}else {
//			log.error("查询审批扭转业务流程下一个角色失败@@@@");
//		}
		String nextRoleId=getBusinessApproveNextRoleId(2,"1001",pcsPurchaseOrder.getOrderType());
		pcsPurchaseOrder.setNextRoleId(nextRoleId);
		pcsPurchaseOrder.setBusinessSequence(1);

		pcsPurchaseOrderMapper.insert(pcsPurchaseOrder);
		String[] strings=pcsPurchaseOrder.getIds().split(",");
		for(String id:strings){
			PcsPurchaseOrder pc = new PcsPurchaseOrder();
			pc.setId(id);
			pc.setIsDel(1);
			pcsPurchaseOrderMapper.updateById(pc);
		}
		if(prodAndStockVos!=null && prodAndStockVos.size()>0) {
			for(ProdAndStockVo entity:prodAndStockVos) {
				PcsOrderLine pcsOrderLine=new PcsOrderLine();
				//外键设置
				pcsOrderLine.setOrderId(pcsPurchaseOrder.getId());
				pcsOrderLine.setAttributeCategoryId(entity.getAttributeCategoryId());
				pcsOrderLine.setContract(pcsPurchaseOrder.getContract());
				pcsOrderLine.setDefaultCode(entity.getProductNo());
				pcsOrderLine.setProductId(entity.getProductId());
				pcsOrderLine.setPriceUnit(entity.getPriceUnit());
				pcsOrderLine.setPriceTotal(entity.getPrice());
				pcsOrderLine.setCreateBy(sysUser.getRealname());
				pcsOrderLine.setCreateTime(new Date());
				pcsOrderLine.setRemark(entity.getRemark());
				pcsOrderLine.setProductName(entity.getName());
				pcsOrderLine.setCategoryType(entity.getCategoryType());
				pcsOrderLine.setProductUomId(entity.getProductUomId());
				pcsOrderLine.setPartnerId(entity.getProductId());
				pcsOrderLine.setStatus("draft");
				pcsOrderLine.setProductPlyQty(entity.getProductPlyQty() == null ? 0 : entity.getProductPlyQty());
				number_total= entity.getProductPlyQty() == null ? 0 : entity.getProductPlyQty() + number_total;
				amount_tota= entity.getPrice() == null ? BigDecimal.ZERO : entity.getPrice().add(amount_tota);
				pcsOrderLineMapper.insert(pcsOrderLine);
			}
		}
		PcsPurchaseOrder pcUp=new PcsPurchaseOrder();
		pcUp.setId(pcsPurchaseOrder.getId());
		pcUp.setNumberTotal(number_total);
		pcUp.setAmountTotal(amount_tota);
		pcsPurchaseOrderMapper.updateById(pcUp);

		//修改采购计划单状态
		String[] pickingNos=pcsPurchaseOrder.getOrign().split(",");
		for(String pickingNo : pickingNos){
			if(pickingNo != null){
				StkPicking stkPicking =new StkPicking();
				stkPicking.setState("done");
				stkPicking.setPickingNo(pickingNo);
				stkPickingMapper.updateByPickingNo(stkPicking);

			}
		}

	}

	@Override
	public IPage<PcsPurchaseOrder> selectPageOrders(IPage iPage, Map<String,Object> dataMap) {
		return pcsPurchaseOrderMapper.selectPageOrders(iPage, dataMap);
	}

	@Override
	public String getBusinessApproveNextRoleId(int businessSequence, String businessId, String businessType) {
		/**
		 * 增加审批流程扭转
		 * 1.自定义审批人的角色业务表business_turn_process
		 * 2.每次新增获取当前角色（可能多个），加自定义业务表中的顺序号 查询
		 * 3.新增没人顺序为1，审批的时候自动增一
		 * 1001 订单审批业务id，business_type
		 */
		// 获取当前用户的所有角色id
		QueryWrapper<BusinessTurnProcess> params=new QueryWrapper<BusinessTurnProcess>();
		params.eq("BUSINESS_SEQUENCE",businessSequence+1);
		params.eq("BUSINESS_ID",businessId);
		params.eq("BUSINESS_TYPE",businessType);
		params.orderByAsc("BUSINESS_SEQUENCE");
		String nextRoleId="";
		List<BusinessTurnProcess>  businessTurnProcesses=iBusinessTurnProcessService.list(params);
		if(!CollectionUtil.isEmpty(businessTurnProcesses)){
			 nextRoleId=businessTurnProcesses.get(0).getRoleId();
		}else {
			log.error("查询审批扭转业务流程下一个角色结束@@@@");
			nextRoleId="";
		}
		return nextRoleId;
	}

	public  void  urgencyOrderIn(PcsPurchaseOrder pcsPurchaseOrder){
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		//审核通过插入入库单信息
		StockInOrderVo stockInOrderVo=new StockInOrderVo();
		List<StockInOrderLineVo> stockInOrderLineList=new ArrayList<>();
		stockInOrderVo.setContract(pcsPurchaseOrder.getContract());
		stockInOrderVo.setCreateInBy(sysUser.getUsername());
		stockInOrderVo.setCreateInName(sysUser.getRealname());
		stockInOrderVo.setCreateTime(new Date());
		try {
			stockInOrderVo.setDatePlanned(DateUtils.parseDate(pcsPurchaseOrder.getDatePlanned(),"yyyy-MM-dd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		stockInOrderVo.setOrderType(pcsPurchaseOrder.getOrderType());
		stockInOrderVo.setPartnerId(pcsPurchaseOrder.getPartnerId());
		stockInOrderVo.setPartnerName(pcsPurchaseOrder.getPartnerName());
		stockInOrderVo.setPickingNo(pcsPurchaseOrder.getPickingNo());
		stockInOrderVo.setState("draft");
		List<PcsOrderLine> pcsOrderLineList = pcsOrderLineMapper.selectByMainId(pcsPurchaseOrder.getId());
		for(PcsOrderLine line:pcsOrderLineList){
			StockInOrderLineVo stockInOrderLine=new StockInOrderLineVo();
			stockInOrderLine.setAttributeCategoryId(line.getAttributeCategoryId());
			stockInOrderLine.setCategoryType(String.valueOf(line.getCategoryType()));
			stockInOrderLine.setContract(pcsPurchaseOrder.getContract());
			stockInOrderLine.setCreateTime(new Date());
			stockInOrderLine.setDefaultCode(line.getDefaultCode());
			stockInOrderLine.setPartnerId(line.getPartnerId());
			stockInOrderLine.setPartnerName(line.getPartnerName());
			stockInOrderLine.setProductId(line.getProductId());
			stockInOrderLine.setProductName(line.getProductName());
			stockInOrderLine.setProductUomId(line.getProductUomId());
			stockInOrderLine.setProductPlyQty(new BigDecimal(line.getProductPlyQty()));
			stockInOrderLine.setPriceUnit(line.getPriceUnit());
			stockInOrderLineList.add(stockInOrderLine);
		}
		stockInOrderVo.setStockInOrderLineList(stockInOrderLineList);
		StringBuffer sb = new StringBuffer();
		sb.append("CGRK").append(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
		stockInOrderVo.setOrderInId(sb.toString());

		stockInOrderService.saveMain(stockInOrderVo);

	}
}
