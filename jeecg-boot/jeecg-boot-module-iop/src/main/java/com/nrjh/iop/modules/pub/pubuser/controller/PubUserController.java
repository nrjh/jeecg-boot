package com.nrjh.iop.modules.pub.pubuser.controller;

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
import com.nrjh.iop.modules.pub.pubuser.entity.PubUser;
import com.nrjh.iop.modules.pub.pubuser.service.IPubUserService;

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
 * @Description: 用户
 * @Author: jeecg-boot
 * @Date:   2020-03-13
 * @Version: V1.0
 */
@Api(tags="用户")
@RestController
@RequestMapping("/pub/pubUser")
@Slf4j
public class PubUserController extends JeecgController<PubUser, IPubUserService> {
	@Autowired
	private IPubUserService pubUserService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pubUser
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "用户-分页列表查询")
	@ApiOperation(value="用户-分页列表查询", notes="用户-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PubUser pubUser,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PubUser> queryWrapper = QueryGenerator.initQueryWrapper(pubUser, req.getParameterMap());
		Page<PubUser> page = new Page<PubUser>(pageNo, pageSize);
		IPage<PubUser> pageList = pubUserService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pubUser
	 * @return
	 */
	@AutoLog(value = "用户-添加")
	@ApiOperation(value="用户-添加", notes="用户-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PubUser pubUser) {
		pubUserService.save(pubUser);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pubUser
	 * @return
	 */
	@AutoLog(value = "用户-编辑")
	@ApiOperation(value="用户-编辑", notes="用户-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PubUser pubUser) {
		pubUserService.updateById(pubUser);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户-通过id删除")
	@ApiOperation(value="用户-通过id删除", notes="用户-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		pubUserService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用户-批量删除")
	@ApiOperation(value="用户-批量删除", notes="用户-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pubUserService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户-通过id查询")
	@ApiOperation(value="用户-通过id查询", notes="用户-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PubUser pubUser = pubUserService.getById(id);
		if(pubUser==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(pubUser);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pubUser
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PubUser pubUser) {
        return super.exportXls(request, pubUser, PubUser.class, "用户");
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
        return super.importExcel(request, response, PubUser.class);
    }

}
