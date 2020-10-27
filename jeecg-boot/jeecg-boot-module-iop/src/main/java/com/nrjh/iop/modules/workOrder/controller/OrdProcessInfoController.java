package com.nrjh.iop.modules.workOrder.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultRepairInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultTaskInfo;
import com.nrjh.iop.modules.workOrder.entity.OrdProcessInfo;
import com.nrjh.iop.modules.workOrder.service.IOrdProcessInfoService;
import com.nrjh.iop.modules.workOrder.vo.OrdProcessInfoEnclosureInfoVo;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 工单处理明细
 * @Author: jeecg-boot
 * @Date:   2020-09-08
 * @Version: V1.0
 */
@Api(tags="工单处理明细")
@RestController
@RequestMapping("/ordProcessInfo/ordProcessInfo")
@Slf4j
public class OrdProcessInfoController extends JeecgController<OrdProcessInfo, IOrdProcessInfoService> {
	@Autowired
	private IOrdProcessInfoService ordProcessInfoService;
	 @Autowired
	 private ISysBaseAPI iSysBaseAPI;


	/**
     * 查询修复方法
     *
     * @param faultTaskInfo
     * @return
     */
    @AutoLog(value = "查询修复方法")
    @ApiOperation(value="查询修复方法", notes="查询修复方法")
    @PostMapping(value = "/queryRepairInfo")
    public Result<?> queryRepairInfo(@RequestBody FaultTaskInfo faultTaskInfo) {
		List<FaultRepairInfo> faultRepairInfoList= ordProcessInfoService.queryRepairInfo(faultTaskInfo);
        if(faultRepairInfoList.size() == 0) {
            return Result.error("未找到修复方法数据");
        }
        return Result.ok(faultRepairInfoList);
    }
	/**
	 * 分页列表查询
	 *
	 * @param ordProcessInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "工单处理明细-分页列表查询")
	@ApiOperation(value="工单处理明细-分页列表查询", notes="工单处理明细-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(OrdProcessInfo ordProcessInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<OrdProcessInfo> queryWrapper = QueryGenerator.initQueryWrapper(ordProcessInfo, req.getParameterMap());
		Page<OrdProcessInfo> page = new Page<OrdProcessInfo>(pageNo, pageSize);
		IPage<OrdProcessInfo> pageList = ordProcessInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param ordProcessInfoEnclosureInfoVo
	 * @return
	 */
	@AutoLog(value = "工单处理明细-添加")
	@ApiOperation(value="工单处理明细-添加", notes="工单处理明细-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody OrdProcessInfoEnclosureInfoVo ordProcessInfoEnclosureInfoVo) {
		LoginUser loginUser = iSysBaseAPI.getUserById(ordProcessInfoEnclosureInfoVo.getCreateId());
		if (loginUser != null) {
			ordProcessInfoEnclosureInfoVo.setCreateName(loginUser.getRealname());
		}
		ordProcessInfoService.saveOrdProcessAndEnclosureInfo(ordProcessInfoEnclosureInfoVo);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param ordProcessInfo
	 * @return
	 */
	@AutoLog(value = "工单处理明细-编辑")
	@ApiOperation(value="工单处理明细-编辑", notes="工单处理明细-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody OrdProcessInfo ordProcessInfo) {
		ordProcessInfoService.updateById(ordProcessInfo);
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "工单处理明细-通过id删除")
	@ApiOperation(value="工单处理明细-通过id删除", notes="工单处理明细-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		ordProcessInfoService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "工单处理明细-批量删除")
	@ApiOperation(value="工单处理明细-批量删除", notes="工单处理明细-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ordProcessInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "工单处理明细-通过id查询")
	@ApiOperation(value="工单处理明细-通过id查询", notes="工单处理明细-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		OrdProcessInfo ordProcessInfo = ordProcessInfoService.getById(id);
		if(ordProcessInfo==null) {
			return Result.error("未找到处理明细数据");
		}
		return Result.ok(ordProcessInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ordProcessInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OrdProcessInfo ordProcessInfo) {
        return super.exportXls(request, ordProcessInfo, OrdProcessInfo.class, "工单处理明细");
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
        return super.importExcel(request, response, OrdProcessInfo.class);
    }

}
