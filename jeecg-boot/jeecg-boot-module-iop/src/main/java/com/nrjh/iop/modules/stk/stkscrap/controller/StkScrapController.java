package com.nrjh.iop.modules.stk.stkscrap.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nrjh.iop.modules.stk.stkscrap.entity.ScrapPro;
import com.nrjh.iop.modules.stk.stkscrap.entity.StkScrap;
import com.nrjh.iop.modules.stk.stkscrap.service.IScrapProService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.nrjh.iop.modules.stk.stkscrap.service.IStkScrapService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 报废管理
 * @Author: jeecg-boot
 * @Date:   2020-08-06
 * @Version: V1.0
 */
@Api(tags="报废管理")
@RestController
@RequestMapping("/scrap/stkScrap")
@Slf4j
public class StkScrapController extends JeecgController<StkScrap, IStkScrapService> {
	@Autowired
	private IStkScrapService stkScrapService;

	@Autowired
	private IScrapProService scrapProService;


	/**
	 * 分页列表查询
	 *
	 * @param stkScrap
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "报废管理-分页列表查询")
	@ApiOperation(value="报废管理-分页列表查询", notes="报废管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StkScrap stkScrap,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StkScrap> queryWrapper = QueryGenerator.initQueryWrapper(stkScrap, req.getParameterMap());
		Page<StkScrap> page = new Page<StkScrap>(pageNo, pageSize);
		IPage<StkScrap> pageList = stkScrapService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
	  * 物料信息列表查询
	  * @param scrapPro
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "报废管理-分页物料信息列表查询")
	 @ApiOperation(value="报废管理-分页物料信息列表查询", notes="报废管理-分页物料信息列表查询")
	 @GetMapping(value = "/wlList")
	 //@RequestBody(required = false) ScrapPro scrapPro,
	 //									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
	 //									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
	 //									  HttpServletRequest req
	 public Result<?> queryWlPageList( ScrapPro scrapPro,
									   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									   	HttpServletRequest req) {
		 //QueryWrapper<ScrapPro> queryWrapper = QueryGenerator.initQueryWrapper(scrapPro, req.getParameterMap());
		 //Page<ScrapPro> page = new Page<ScrapPro>(pageNo,pageSize);
		 //List scrapProList = scrapProService.queryProdList(scrapPro);
		 //return Result.ok(scrapProList);

		 Result<List<ScrapPro>> result=new Result<>();
		 result.setCode(0);
		 List<ScrapPro> records=scrapProService.queryProdList(scrapPro);
		 result.setResult(records);
		 return result;

	 }

	 /**
	  * 保存报废物料信息
	  * @return
	  */
	 @AutoLog(value = "报废管理-保存报废物料信息")
	 @ApiOperation(value="报废管理-保存报废物料信息", notes="报废管理-保存报废物料信息")
	 @RequestMapping(value = "/save",method = RequestMethod.POST)
	 public Result<?> saveScrapProList(@RequestBody List<ScrapPro> scrapProList) {
		for(int i = 0; i < scrapProList.size(); i++){
			scrapProService.saveProdList(scrapProList.get(i));
		}
		 return Result.ok("保存成功!");
	 }

	 /**
	  * 查看报废物料信息
	  * 1.通过勾选列表取第一个字段获取报废单号，查询报废表的基本信息回显到弹出表单
	  * 2.通过报废单号查看报废明细,回显到物料列表
	  * @return
	  */
	 @AutoLog(value = "报废管理-查看报废主表信息")
	 @ApiOperation(value="报废管理-查看报废主表信息", notes="报废管理-查看报废主表信息")
	 @RequestMapping(value = "/look")
	 public Result<?> lookStkScrap(@RequestParam(name="id",required=true) String id) {
	 	StkScrap stkScrap = stkScrapService.getById(id);
	 	if(stkScrap==null) {
	 		return Result.error("未找到对应数据");
	 	}
	 	return Result.ok(stkScrap);
	 }

