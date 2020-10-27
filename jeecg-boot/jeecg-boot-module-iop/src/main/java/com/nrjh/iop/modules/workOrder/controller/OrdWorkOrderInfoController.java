package com.nrjh.iop.modules.workOrder.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.bind.service.IFaultPrdProductInfoService;
import com.nrjh.iop.modules.alarm.bind.vo.FaultPrdProductInfoVO;
import com.nrjh.iop.modules.alarm.record.service.IVAlarmInfoService;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.EquipRemoteUser;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.IEquipRemoteUserService;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrder;
import com.nrjh.iop.modules.outOrder.service.IStockOutOrderService;
import com.nrjh.iop.modules.workOrder.entity.SysUser;
import com.nrjh.iop.modules.workOrder.service.CommonOrdWorkOrderInfoService;
import com.nrjh.iop.modules.workOrder.vo.OrdWorkOrderEnclosureInfoVo;
import io.netty.util.internal.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import com.nrjh.iop.modules.workOrder.service.IOrdWorkOrderInfoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.iop.pub.pubuser.entity.PubUser2;
import org.jeecg.modules.iop.pub.pubuser.mapper.PubUserMapper2;
import org.jeecg.modules.iop.pub.pubuser.service.IPubUserService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 故障运维工单表  工单待派发
 * @Author: lei-li
 * @Date:   2020-09-01
 * @Version: V1.0
 */
