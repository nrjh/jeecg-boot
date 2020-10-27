package com.nrjh.iop.modules.order.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.order.entity.BusinessTurnProcess;
import com.nrjh.iop.modules.order.entity.PcsOrderLine;
import com.nrjh.iop.modules.order.entity.PcsPurchaseOrder;
import com.nrjh.iop.modules.order.entity.StkSuggest;
import com.nrjh.iop.modules.order.service.IBusinessTurnProcessService;
import com.nrjh.iop.modules.order.service.IPcsOrderLineService;
import com.nrjh.iop.modules.order.service.IPcsPurchaseOrderService;
import com.nrjh.iop.modules.order.service.IStkSuggestService;
import com.nrjh.iop.modules.order.vo.PcsPurchaseOrderPage;
import com.nrjh.iop.modules.order.vo.ProdAndStockVo;
import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttributeCategory;
import com.nrjh.iop.modules.prd.attrcategory.service.IPrdAttributeCategoryService;
import com.nrjh.iop.modules.stk.in.service.IStockInOrderService;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderLineVo;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderVo;
import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.nrjh.iop.modules.stk.move.service.IStkMoveService;
import com.nrjh.iop.modules.stk.stkscrap.entity.ScrapPro;
import com.nrjh.iop.modules.stk.stkscrap.service.IScrapProService;
import com.nrjh.iop.modules.uom.category.entity.UomUom;
import com.nrjh.iop.modules.uom.category.service.IUomUomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @Description: 采购单
 * @Author: jeecg-boot
 * @Date:   2020-08-15
 * @Version: V1.0
 */
@Api(tags="采购单")
@RestController
@RequestMapping("/order/pcsPurchaseOrder")
@Slf4j
public class PcsPurchaseOrderController {
	private static final String STK_SUGGEST_RESULT = "reject";
	@Autowired
	private IPcsPurchaseOrderService pcsPurchaseOrderService;
	@Autowired
	private IPcsOrderLineService pcsOrderLineService;

	@Autowired
	private IStkSuggestService stkSuggestService;

	@Autowired
	private IStkMoveService stkMoveService;

	@Autowired
	private IStockInOrderService stockInOrderService;

	@Autowired
	private IPrdAttributeCategoryService iPrdAttributeCategoryService;

	@Autowired
	private IUomUomService iUomUomService;

	@Autowired
	private IScrapProService scrapProService;

	@Autowired
	private IBusinessTurnProcessService iBusinessTurnProcessService;
	@Autowired
	private ISysBaseAPI iSysBaseAPI;