	 @AutoLog(value = "报废管理-查看报废物料信息列表")
	 @ApiOperation(value="报废管理-查看报废物料信息列表", notes="报废管理-查看报废物料信息列表")
	 @RequestMapping(value = "/lookList")
	 public Result<?> lookScrapPro(@RequestParam(name="scrapInventoryOrder",required=true) String scrapInventoryOrder) {
		 Result<List<ScrapPro>> result=new Result<>();
		 result.setCode(0);
		 List<ScrapPro> records=scrapProService.lookScrapPro(scrapInventoryOrder);
		 result.setResult(records);
		 return result;
	 }

	 /**
	  * 提交报废物料信息
	  *
	  * 把当前时间和操作人信息保存到主表
	  * 更改主表的状态字段
	  * @return
	  */
	 @AutoLog(value = "报废管理-提交报废物料信息")
	 @ApiOperation(value="报废管理-提交报废物料信息", notes="报废管理-提交报废物料信息")
	 @RequestMapping(value = "/submit")
	 public Result<?> submitScrapPro(@RequestBody StkScrap stkScrap){

		 Boolean b = scrapProService.submitScrapPro(stkScrap);
		 if(!b){
		 	return Result.error("提交失败！");
		 }else {
			 return Result.ok("提交成功！");
		 }
	 }

	 /**
	  * 确认单据状态
	  *
	  * @return
	  */
	 @AutoLog(value = "报废管理-确认单据状态")
	 @ApiOperation(value="报废管理-确认单据状态", notes="报废管理-确认单据状态")
	 @RequestMapping(value = "/sure")
	 public Result<?> sureScrapPro(@RequestParam(name="id",required=true) String id) throws BusinessException {

		 Boolean b = scrapProService.sureScrapPro(id);
		 if(!b){
			 return Result.error("确认失败！");
		 }else {
			 return Result.ok("确认成功！");
		 }
	 }


	 /**
	  * 审核单据
	  *
	  * @return
	  */
	 @AutoLog(value = "报废管理-审核单据")
	 @ApiOperation(value="报废管理-审核单据", notes="报废管理-审核单据")
	 @RequestMapping(value = "/shenhe")
	 public Result<?> shenheScrapPro(@RequestParam(name="id",required=true) String id,@RequestParam(name="info",required=true) String info) throws BusinessException {

		 Boolean b = scrapProService.shenheScrapPro(id,info);
		 if(!b){
			 return Result.error("审核失败！");
		 }else {
			 return Result.ok("审核成功！");
		 }
	 }

	 /**
	  * 作废单据
	  *
	  * @return
	  */
	 @AutoLog(value = "报废管理-作废单据")
	 @ApiOperation(value="报废管理-作废单据", notes="报废管理-作废单据")
	 @RequestMapping(value = "/bad")
	 public Result<?> badScrapPro(@RequestParam(name="id",required=true) String id) throws BusinessException {

		 Boolean b = scrapProService.badScrapPro(id);
		 if(!b){
			 return Result.error("作废失败！");
		 }else {
			 return Result.ok("作废成功！");
		 }
	 }

	/**
	 *   添加
	 *
	 * @param stkScrap
	 * @return
	 */
	@AutoLog(value = "报废管理-添加")
	@ApiOperation(value="报废管理-添加", notes="报废管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StkScrap stkScrap) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录人
		stkScrap.setApplicationWorker(sysUser.getRealname());
		stkScrap.setApplicationDate(new Date());//设置当前时间
		stkScrapService.save(stkScrap);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param stkScrap
	 * @return
	 */
	@AutoLog(value = "报废管理-编辑")
	@ApiOperation(value="报废管理-编辑", notes="报废管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StkScrap stkScrap) {
		stkScrapService.updateById(stkScrap);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "报废管理-通过id删除")
	@ApiOperation(value="报废管理-通过id删除", notes="报废管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		stkScrapService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "报废管理-批量删除")
	@ApiOperation(value="报废管理-批量删除", notes="报废管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.stkScrapService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "报废管理-通过id查询")
	@ApiOperation(value="报废管理-通过id查询", notes="报废管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StkScrap stkScrap = stkScrapService.getById(id);
		if(stkScrap==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(stkScrap);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param stkScrap
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StkScrap stkScrap) {
        return super.exportXls(request, stkScrap, StkScrap.class, "报废管理");
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
        return super.importExcel(request, response, StkScrap.class);
    }

}
