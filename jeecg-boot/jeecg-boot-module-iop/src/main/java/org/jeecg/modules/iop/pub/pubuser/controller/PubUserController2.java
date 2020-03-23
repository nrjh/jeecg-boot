package org.jeecg.modules.iop.pub.pubuser.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.iop.pub.pubuser.entity.PubUser2;
import org.jeecg.modules.iop.pub.pubuser.service.IPubUserService2;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: pub_user
 * @Author: jeecg-boot
 * @Date:   2020-02-24
 * @Version: V1.0
 */
@RestController
@RequestMapping("/iop/pub/pubuser")
@Slf4j
public class PubUserController2 extends JeecgController<PubUser2, IPubUserService2> {
	@Autowired
	private IPubUserService2 pubUserService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pubUser
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PubUser2 pubUser,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PubUser2> queryWrapper = QueryGenerator.initQueryWrapper(pubUser, req.getParameterMap());
		Page<PubUser2> page = new Page<PubUser2>(pageNo, pageSize);
		IPage<PubUser2> pageList = pubUserService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pubUser
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody PubUser2 pubUser) {
		pubUserService.save(pubUser);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pubUser
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody PubUser2 pubUser) {
		pubUserService.updateById(pubUser);
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
		pubUserService.removeById(id);
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
		this.pubUserService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PubUser2 pubUser = pubUserService.getById(id);
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
    public ModelAndView exportXls(HttpServletRequest request, PubUser2 pubUser) {
        return super.exportXls(request, pubUser, PubUser2.class, "pub_user");
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
        return super.importExcel(request, response, PubUser2.class);
    }

}