	/**
	 * 分页列表查询
	 *
	 * @param pcsPurchaseOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采购单-分页列表查询")
	@ApiOperation(value="采购单-分页列表查询", notes="采购单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PcsPurchaseOrder pcsPurchaseOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<PcsPurchaseOrder> page = new Page<>(pageNo, pageSize);
		Map<String, String[]> parameterMap = req.getParameterMap();
		Map<String,Object> dataMap = new HashMap<>(256);
		parameterMap.forEach((key, value) -> {
			dataMap.put(key,value[0]);
		});
		IPage<PcsPurchaseOrder> pcsPurchaseOrderIPage = pcsPurchaseOrderService.selectPageOrders(page,dataMap);
		if (CollectionUtils.isEmpty(pcsPurchaseOrderIPage.getRecords())){
			return Result.ok(pcsPurchaseOrderIPage);
		}
		//加载采购详情
		pcsPurchaseOrderIPage.getRecords().forEach(item -> {
			//订单编号为空到采购计划表查询
			if (!StringUtils.isEmpty(item.getPickingNo())) {
				QueryWrapper<PcsOrderLine> queryWrapper2 = new QueryWrapper<>();
				queryWrapper2.eq("order_id", item.getId());
				//获取采购详情实体类
				List<PcsOrderLine> pcsOrderLineList = pcsOrderLineService.list(queryWrapper2);
				if (pcsOrderLineList != null) {
					String text = "";
					for (PcsOrderLine pcsOrderLine : pcsOrderLineList) {
						//规格
						PrdAttributeCategory category = iPrdAttributeCategoryService.getById(pcsOrderLine.getAttributeCategoryId());
						//单位
						UomUom uom = iUomUomService.getById(pcsOrderLine.getProductUomId());
						text.append(pcsOrderLine.getProductName() == null ? "" + "[ " : pcsOrderLine.getProductName() + "[ ");
						text.append(category == null ? "" + " ]-" : category.getName() + " ]-");
						text.append(item.getNumberTotal() == null ? "" : item.getNumberTotal());
						text.append(uom == null ? ";\t\n" : uom.getName() +";\t\n");
					}
					item.setOrignContent(text);
				}
			}else{
				StringBuffer resultBuff=new StringBuffer();
				List<StkMove> stkMove = stkMoveService.selectStkMoveByOrign(item.getOrign());
				stkMove.forEach( move->{
					String text = "";
					if (move != null){
						//规格
						PrdAttributeCategory category = iPrdAttributeCategoryService.selectAttributeByProductId(move.getProductId());
						//单位
						UomUom uom = iUomUomService.getById(move.getProductUomId());
						text += move.getName() == null ? "" + "- [" : move.getName() + "- [";
						text += category == null ? "" + " ]" : category.getName() + " ]";
						text += item.getNumberTotal() == null ? "" : item.getNumberTotal();
						text += uom == null ? "" + "；\n" : uom.getName() + "；\n";
						resultBuff.append(text);
	//					item.setOrignContent(move.getProductQty() == null ? "" : move.getName() + " [ " + (category == null ? "" : category.getName() == null ? "" : category.getName()) + " ]" + "," + item.getNumberTotal() + (uom == null ? "" : uom.getName() == null ? "" : uom.getName()));
					}
				});
				item.setOrignContent(String.valueOf(resultBuff));
			}
		});
		return Result.ok(pcsPurchaseOrderIPage);
	}

	/**
	 *   添加
	 *
	 * @param pcsPurchaseOrderPage
	 * @return
	 */
	@AutoLog(value = "采购单-添加")
	@ApiOperation(value="采购单-添加", notes="采购单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PcsPurchaseOrderPage pcsPurchaseOrderPage) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		PcsPurchaseOrder pcsPurchaseOrder = new PcsPurchaseOrder();
		BeanUtils.copyProperties(pcsPurchaseOrderPage, pcsPurchaseOrder);
		List<String> roleIds=iSysBaseAPI.getRoleIdsByUsername(sysUser.getUsername());
		pcsPurchaseOrderService.saveMain(pcsPurchaseOrder, pcsPurchaseOrderPage.getPcsOrderLineList(),roleIds);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param pcsPurchaseOrderPage
	 * @return
	 */
	@AutoLog(value = "采购单-编辑")
	@ApiOperation(value="采购单-编辑", notes="采购单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PcsPurchaseOrderPage pcsPurchaseOrderPage) {
		PcsPurchaseOrder pcsPurchaseOrder = new PcsPurchaseOrder();
		BeanUtils.copyProperties(pcsPurchaseOrderPage, pcsPurchaseOrder);
		PcsPurchaseOrder pcsPurchaseOrderEntity = pcsPurchaseOrderService.getById(pcsPurchaseOrder.getId());
		if(pcsPurchaseOrderEntity==null) {
			return Result.error("未找到对应数据");
		}
		pcsPurchaseOrderService.updateMain(pcsPurchaseOrder, pcsPurchaseOrderPage.getPcsOrderLineList());
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购单-通过id删除")
	@ApiOperation(value="采购单-通过id删除", notes="采购单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pcsPurchaseOrderService.delMain(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购单-批量删除")
	@ApiOperation(value="采购单-批量删除", notes="采购单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pcsPurchaseOrderService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购单-通过id查询")
	@ApiOperation(value="采购单-通过id查询", notes="采购单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PcsPurchaseOrder pcsPurchaseOrder = pcsPurchaseOrderService.getById(id);
		if(pcsPurchaseOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pcsPurchaseOrder);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购明细集合-通过id查询")
	@ApiOperation(value="采购明细集合-通过id查询", notes="采购明细-通过id查询")
	@GetMapping(value = "/queryPcsOrderLineByMainId")
	public Result<?> queryPcsOrderLineListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PcsOrderLine> pcsOrderLineList = pcsOrderLineService.selectByMainId(id);
		List<ProdAndStockVo> result=new ArrayList<>();
		if(!CollectionUtils.isEmpty(pcsOrderLineList)){
			for(PcsOrderLine pcsOrderLine:pcsOrderLineList){
				ProdAndStockVo prodAndStockVo=new ProdAndStockVo();
				prodAndStockVo.setProductId(pcsOrderLine.getProductId());
				prodAndStockVo.setId(pcsOrderLine.getId());
				prodAndStockVo.setPartnerId(pcsOrderLine.getPartnerId());
				ScrapPro scrapPro=new ScrapPro();
				scrapPro.setProductId(pcsOrderLine.getProductId());
				List<ScrapPro> records=scrapProService.queryProdList(scrapPro);
				if(!CollectionUtils.isEmpty(records)){
					prodAndStockVo.setAcName(records.get(0).getAcName());
					prodAndStockVo.setUuName(records.get(0).getUuName());
					prodAndStockVo.setCateName(records.get(0).getCateName());
					prodAndStockVo.setName(records.get(0).getRpoductName());
					prodAndStockVo.setRpoductName(records.get(0).getRpoductName());
                    prodAndStockVo.setProductQty(records.get(0).getProductQty());
                    prodAndStockVo.setVirtualQty(records.get(0).getVirtualQty());
				}
				prodAndStockVo.setProductNo(pcsOrderLine.getDefaultCode());
				prodAndStockVo.setProductUomQty(new BigDecimal( pcsOrderLine.getProductPlyQty() == null ? 0 :  pcsOrderLine.getProductPlyQty()));
				prodAndStockVo.setPriceUnit(pcsOrderLine.getPriceUnit());
				prodAndStockVo.setPrice(pcsOrderLine.getPriceTotal());
				prodAndStockVo.setRemark(pcsOrderLine.getRemark());
				prodAndStockVo.setProductPlyQty(pcsOrderLine.getProductPlyQty());
				result.add(prodAndStockVo);
			}

		}
		return Result.ok(result);
	}

    /**
    * 审批
    *
    * @param request
    */
//	@RequestMapping(value = "/approve")
//    public Result<?> approve(HttpServletRequest request,String pickingNo ,String approveInfo,String id,String result) {
//		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//		PcsPurchaseOrder pcsPurchaseOrder = pcsPurchaseOrderService.getById(id);
//		Map<String, Object> var1=new HashMap<String, Object>();
//		var1.put("order_no",pickingNo);
//		var1.put("result",result);
//		Collection<StkSuggest> list= stkSuggestService.listByMap(var1);
//		//紧急单据 1次审核  urgency   正常单据2次审核 normal
//		if("normal".equals(pcsPurchaseOrder.getOrderType()) && list.size()>1){
//			return Result.error("已经审核通过！");
//		}else if ("urgency".equals(pcsPurchaseOrder.getOrderType()) && list.size()>0){
//			return Result.error("已经审核通过！");
//		}else {
//			StkSuggest stkSuggest=new StkSuggest();
//			stkSuggest.setCreateBy(sysUser.getRealname());
//			stkSuggest.setCreateTime(new Date());
//			stkSuggest.setOrderNo(pickingNo);
//			stkSuggest.setSugg(approveInfo);
//			stkSuggest.setResult(result);
//			if(STK_SUGGEST_RESULT.equals(result)){
//				pcsPurchaseOrder.setState("refuse");
//				pcsPurchaseOrderService.updateById(pcsPurchaseOrder);
//				stkSuggestService.save(stkSuggest);
//				return Result.ok("审批退回！");
//			}else {
//				if("normal".equals(pcsPurchaseOrder.getOrderType()) && list.size()==1 || "urgency".equals(pcsPurchaseOrder.getOrderType())){
//					//审核通过插入入库单信息
//					StockInOrderVo stockInOrderVo=new StockInOrderVo();
//					List<StockInOrderLineVo> stockInOrderLineList=new ArrayList<>();
//					stockInOrderVo.setContract(pcsPurchaseOrder.getContract());
//					stockInOrderVo.setCreateInBy(sysUser.getUsername());
//					stockInOrderVo.setCreateInName(sysUser.getRealname());
//					stockInOrderVo.setCreateTime(new Date());
//					try {
//						stockInOrderVo.setDatePlanned(DateUtils.parseDate(pcsPurchaseOrder.getDatePlanned(),"yyyy-MM-dd"));
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//					stockInOrderVo.setOrderType(pcsPurchaseOrder.getOrderType());
//					stockInOrderVo.setPartnerId(pcsPurchaseOrder.getPartnerId());
//
//					String partnerName = stockInOrderService.queryDictTextByKey("vendorCode", String.valueOf(pcsPurchaseOrder.getPartnerId()));
//					stockInOrderVo.setPartnerName(partnerName);
//					stockInOrderVo.setPickingNo(pcsPurchaseOrder.getPickingNo());
//					stockInOrderVo.setState("submit");
//					List<PcsOrderLine> pcsOrderLineList = pcsOrderLineService.selectByMainId(id);
//					for(PcsOrderLine line:pcsOrderLineList){
//						StockInOrderLineVo stockInOrderLine=new StockInOrderLineVo();
//						stockInOrderLine.setAttributeCategoryId(line.getAttributeCategoryId());
//						stockInOrderLine.setCategoryType(String.valueOf(line.getCategoryType()));
//						stockInOrderLine.setContract(pcsPurchaseOrder.getContract());
//						stockInOrderLine.setCreateTime(new Date());
//						stockInOrderLine.setDefaultCode(line.getDefaultCode());
//						stockInOrderLine.setPartnerId(line.getPartnerId());
//						stockInOrderLine.setPartnerName(line.getPartnerName());
//						stockInOrderLine.setProductId(line.getProductId());
//						stockInOrderLine.setProductName(line.getProductName());
//						stockInOrderLine.setProductUomId(line.getProductUomId());
//						stockInOrderLine.setProductPlyQty(new BigDecimal(line.getProductPlyQty()));
//						stockInOrderLine.setPriceUnit(line.getPriceUnit());
//						stockInOrderLineList.add(stockInOrderLine);
//					}
//					stockInOrderVo.setStockInOrderLineList(stockInOrderLineList);
//					StringBuffer sb = new StringBuffer();
//					sb.append("CGRK").append(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
//					stockInOrderVo.setOrderInId(sb.toString());
//					stockInOrderVo.setPickingNo(pickingNo);
//					stockInOrderService.saveMain(stockInOrderVo);
//					pcsPurchaseOrder.setState("done");
//					pcsPurchaseOrderService.updateById(pcsPurchaseOrder);
//				}
//				stkSuggestService.save(stkSuggest);
//				return Result.ok("审批成功！");
//			}
//		}
//    }

	/**
	 * 自定义审批人的角色业务表business_turn_process
	 * 1.更加当前采购单的的BUSINESS_SEQUENCE 自动增1 和业务BUSINESS_ID 和业务BUSINESS_TYPE 查询扭转下一个角色审批
	 * 2.把当前的用户角色条件传入
	 *
	 * @param request
	 */
	@RequestMapping(value = "/approve")
	public Result<?> approve(HttpServletRequest request,String pickingNo ,String approveInfo,String id,String result) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		PcsPurchaseOrder pcsPurchaseOrder = pcsPurchaseOrderService.getById(id);
		Map<String, Object> var1=new HashMap<String, Object>();
		var1.put("order_no",pickingNo);
		var1.put("result",result);
		Collection<StkSuggest> list= stkSuggestService.listByMap(var1);
		//紧急单据 1次审核  urgency   正常单据2次审核 normal
			StkSuggest stkSuggest=new StkSuggest();
			stkSuggest.setCreateBy(sysUser.getRealname());
			stkSuggest.setCreateTime(new Date());
			stkSuggest.setOrderNo(pickingNo);
			stkSuggest.setSugg(approveInfo);
			stkSuggest.setResult(result);
			if(STK_SUGGEST_RESULT.equals(result)){
				pcsPurchaseOrder.setState("refuse");
				//审批拒绝 结束扭转
				pcsPurchaseOrder.setNextRoleId("99");
				pcsPurchaseOrderService.updateById(pcsPurchaseOrder);
				stkSuggestService.save(stkSuggest);
				return Result.ok("审批退回！");
			}else {

				// 获取当前用户的所有角色id
				int sequenceNum=pcsPurchaseOrder.getBusinessSequence()+1;
					QueryWrapper<BusinessTurnProcess> params=new QueryWrapper<BusinessTurnProcess>();
					params.eq("BUSINESS_SEQUENCE",sequenceNum+1);
					params.eq("BUSINESS_ID","1001");
					params.eq("BUSINESS_TYPE",pcsPurchaseOrder.getOrderType());
					params.orderByAsc("BUSINESS_SEQUENCE");
					List<BusinessTurnProcess>  businessTurnProcesses=iBusinessTurnProcessService.list(params);
					if(!CollectionUtil.isEmpty(businessTurnProcesses)){
						pcsPurchaseOrder.setNextRoleId(businessTurnProcesses.get(0).getRoleId());
						pcsPurchaseOrder.setBusinessSequence(sequenceNum);
						pcsPurchaseOrderService.updateById(pcsPurchaseOrder);
					}else {
						log.error("审批自定义审批人的角色业务表business_turn_process 业务表查出的下一个角色数据为空时，审核流程结束");
						//审核通过插入入库单信息
						approveSuccess(pcsPurchaseOrder,id,pickingNo);
						pcsPurchaseOrder.setState("done");
						pcsPurchaseOrder.setNextRoleId("99");
						pcsPurchaseOrderService.updateById(pcsPurchaseOrder);
					}
				 }
				stkSuggestService.save(stkSuggest);
				return Result.ok("审批成功！");
	}

	/**
	 * //审核通过插入入库单信息
	 * 自定义审批人的角色业务表business_turn_process 业务表查出的下一个角色数据为空时，审核流程结束
	 */
	public  void approveSuccess(PcsPurchaseOrder pcsPurchaseOrder,String id,String pickingNo){
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		StockInOrderVo stockInOrderVo=new StockInOrderVo();
		List<StockInOrderLineVo> stockInOrderLineList=new ArrayList<>();
		stockInOrderVo.setContract(pcsPurchaseOrder.getContract());

		stockInOrderVo.setCreateInBy(pcsPurchaseOrder.getCreateBy());

		stockInOrderVo.setCreateInName(pcsPurchaseOrder.getCreateName());
		stockInOrderVo.setCreateTime(new Date());
		try {
			stockInOrderVo.setDatePlanned(DateUtils.parseDate(pcsPurchaseOrder.getDatePlanned(),"yyyy-MM-dd"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		stockInOrderVo.setOrderType(pcsPurchaseOrder.getOrderType());
		stockInOrderVo.setPartnerId(pcsPurchaseOrder.getPartnerId());

		String partnerName = stockInOrderService.queryDictTextByKey("vendorCode", String.valueOf(pcsPurchaseOrder.getPartnerId()));
		stockInOrderVo.setPartnerName(partnerName);
		stockInOrderVo.setPickingNo(pcsPurchaseOrder.getPickingNo());
		stockInOrderVo.setState("submit");
		List<PcsOrderLine> pcsOrderLineList = pcsOrderLineService.selectByMainId(id);
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
		stockInOrderVo.setPickingNo(pickingNo);
		stockInOrderService.saveMain(stockInOrderVo);
	}

	@PostMapping(value = "/commple")
	public Result<?> commple(@RequestBody PcsPurchaseOrderPage pcsPurchaseOrderPage) {
//		String[] strings=ids.split(";");
		PcsPurchaseOrder pcsPurchaseOrder = new PcsPurchaseOrder();
		BeanUtils.copyProperties(pcsPurchaseOrderPage, pcsPurchaseOrder);
		pcsPurchaseOrderService.commpleOrderPlan(pcsPurchaseOrder,pcsPurchaseOrderPage.getPcsOrderLineList());
		return Result.ok("操作成功！");
	}


	/**
	 * 通过ids查询
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购明细集合-通过ids查询")
	@ApiOperation(value="采购明细集合-通过ids查询", notes="采购明细-通过ids查询")
	@PostMapping(value = "/queryPcsOrderLineByIds")
	public Result<?> queryPcsOrderLineByIds(@RequestBody String[] ids) {
		List<ProdAndStockVo> pcsOrderLines = new ArrayList<>();
		for(String id : ids){
			List<PcsOrderLine> pcsOrderLineList = pcsOrderLineService.selectByMainId(id);
			List<ProdAndStockVo> result=new ArrayList<>();
			if(!CollectionUtils.isEmpty(pcsOrderLineList)){
				for(PcsOrderLine pcsOrderLine:pcsOrderLineList){
					ProdAndStockVo prodAndStockVo=new ProdAndStockVo();
					prodAndStockVo.setId(pcsOrderLine.getId());
					prodAndStockVo.setProductId(pcsOrderLine.getProductId());
					ScrapPro scrapPro=new ScrapPro();
					scrapPro.setProductId(pcsOrderLine.getProductId());
					List<ScrapPro> records=scrapProService.queryProdList(scrapPro);
					if(!CollectionUtils.isEmpty(records)){
						prodAndStockVo.setAcName(records.get(0).getAcName());
						prodAndStockVo.setUuName(records.get(0).getUuName());
						prodAndStockVo.setCateName(records.get(0).getCateName());
						prodAndStockVo.setName(records.get(0).getRpoductName());
						prodAndStockVo.setRpoductName(records.get(0).getRpoductName());
                        prodAndStockVo.setProductQty(records.get(0).getProductQty());
                        prodAndStockVo.setVirtualQty(records.get(0).getVirtualQty());
						prodAndStockVo.setProductNo(records.get(0).getProductNo());
					}
//					prodAndStockVo.setProductNo(pcsOrderLine.getDefaultCode());
					prodAndStockVo.setProductUomQty(new BigDecimal( pcsOrderLine.getProductPlyQty() == null ? 0 :  pcsOrderLine.getProductPlyQty()));
					prodAndStockVo.setProductPlyQty(pcsOrderLine.getProductPlyQty());
					prodAndStockVo.setPriceUnit(pcsOrderLine.getPriceUnit());
					prodAndStockVo.setPrice(pcsOrderLine.getPriceTotal());
					prodAndStockVo.setRemark(pcsOrderLine.getRemark());
					prodAndStockVo.setPartnerId(pcsOrderLine.getPartnerId());
					result.add(prodAndStockVo);
				}

			}
			pcsOrderLines.addAll(result);
		}
		return Result.ok(pcsOrderLines);
	}

	/**
	 * @param pcsPurchaseOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采购单-审核单独列表")
	@ApiOperation(value="采购单-审核单独列表", notes="采购单-审核单独列表")
	@GetMapping(value = "/approveList")
	public Result<?> A(PcsPurchaseOrder pcsPurchaseOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<PcsPurchaseOrder> page = new Page<>(pageNo, pageSize);
		Map<String, String[]> parameterMap = req.getParameterMap();
		Map<String,Object> dataMap = new HashMap<>(256);
		parameterMap.forEach((key, value) -> {
			dataMap.put(key,value[0]);
		});
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		// 获取当前用户的所有角色id
		List<String> roleIds=iSysBaseAPI.getRoleIdsByUsername(sysUser.getUsername());

		dataMap.put("roleIds",roleIds);

		dataMap.put("businessSequence","true");
		IPage<PcsPurchaseOrder> pcsPurchaseOrderIPage = pcsPurchaseOrderService.selectPageOrders(page,dataMap);
		if (CollectionUtils.isEmpty(pcsPurchaseOrderIPage.getRecords())){
			return Result.ok(pcsPurchaseOrderIPage);
		}
		//加载采购详情
		pcsPurchaseOrderIPage.getRecords().forEach(item -> {
			//订单编号为空到采购计划表查询
			if (!StringUtils.isEmpty(item.getPickingNo())) {
				QueryWrapper<PcsOrderLine> queryWrapper2 = new QueryWrapper<>();
				queryWrapper2.eq("order_id", item.getId());
				//获取采购详情实体类
				PcsOrderLine pcsOrderLine = pcsOrderLineService.list(queryWrapper2) == null  ? null : pcsOrderLineService.list(queryWrapper2).get(0);
				if (pcsOrderLine != null) {
					//规格
					PrdAttributeCategory category = iPrdAttributeCategoryService.getById(pcsOrderLine.getAttributeCategoryId());
					//单位
					UomUom uom = iUomUomService.getById(pcsOrderLine.getProductUomId());
					item.setOrignContent(pcsOrderLine.getProductName() == null ? "" : pcsOrderLine.getProductName() + " [ " + (category == null ? "" : category.getName() == null ? "" : category.getName()) + " ]" + "," + item.getNumberTotal() + (uom == null ? "" : uom.getName() == null ? "" : uom.getName()));
				}
			}else{
				StringBuffer resultBuff=new StringBuffer();
				List<StkMove> stkMove = stkMoveService.selectStkMoveByOrign(item.getOrign());
				stkMove.forEach( move->{
					if (move != null){
						//规格
						PrdAttributeCategory category = iPrdAttributeCategoryService.selectAttributeByProductId(move.getProductId());
						//单位
						UomUom uom = iUomUomService.getById(move.getProductUomId());
						resultBuff.append(move.getName() == null ? "" : move.getName() + "- [ " + (category == null ? "" : category.getName() == null ? "" : category.getName()) + " ]" + "-" + item.getNumberTotal() + (uom == null ? "" : uom.getName() == null ? "" : uom.getName())+"； ");
//						item.setOrignContent(move.getProductQty() == null ? "" : move.getName() + " [ " + (category == null ? "" : category.getName() == null ? "" : category.getName()) + " ]" + "," + item.getNumberTotal() + (uom == null ? "" : uom.getName() == null ? "" : uom.getName()));
					}
				});
				item.setOrignContent(String.valueOf(resultBuff));
			}
		});
		return Result.ok(pcsPurchaseOrderIPage);
	}


}