@Api(tags="故障运维工单表")
@RestController
@RequestMapping("/workOrder/ordWorkOrderInfo")
@Slf4j
public class OrdWorkOrderInfoController extends JeecgController<OrdWorkOrderInfo, IOrdWorkOrderInfoService> {
	@Autowired
	private IOrdWorkOrderInfoService ordWorkOrderInfoService;
	@Autowired
	private CommonOrdWorkOrderInfoService commonOrdWorkOrderInfoService;
	@Autowired
	private IStockOutOrderService stockOutOrderService;
	@Autowired
	private IVAlarmInfoService ivAlarmInfoService;
	@Autowired
	private IFaultPrdProductInfoService faultPrdProductInfoService;
	 @Autowired
	 private IEquipRemoteUserService iEquipRemoteUserService;
     /**
      * 通过工单领料出库单号查询领料出库信息
      *
      * @param orderOutId
      * @return
      */
     @AutoLog(value = "通过工单领料出库单号查询领料出库信息")
     @ApiOperation(value = "通过工单领料出库单号查询领料出库信息", notes = "通过工单领料出库单号查询领料出库信息")
     @GetMapping(value = "/queryStockOutById")
     public Result<?> queryStockOutById(@RequestParam(name = "orderOutId", required = true) String orderOutId) {
         StockOutOrder stockOutOrder = stockOutOrderService.queryStockOutById(orderOutId);
         if (stockOutOrder == null) {
             return Result.error("未找到对应数据");
         }
         return Result.ok(stockOutOrder);
     }
	 /**
	  * 通过设备id查询设备编号
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "故障运维工单表-通过设备id查询设备编号")
	 @ApiOperation(value="故障运维工单表-通过设备id查询设备编号", notes="故障运维工单表-通过设备id查询设备编号")
	 @GetMapping(value = "/queryEquipNo")
	 public Result<?> queryEquipNo(@RequestParam(name="id",required=true) String id) {
		String equipNo = ordWorkOrderInfoService.getEquipNo(id);
		 if(equipNo==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(equipNo);
	 }

	/**
	 * 分页列表查询
	 *
	 * @param ordWorkOrderInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "故障运维工单表-分页列表查询")
	@ApiOperation(value="故障运维工单表-分页列表查询", notes="故障运维工单表-分页列表查询")
		@GetMapping(value = "/list")
	public Result<?> queryPageList(OrdWorkOrderInfo ordWorkOrderInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		if(ordWorkOrderInfo.getOrderType() != null){
			JSONObject jsonObject = JSONObject.parseObject(ordWorkOrderInfo.getOrderType());
			Object o = jsonObject.get("key");
			ordWorkOrderInfo.setOrderType((String)o);
		}
		QueryWrapper<OrdWorkOrderInfo> queryWrapper = QueryGenerator.initQueryWrapper(ordWorkOrderInfo, req.getParameterMap());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try{
			if(req.getParameterMap().get("startApplyDate")!=null && req.getParameterMap().get("endApplyDate") != null){
				Date startApplyTime = formatter.parse(req.getParameterMap().get("startApplyDate")[0]);
				Date endApplyDate = formatter.parse(req.getParameterMap().get("endApplyDate")[0]);
				queryWrapper.between("CREATE_TIME",startApplyTime,endApplyDate);
			}
		}catch (JeecgBootException | ParseException e){
			throw new JeecgBootException("时间格式转换失败");
		}
		Page<OrdWorkOrderInfo> page = new Page<OrdWorkOrderInfo>(pageNo, pageSize);
		queryWrapper.eq("PROCESS_STATUS","01");
		IPage<OrdWorkOrderInfo> pageList = ordWorkOrderInfoService.page(page, queryWrapper);
		pageList.getRecords().forEach(item ->{
			if(!StringUtil.isNullOrEmpty(item.getEquipNo())){
				List<Equip> equips=ivAlarmInfoService.queryListEquip(item.getEquipNo());
				if (!CollectionUtil.isEmpty(equips)){
					/**
					 * 判断是否可以远程
					 */
					QueryWrapper<EquipRemoteUser> queryWrapper2 = new QueryWrapper<EquipRemoteUser>();
					queryWrapper.eq("AREA_ID",item.getLineNo());
					queryWrapper.eq("EQUIP_NO",item.getEquipNo());
					List<EquipRemoteUser> list=	iEquipRemoteUserService.list(queryWrapper2);
					if (CollectionUtils.isEmpty(list)) {
						item.setRemoteStatus("0");
					} else {
						item.setRemoteStatus("1");
					}
//					item.setEquipName(equips.get(0).getName());
					FaultPrdProductInfoVO faultPrdProductInfoVO =new FaultPrdProductInfoVO();
					faultPrdProductInfoVO.setProductId(equips.get(0).getEquipId());
					faultPrdProductInfoVO.setLineNo(equips.get(0).getAreaId());
					List<FaultPrdProductInfoVO> faultPrdProductInfoVOList=  faultPrdProductInfoService.queryFaultPrdProduct(faultPrdProductInfoVO);
					//todo 存在多个设备绑定不同的等级怎么处理？
					if(!CollectionUtil.isEmpty(faultPrdProductInfoVOList)){
						item.setAlarmLevel(faultPrdProductInfoVOList.get(0).getFaultGrade());
					}
					item.setAlarmAmount(ivAlarmInfoService.alarmAmountByEquipId(item.getEquipNo()));
				}
			}
		});
		return Result.ok(pageList);
	}



	/**
	 *   添加
	 *
	 * @param ordWorkOrderEnclosureInfoVo
	 * @return
	 */
	@AutoLog(value = "故障运维工单表-添加")
	@ApiOperation(value="故障运维工单表-添加", notes="故障运维工单表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody OrdWorkOrderEnclosureInfoVo ordWorkOrderEnclosureInfoVo) {

		ordWorkOrderInfoService.saveWorkOrderInfo(ordWorkOrderEnclosureInfoVo);

		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param ordWorkOrderInfo
	 * @return
	 */
	@AutoLog(value = "故障运维工单表-编辑")
	@ApiOperation(value="故障运维工单表-编辑", notes="故障运维工单表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody OrdWorkOrderInfo ordWorkOrderInfo) {
		ordWorkOrderInfoService.updateById(ordWorkOrderInfo);
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "故障运维工单表-通过id删除")
	@ApiOperation(value="故障运维工单表-通过id删除", notes="故障运维工单表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
//		ordWorkOrderInfoService.removeById(id);
		ordWorkOrderInfoService.deleteById(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "故障运维工单表-批量删除")
	@ApiOperation(value="故障运维工单表-批量删除", notes="故障运维工单表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ordWorkOrderInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "故障运维工单表-通过id查询")
	@ApiOperation(value="故障运维工单表-通过id查询", notes="故障运维工单表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		OrdWorkOrderInfo ordWorkOrderInfo = ordWorkOrderInfoService.getById(id);
		if(ordWorkOrderInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(ordWorkOrderInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ordWorkOrderInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OrdWorkOrderInfo ordWorkOrderInfo) {
        return super.exportXls(request, ordWorkOrderInfo, OrdWorkOrderInfo.class, "故障运维工单表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, OrdWorkOrderInfo.class);
    }

	 /**
	  * 查询所有人员信息
	  *
	  * @param
	  * @return
	  */
	 @AutoLog(value = "查询所有人员信息-派工")
	 @ApiOperation(value="查询所有人员信息-派工", notes="查询所有人员信息-派工")
	 @GetMapping(value = "/queryUserInfo")
	 public Result<?> queryUserInfo(SysUser sysUser,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req){
	 	return common(pageNo,pageSize,"02",req,sysUser);
	 }

	 @AutoLog(value = "查询所有人员信息-转派")
	 @ApiOperation(value="查询所有人员信息-转派", notes="查询所有人员信息-转派")
	 @GetMapping(value = "/queryUserInfo2")
	 public Result<?> queryUserInfo2(SysUser sysUser,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req){
		 return common(pageNo,pageSize,"04",req,sysUser);
	 }

	 private Result<?> common(Integer pageNo,Integer pageSize,String status,HttpServletRequest req,SysUser sysUser){
		 QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(sysUser, req.getParameterMap());
		 Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
//		 List<SysUser> sysUserList = commonOrdWorkOrderInfoService.queryUserInfo();
		 Map<String, String[]> parameterMap = req.getParameterMap();
		 Map<String,Object> dataMap = new HashMap<>(256);
		 parameterMap.forEach((key, value) -> {
			 dataMap.put(key,value[0]);
		 });
		 IPage<SysUser> pageList = commonOrdWorkOrderInfoService.queryUserInfo(page,dataMap);
		 if (CollectionUtils.isEmpty(pageList.getRecords())){
			 return Result.ok(pageList);
		 }
		 pageList.getRecords().forEach(item -> {
			 QueryWrapper queryWrapper2 = new QueryWrapper<SysUser> ();
			 queryWrapper2.eq("DISPATCH_TO_USER",item.getId());
			 queryWrapper2.eq("PROCESS_STATUS",status);
			 item.setSize(ordWorkOrderInfoService.count(queryWrapper2));
		 });
		 return Result.ok(pageList);
	 }



	 /**
	  *   派工
	  *
	  * @param ordWorkOrderInfoList
	  * @return
	  */
	 @AutoLog(value = "派工")
	 @ApiOperation(value="派工", notes="派工")
	 @PostMapping(value = "/dispatchingInfo")
	 public Result<?> dispatchingInfo(@RequestBody List<OrdWorkOrderInfo> ordWorkOrderInfoList) {
	 	for(OrdWorkOrderInfo ordWorkOrderInfo : ordWorkOrderInfoList){
			ordWorkOrderInfoService.saveDispatchingInfo(ordWorkOrderInfo);
		}
		 return Result.ok("添加成功！");
	 }

	 /**
	  *  转派
	  *
	  * @param ordWorkOrderInfoList
	  * @return
	  */
	 @AutoLog(value = "转派")
	 @ApiOperation(value="转派", notes="转派")
	 @PostMapping(value = "/transferInfo")
	 public Result<?> transferInfo(@RequestBody List<OrdWorkOrderInfo> ordWorkOrderInfoList) {
		 for(OrdWorkOrderInfo ordWorkOrderInfo : ordWorkOrderInfoList){
			if (ordWorkOrderInfo == null){
				continue;
			}
			 SysUser sysUser = commonOrdWorkOrderInfoService.getUserInfoById(ordWorkOrderInfo.getDispatchToUser());
			 if(BeanUtil.isEmpty(sysUser)){
			 	return Result.error("人员信息错误");
			 }
		 	ordWorkOrderInfoService.saveTransferInfo(ordWorkOrderInfo,sysUser);
		 }
		 return Result.ok("添加成功！");
	 }

	 /**
	  * 结单
	  */
	 @AutoLog(value = "结单")
	 @ApiOperation(value="结单", notes="结单")
	 @PostMapping(value = "/checkInfo")
	 public Result<?> transferInfo(@RequestParam("id") String id) {
		 OrdWorkOrderInfo ordWorkOrderInfo = ordWorkOrderInfoService.getById(id);
		 ordWorkOrderInfo.setRecoveryTime(new Date());
		 //09，已归档
		 ordWorkOrderInfo.setProcessStatus("09");
		 ordWorkOrderInfoService.updateById(ordWorkOrderInfo);
		 return Result.ok("更新成功！");
	 }

 }
