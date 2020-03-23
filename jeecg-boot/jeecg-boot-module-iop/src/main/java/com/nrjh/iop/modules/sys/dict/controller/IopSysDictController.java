package com.nrjh.iop.modules.sys.dict.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.sys.dict.entry.IopSysDict;
import com.nrjh.iop.modules.sys.dict.model.IopSysDictTree;
import com.nrjh.iop.modules.sys.dict.service.IIopSysDictItemService;
import com.nrjh.iop.modules.sys.dict.service.IIopSysDictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@RestController
@RequestMapping("/iop/sys/dict")
@Slf4j
public class IopSysDictController {

	@Autowired
	private IIopSysDictService iopSysDictService;
	@Autowired
	private IIopSysDictItemService iopSysDictItemService;
	@Autowired
	public RedisTemplate<String, Object> redisTemplate;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<IopSysDict>> queryPageList(IopSysDict iopSysDict, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<IopSysDict>> result = new Result<IPage<IopSysDict>>();
		QueryWrapper<IopSysDict> queryWrapper = QueryGenerator.initQueryWrapper(iopSysDict, req.getParameterMap());
		Page<IopSysDict> page = new Page<IopSysDict>(pageNo, pageSize);
		IPage<IopSysDict> pageList = iopSysDictService.page(page, queryWrapper);
		log.debug("查询当前页："+pageList.getCurrent());
		log.debug("查询当前页数量："+pageList.getSize());
		log.debug("查询结果数量："+pageList.getRecords().size());
		log.debug("数据总数："+pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * @功能：获取树形字典数据
	 * @param iopSysDict
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/treeList", method = RequestMethod.GET)
	public Result<List<IopSysDictTree>> treeList(IopSysDict iopSysDict, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
		Result<List<IopSysDictTree>> result = new Result<>();
		LambdaQueryWrapper<IopSysDict> query = new LambdaQueryWrapper<>();
		// 构造查询条件
		String dictName = iopSysDict.getDictName();
		if(oConvertUtils.isNotEmpty(dictName)) {
			query.like(true, IopSysDict::getDictName, dictName);
		}
		query.orderByDesc(true, IopSysDict::getCreateTime);
		List<IopSysDict> list = iopSysDictService.list(query);
		List<IopSysDictTree> treeList = new ArrayList<>();
		for (IopSysDict node : list) {
			treeList.add(new IopSysDictTree(node));
		}
		result.setSuccess(true);
		result.setResult(treeList);
		return result;
	}

	/**
	 * 获取字典数据
	 * @param dictCode 字典code
	 * @param dictCode 表名,文本字段,code字段  | 举例：sys_user,realname,id
	 * @return
	 */
	@RequestMapping(value = "/getDictItems/{dictCode}", method = RequestMethod.GET)
	public Result<List<DictModel>> getDictItems(@PathVariable String dictCode) {
		log.info(" dictCode : "+ dictCode);
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		List<DictModel> ls = null;
		try {
			if(dictCode.indexOf(",")!=-1) {
				//关联表字典（举例：sys_user,realname,id）
				String[] params = dictCode.split(",");
				
				if(params.length<3) {
					result.error500("字典Code格式不正确！");
					return result;
				}
				//SQL注入校验（只限制非法串改数据库）
				final String[] sqlInjCheck = {params[0],params[1],params[2]};
				SqlInjectionUtil.filterContent(sqlInjCheck);
				
				if(params.length==4) {
					//SQL注入校验（查询条件SQL 特殊check，此方法仅供此处使用）
					SqlInjectionUtil.specialFilterContent(params[3]);
					ls = iopSysDictService.queryTableDictItemsByCodeAndFilter(params[0],params[1],params[2],params[3]);
				}else if (params.length==3) {
					ls = iopSysDictService.queryTableDictItemsByCode(params[0],params[1],params[2]);
				}else{
					result.error500("字典Code格式不正确！");
					return result;
				}
			}else {
				//字典表
				 ls = iopSysDictService.queryDictItemsByCode(dictCode);
			}

			 result.setSuccess(true);
			 result.setResult(ls);
			 log.info(result.toString());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
			return result;
		}

		return result;
	}

	/**
	 * 获取字典数据
	 * @param dictCode
	 * @return
	 */
	@RequestMapping(value = "/getDictText/{dictCode}/{key}", method = RequestMethod.GET)
	public Result<String> getDictItems(@PathVariable("dictCode") String dictCode, @PathVariable("key") String key) {
		log.info(" dictCode : "+ dictCode);
		Result<String> result = new Result<String>();
		String text = null;
		try {
			text = iopSysDictService.queryDictTextByKey(dictCode, key);
			 result.setSuccess(true);
			 result.setResult(text);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
			return result;
		}
		return result;
	}



}
