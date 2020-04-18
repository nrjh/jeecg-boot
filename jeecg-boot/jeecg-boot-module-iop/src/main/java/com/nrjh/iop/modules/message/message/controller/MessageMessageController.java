package com.nrjh.iop.modules.message.message.controller;

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
import com.nrjh.iop.modules.message.message.entity.MessageMessage;
import com.nrjh.iop.modules.message.message.service.IMessageMessageService;

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

 /**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@RestController
@RequestMapping("/iop/message/message")
@Slf4j
public class MessageMessageController extends JeecgController<MessageMessage, IMessageMessageService>{
	@Autowired
	private IMessageMessageService messageMessageService;
	
	/**
	 * 分页列表查询
	 *
	 * @param messageMessage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(MessageMessage messageMessage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String pid = messageMessage.getPid();
		if (oConvertUtils.isEmpty(pid)) {
			pid = "0";
		}
		messageMessage.setPid(null);
		QueryWrapper<MessageMessage> queryWrapper = QueryGenerator.initQueryWrapper(messageMessage, req.getParameterMap());
		// 使用 eq 防止模糊查询
		queryWrapper.eq("pid", pid);
		Page<MessageMessage> page = new Page<MessageMessage>(pageNo, pageSize);
		IPage<MessageMessage> pageList = messageMessageService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
      * 获取子数据
      * @param testTree
      * @param req
      * @return
      */
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(MessageMessage messageMessage,HttpServletRequest req) {
		QueryWrapper<MessageMessage> queryWrapper = QueryGenerator.initQueryWrapper(messageMessage, req.getParameterMap());
		List<MessageMessage> list = messageMessageService.list(queryWrapper);
		return Result.ok(list);
	}
	
	
	/**
	 *   添加
	 *
	 * @param messageMessage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody MessageMessage messageMessage) {
		messageMessageService.addMessageMessage(messageMessage);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param messageMessage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody MessageMessage messageMessage) {
		messageMessageService.updateMessageMessage(messageMessage);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		messageMessageService.deleteMessageMessage(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.messageMessageService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MessageMessage messageMessage = messageMessageService.getById(id);
		if(messageMessage==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(messageMessage);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param messageMessage
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MessageMessage messageMessage) {
		return super.exportXls(request, messageMessage, MessageMessage.class, "消息");
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
		return super.importExcel(request, response, MessageMessage.class);
    }

}
