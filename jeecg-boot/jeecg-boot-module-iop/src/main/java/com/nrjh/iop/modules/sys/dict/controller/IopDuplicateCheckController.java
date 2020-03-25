package com.nrjh.iop.modules.sys.dict.controller;

import com.nrjh.iop.modules.sys.dict.model.IopDuplicateCheckVo;
import com.nrjh.iop.modules.sys.dict.service.IIopDuplicateCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Title: DuplicateCheckAction
 * @Description: 重复校验工具
 * @Author 张代浩
 * @Date 2019-03-25
 * @Version V1.0
 */
@Slf4j
@RestController
@RequestMapping("/iop/sys/duplicate")
@Api(tags="重复校验")
public class IopDuplicateCheckController {

	@Autowired
	IIopDuplicateCheckService iopDuplicateCheckService;

	/**
	 * 校验数据是否在系统中是否存在
	 * 
	 * @return
	 */
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	@ApiOperation("重复校验接口")
	public Result<Object> doDuplicateCheck(IopDuplicateCheckVo iopDuplicateCheckVo, HttpServletRequest request) {
		Long num = null;

		log.info("----duplicate check------："+ iopDuplicateCheckVo.toString());
		if (StringUtils.isNotBlank(iopDuplicateCheckVo.getDataId())) {
			// [2].编辑页面校验
			num = iopDuplicateCheckService.duplicateCheckCountSql(iopDuplicateCheckVo);
		} else {
			// [1].添加页面校验
			num = iopDuplicateCheckService.duplicateCheckCountSqlNoDataId(iopDuplicateCheckVo);
		}

		if (num == null || num == 0) {
			// 该值可用
			return Result.ok("该值可用！");
		} else {
			// 该值不可用
			log.info("该值不可用，系统中已存在！");
			return Result.error("该值不可用，系统中已存在！");
		}
	}
}
