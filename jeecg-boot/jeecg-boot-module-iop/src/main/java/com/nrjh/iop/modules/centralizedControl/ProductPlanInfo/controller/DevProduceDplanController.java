package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProduceDplan;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service.IDevProduceDplanService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: DEV_PRODUCE_DPLAN
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Api(tags="DEV_PRODUCE_DPLAN")
@RestController
@RequestMapping("/prod/devProduceDplan")
@Slf4j
public class DevProduceDplanController extends JeecgController<DevProduceDplan, IDevProduceDplanService> {
	@Autowired
	private IDevProduceDplanService devProduceDplanService;
	
	/**
	 * 分页列表查询
	 *
	 * @param devProduceDplan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_DPLAN-分页列表查询")
	@ApiOperation(value="DEV_PRODUCE_DPLAN-分页列表查询", notes="DEV_PRODUCE_DPLAN-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevProduceDplan devProduceDplan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String categ = req.getParameter("categ");
		String produceCycleId = req.getParameter("produceCycleId");
		List<DevProduceDplan> records = devProduceDplanService.detailList(categ,produceCycleId);
		Map<String,Object> recordMap = new HashMap<>(256);
		if (records == null){
			recordMap.put("records",null);
			return Result.ok(recordMap);
		}
		records.forEach(item -> {
		    item.setCycleDay(item.getCycleMonth() + "月" + item.getCycleDay() + "日");
		});
		recordMap.put("records",records);
		return Result.ok(recordMap);
	}
	
	/**
	 *   添加
	 *
	 * @param devProduceDplan
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_DPLAN-添加")
	@ApiOperation(value="DEV_PRODUCE_DPLAN-添加", notes="DEV_PRODUCE_DPLAN-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevProduceDplan devProduceDplan) {
		devProduceDplanService.save(devProduceDplan);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param devProduceDplan
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_DPLAN-编辑")
	@ApiOperation(value="DEV_PRODUCE_DPLAN-编辑", notes="DEV_PRODUCE_DPLAN-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevProduceDplan devProduceDplan) {
		devProduceDplanService.updateById(devProduceDplan);
		return Result.ok("编辑成功!");
	}

	 @AutoLog(value = "DEV_PRODUCE_DPLAN-合成")
	 @ApiOperation(value="DEV_PRODUCE_DPLAN-合成", notes="DEV_PRODUCE_DPLAN-合成")
	 @PostMapping(value = "/synthesis")
	 public Result<?> synthesis(@RequestParam("ids") String[] ids) {
		 DevProduceDplan produceDplan = devProduceDplanService.getById(ids[0]);
		 List<DevProduceDplan> list = new ArrayList<>();
		 for (String id : ids){
			 list.add(devProduceDplanService.getById(id));
		 }
		 //检测数量
		 int num = list.stream().mapToInt(i -> i.getPlanDayNumber()).sum();
		 produceDplan.setPlanDayNumber(num);
		 devProduceDplanService.removeByIds(Arrays.asList(ids));
		 devProduceDplanService.save(produceDplan);
		 return Result.ok("合成成功!");
	 }

	 @AutoLog(value = "DEV_PRODUCE_DPLAN-提交")
	 @ApiOperation(value="DEV_PRODUCE_DPLAN-提交", notes="DEV_PRODUCE_DPLAN-提交")
	 @PostMapping(value = "/submit")
	 public Result<?> submit(@RequestParam("ids") String[] ids) {
		 for (String id : ids) {
			 DevProduceDplan produceDplan = devProduceDplanService.getById(id);
			 //02生效
			 produceDplan.setPlanStruts("02");
			 devProduceDplanService.updateById(produceDplan);
		 }
		 return Result.ok("提交成功!");
	 }
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_DPLAN-通过id删除")
	@ApiOperation(value="DEV_PRODUCE_DPLAN-通过id删除", notes="DEV_PRODUCE_DPLAN-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		devProduceDplanService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_DPLAN-批量删除")
	@ApiOperation(value="DEV_PRODUCE_DPLAN-批量删除", notes="DEV_PRODUCE_DPLAN-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devProduceDplanService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_DPLAN-通过id查询")
	@ApiOperation(value="DEV_PRODUCE_DPLAN-通过id查询", notes="DEV_PRODUCE_DPLAN-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevProduceDplan devProduceDplan = devProduceDplanService.getById(id);
		if(devProduceDplan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(devProduceDplan);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devProduceDplan
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevProduceDplan devProduceDplan) {
        return super.exportXls(request, devProduceDplan, DevProduceDplan.class, "DEV_PRODUCE_DPLAN");
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
        return super.importExcel(request, response, DevProduceDplan.class);
    }

}
