package com.nrjh.iop.modules.msg.message.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.msg.message.entity.MsgTrackingValue;
import com.nrjh.iop.modules.msg.message.entity.MsgMessage;
import com.nrjh.iop.modules.msg.message.vo.MsgMessagePage;
import com.nrjh.iop.modules.msg.message.service.IMsgMessageService;
import com.nrjh.iop.modules.msg.message.service.IMsgTrackingValueService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Api(tags="消息")
@RestController
@RequestMapping("/iop/msg/message")
@Slf4j
public class MsgMessageController {
	@Autowired
	private IMsgMessageService msgMessageService;
	@Autowired
	private IMsgTrackingValueService msgTrackingValueService;
	
	/**
	 * 分页列表查询
	 *
	 * @param msgMessage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "消息-分页列表查询")
	@ApiOperation(value="消息-分页列表查询", notes="消息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MsgMessage msgMessage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MsgMessage> queryWrapper = QueryGenerator.initQueryWrapper(msgMessage, req.getParameterMap());
		Page<MsgMessage> page = new Page<MsgMessage>(pageNo, pageSize);
		IPage<MsgMessage> pageList = msgMessageService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param msgMessagePage
	 * @return
	 */
	@AutoLog(value = "消息-添加")
	@ApiOperation(value="消息-添加", notes="消息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody MsgMessagePage msgMessagePage) {
		/**
		 * 此功能暂时只支持  旧文本  新文本 字段存储，无论什么类型都存储到这两个字段
		 */
		MsgMessage msgMessage = new MsgMessage();
		BeanUtils.copyProperties(msgMessagePage, msgMessage);
		msgMessageService.saveMain(msgMessage, msgMessagePage.getMsgTrackingValueList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param msgMessagePage
	 * @return
	 */
	@AutoLog(value = "消息-编辑")
	@ApiOperation(value="消息-编辑", notes="消息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody MsgMessagePage msgMessagePage) {
		MsgMessage msgMessage = new MsgMessage();
		BeanUtils.copyProperties(msgMessagePage, msgMessage);
		MsgMessage msgMessageEntity = msgMessageService.getById(msgMessage.getId());
		if(msgMessageEntity==null) {
			return Result.error("未找到对应数据");
		}
		msgMessageService.updateMain(msgMessage, msgMessagePage.getMsgTrackingValueList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "消息-通过id删除")
	@ApiOperation(value="消息-通过id删除", notes="消息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		msgMessageService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "消息-批量删除")
	@ApiOperation(value="消息-批量删除", notes="消息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.msgMessageService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "消息-通过id查询")
	@ApiOperation(value="消息-通过id查询", notes="消息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MsgMessage msgMessage = msgMessageService.getById(id);
		if(msgMessage==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(msgMessage);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "消息值变更记录集合-通过id查询")
	@ApiOperation(value="消息值变更记录集合-通过id查询", notes="消息值变更记录-通过id查询")
	@GetMapping(value = "/queryMsgTrackingValueByMainId")
	public Result<?> queryMsgTrackingValueListByMainId(@RequestParam(name="id",required=true) String id) {
		List<MsgTrackingValue> msgTrackingValueList = msgTrackingValueService.selectByMainId(id);
		return Result.ok(msgTrackingValueList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param msgMessage
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MsgMessage msgMessage) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<MsgMessage> queryWrapper = QueryGenerator.initQueryWrapper(msgMessage, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<MsgMessage> queryList = msgMessageService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<MsgMessage> msgMessageList = new ArrayList<MsgMessage>();
      if(oConvertUtils.isEmpty(selections)) {
          msgMessageList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          msgMessageList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<MsgMessagePage> pageList = new ArrayList<MsgMessagePage>();
      for (MsgMessage main : msgMessageList) {
          MsgMessagePage vo = new MsgMessagePage();
          BeanUtils.copyProperties(main, vo);
          List<MsgTrackingValue> msgTrackingValueList = msgTrackingValueService.selectByMainId(main.getId());
          vo.setMsgTrackingValueList(msgTrackingValueList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "消息列表");
      mv.addObject(NormalExcelConstants.CLASS, MsgMessagePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("消息数据", "导出人:"+sysUser.getRealname(), "消息"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<MsgMessagePage> list = ExcelImportUtil.importExcel(file.getInputStream(), MsgMessagePage.class, params);
              for (MsgMessagePage page : list) {
                  MsgMessage po = new MsgMessage();
                  BeanUtils.copyProperties(page, po);
                  msgMessageService.saveMain(po, page.getMsgTrackingValueList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
    }

}
