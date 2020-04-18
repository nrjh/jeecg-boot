package com.nrjh.iop.modules.message.trackingvalue.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.message.trackingvalue.entity.MessageTrackingValue;
import com.nrjh.iop.modules.message.trackingvalue.service.IMessageTrackingValueService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 变更记录
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Api(tags="变更记录")
@RestController
@RequestMapping("/iop/message/trackingvalue")
@Slf4j
public class MessageTrackingValueController extends JeecgController<MessageTrackingValue, IMessageTrackingValueService> {
	@Autowired
	private IMessageTrackingValueService messageTrackingValueService;
	
	/**
	 * 分页列表查询
	 *
	 * @param messageTrackingValue
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "变更记录-分页列表查询")
	@ApiOperation(value="变更记录-分页列表查询", notes="变更记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MessageTrackingValue messageTrackingValue,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MessageTrackingValue> queryWrapper = QueryGenerator.initQueryWrapper(messageTrackingValue, req.getParameterMap());
		Page<MessageTrackingValue> page = new Page<MessageTrackingValue>(pageNo, pageSize);
		IPage<MessageTrackingValue> pageList = messageTrackingValueService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param messageTrackingValue
	 * @return
	 */
	@AutoLog(value = "变更记录-添加")
	@ApiOperation(value="变更记录-添加", notes="变更记录-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody MessageTrackingValue messageTrackingValue) {
		messageTrackingValueService.save(messageTrackingValue);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param messageTrackingValue
	 * @return
	 */
	@AutoLog(value = "变更记录-编辑")
	@ApiOperation(value="变更记录-编辑", notes="变更记录-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody MessageTrackingValue messageTrackingValue) {
		messageTrackingValueService.updateById(messageTrackingValue);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "变更记录-通过id删除")
	@ApiOperation(value="变更记录-通过id删除", notes="变更记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		messageTrackingValueService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "变更记录-批量删除")
	@ApiOperation(value="变更记录-批量删除", notes="变更记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.messageTrackingValueService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "变更记录-通过id查询")
	@ApiOperation(value="变更记录-通过id查询", notes="变更记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MessageTrackingValue messageTrackingValue = messageTrackingValueService.getById(id);
		if(messageTrackingValue==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(messageTrackingValue);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param messageTrackingValue
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MessageTrackingValue messageTrackingValue) {
        return super.exportXls(request, messageTrackingValue, MessageTrackingValue.class, "变更记录");
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
        return super.importExcel(request, response, MessageTrackingValue.class);
    }

}
