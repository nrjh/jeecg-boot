package com.nrjh.iop.modules.alarm.alarmtype.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultReasonInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultRepairInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultSopInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultTaskInfo;
import com.nrjh.iop.modules.alarm.alarmtype.service.*;
import com.nrjh.iop.modules.alarm.alarmtype.vo.EquipIdModel;
import com.nrjh.iop.modules.alarm.alarmtype.vo.FaultTaskInfoPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 报警类型管理
 * @Author: jeecg-boot
 * @Date:   2020-08-27
 * @Version: V1.0
 */
@Api(tags="报警类型管理")
@RestController
@RequestMapping("/alarmType/faultTaskInfo")
@Slf4j
public class FaultTaskInfoController {
	@Autowired
	private IFaultTaskInfoService faultTaskInfoService;
	@Autowired
	private IFaultRepairInfoService faultRepairInfoService;
	@Autowired
	private IFaultSopInfoService faultSopInfoService;
	@Autowired
	private IEquipldTreeService iEquipldTreeService;

	@Autowired
	private IFaultReasonInfoService faultReasonInfoService;

	/**
	 * 分页列表查询
	 *
	 * @param faultTaskInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "报警类型管理-分页列表查询")
	@ApiOperation(value="报警类型管理-分页列表查询", notes="报警类型管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(FaultTaskInfo faultTaskInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<FaultTaskInfo> queryWrapper = QueryGenerator.initQueryWrapper(faultTaskInfo, req.getParameterMap());
		Page<FaultTaskInfo> page = new Page<FaultTaskInfo>(pageNo, pageSize);
		IPage<FaultTaskInfo> pageList = faultTaskInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param faultTaskInfoPage
	 * @return
	 */
	@AutoLog(value = "报警类型管理-添加")
	@ApiOperation(value="报警类型管理-添加", notes="报警类型管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FaultTaskInfoPage faultTaskInfoPage) {
		FaultTaskInfo faultTaskInfo = new FaultTaskInfo();
		BeanUtils.copyProperties(faultTaskInfoPage, faultTaskInfo);
		if(StringUtils.isEmpty(faultTaskInfo.getIsStatus())){
			faultTaskInfo.setIsStatus(String.valueOf(1));
		}
		faultTaskInfoService.saveMain(faultTaskInfo, faultTaskInfoPage.getFaultRepairInfoList(),faultTaskInfoPage.getFaultSopInfoList(),faultTaskInfoPage.getFaultReasonInfoList());
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param faultTaskInfoPage
	 * @return
	 */
	@AutoLog(value = "报警类型管理-编辑")
	@ApiOperation(value="报警类型管理-编辑", notes="报警类型管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FaultTaskInfoPage faultTaskInfoPage) {
		FaultTaskInfo faultTaskInfo = new FaultTaskInfo();
		BeanUtils.copyProperties(faultTaskInfoPage, faultTaskInfo);
		FaultTaskInfo faultTaskInfoEntity = faultTaskInfoService.getById(faultTaskInfo.getId());
		if(faultTaskInfoEntity==null) {
			return Result.error("未找到对应数据");
		}
		if(StringUtils.isEmpty(faultTaskInfo.getLineTeam())){
			faultTaskInfo.setLineTeam(faultTaskInfoEntity.getLineTeam());
		}
		if(StringUtils.isEmpty(faultTaskInfo.getIsStatus())){
			faultTaskInfo.setIsStatus(String.valueOf(1));
		}
		faultTaskInfoService.updateMain(faultTaskInfo, faultTaskInfoPage.getFaultRepairInfoList(),faultTaskInfoPage.getFaultSopInfoList(),faultTaskInfoPage.getFaultReasonInfoList());
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "报警类型管理-通过id删除")
	@ApiOperation(value="报警类型管理-通过id删除", notes="报警类型管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		faultTaskInfoService.delMain(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "报警类型管理-批量删除")
	@ApiOperation(value="报警类型管理-批量删除", notes="报警类型管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.faultTaskInfoService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "报警类型管理-通过id查询")
	@ApiOperation(value="报警类型管理-通过id查询", notes="报警类型管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		FaultTaskInfo faultTaskInfo = faultTaskInfoService.getById(id);
		if(faultTaskInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(faultTaskInfo);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "修复方法集合-通过id查询")
	@ApiOperation(value="修复方法集合-通过id查询", notes="修复方法-通过id查询")
	@GetMapping(value = "/queryFaultRepairInfoByMainId")
	public Result<?> queryFaultRepairInfoListByMainId(@RequestParam(name="id",required=true) String id) {
		List<FaultRepairInfo> faultRepairInfoList = faultRepairInfoService.selectByMainId(id);
		return Result.ok(faultRepairInfoList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "作业指导集合-通过id查询")
	@ApiOperation(value="作业指导集合-通过id查询", notes="作业指导-通过id查询")
	@GetMapping(value = "/queryFaultSopInfoByMainId")
	public Result<?> queryFaultSopInfoListByMainId(@RequestParam(name="id",required=true) String id) {
		List<FaultSopInfo> faultSopInfo2List = faultSopInfoService.selectByMainId(id);
		return Result.ok(faultSopInfo2List);
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "作业指导集合-通过id查询")
	@ApiOperation(value="作业指导集合-通过id查询", notes="作业指导-通过id查询")
	@GetMapping(value = "/queryFaultReasonInfoListByMainId")
	public Result<?> queryFaultReasonInfoListByMainId(@RequestParam(name="id",required=true) String id) {
		List<FaultReasonInfo> faultReasonInfo = faultSopInfoService.selectReasonByMainId(id);
		return Result.ok(faultReasonInfo);
	}

	/**
	 * 查询数据,并以树结构数据格式响应给前端
	 *
	 * @return
	 */
	@RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
	public Result<List<EquipIdModel>> queryMyDeptTreeList() {
		Result<List<EquipIdModel>> result = new Result<>();

		List<EquipIdModel> listFrist=iEquipldTreeService.selectFristEquip();
		for(EquipIdModel equipIdModel:listFrist){
//			List<EquipIdModel> children=iEquipldTreeService.selectThreeEquip(equipIdModel.getParamId());
			List<EquipIdModel> children=iEquipldTreeService.selectEquipType(equipIdModel.getParamId());
			for(EquipIdModel ch:children){
				ch.setLeaf(true);
				ch.setId(equipIdModel.getParamId()+ch.getParamId());
				ch.setKey(equipIdModel.getParamId()+ch.getParamId());
				ch.setValue(ch.getParamId());
				ch.setParentId(equipIdModel.getParamId());
				ch.setTitle(ch.getParamName());
			}
			equipIdModel.setChildren(children);
			equipIdModel.setLeaf(false);
			equipIdModel.setKey(equipIdModel.getParamId());
			equipIdModel.setId(equipIdModel.getParamId());
			equipIdModel.setValue(equipIdModel.getParamId());
		}
		result.setResult(listFrist);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 查询故障报警名称
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryFaulNameByEquipType")
	public Result<?> queryFaulNameByEquipType(@RequestParam(name="id",required=true) String id) {
		List<Map> list = faultTaskInfoService.queryFaulNameByEquipType(id);
		return Result.ok(list);
	}

	@AutoLog(value = "查询报警原因")
	@ApiOperation(value="查询报警原因-通过检定线编号、设备编号查询", notes="查询报警原因-通过检定线编号、设备编号查询")
	@GetMapping(value = "/errorReasonNameAndId")
	public Result<?> errorReasonNameAndId(@RequestParam(name="lineNo",required=true) String lineNo,@RequestParam(name = "equipNo",required = true)String equipNo) {
		List<FaultReasonInfo> faultReasonInfoList = faultReasonInfoService.errorReasonNameAndId(lineNo,equipNo);
		if(faultReasonInfoList.size() == 0) {
			return Result.error("未找到报警原因数据");
		}
		return Result.ok(faultReasonInfoList);
	}

}
