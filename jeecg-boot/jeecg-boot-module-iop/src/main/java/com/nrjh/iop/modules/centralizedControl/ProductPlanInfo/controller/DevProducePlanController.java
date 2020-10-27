package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.controller;

import java.text.ParseException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProduceCycle;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProduceDplan;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.mapper.DevProduceDplanMapper;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service.IDevProduceCycleService;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service.IDevProduceDplanService;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service.IDevProducePlanService;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProducePlan;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: DEV_PRODUCE_PLAN
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Api(tags="DEV_PRODUCE_PLAN")
@RestController
@RequestMapping("/prod/devProducePlan")
@Slf4j
public class DevProducePlanController extends JeecgController<DevProducePlan, IDevProducePlanService> {
	@Autowired
	private IDevProducePlanService devProducePlanService;

	 @Autowired
	 private IDevProduceDplanService devProduceDplanService;

	 @Autowired
	 private IDevProduceCycleService devProduceCycleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param devProducePlan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_PLAN-分页列表查询")
	@ApiOperation(value="DEV_PRODUCE_PLAN-分页列表查询", notes="DEV_PRODUCE_PLAN-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevProducePlan devProducePlan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DevProducePlan> queryWrapper = QueryGenerator.initQueryWrapper(devProducePlan, req.getParameterMap());
		Page<DevProducePlan> page = new Page<DevProducePlan>(pageNo, pageSize);
		String categ = req.getParameter("categ");
		String produceCycleId = req.getParameter("produceCycleId");
		queryWrapper.eq("EQUIP_CATEG",categ);
		queryWrapper.eq("PRODUCE_CYCLE_ID",produceCycleId);
		queryWrapper.orderByDesc("create_time");
		IPage<DevProducePlan> pageList = devProducePlanService.page(page, queryWrapper);
		System.out.println(pageList);
		return Result.ok(pageList);
	}

	 @AutoLog(value = "DEV_PRODUCE_PLAN-提交校验")
	 @ApiOperation(value="DEV_PRODUCE_PLAN-提交校验", notes="DEV_PRODUCE_PLAN-提交校验")
	 @PostMapping(value = "/checkPlan")
	 public Result<?> checkPlan(@RequestBody DevProducePlan devProducePlan){
		 String equipCateg = devProducePlan.getEquipCateg();
		 String produceCycleId = devProducePlan.getProduceCycleId();
		 QueryWrapper queryWrapper = new QueryWrapper();
		 queryWrapper.eq("PRODUCE_CYCLE_ID",produceCycleId);
		 queryWrapper.eq("EQUIP_CATEG",equipCateg);
		 Result result = new Result();
		 result.setCode(200);
		 result.setSuccess(false);
		 if ("D".equals(equipCateg)){
			 String equipRateD = devProducePlan.getEquipRateD();
			 queryWrapper.eq("EQUIP_RATE_D",equipRateD);
			 DevProducePlan producePlan = devProducePlanService.getOne(queryWrapper);
			 if (Objects.nonNull(producePlan)) {
				 result.setMessage("单能电能表的单相频率已存在，请重新选择");
				 return result;
			 }
		}else if ("S".equals(equipCateg)){
			 String equipDescribeS = devProducePlan.getEquipDescribeS();
			 String equipRateS = devProducePlan.getEquipRateS();
			 String equipI = devProducePlan.getEquipI();
		 	 queryWrapper.eq("EQUIP_DESCRIBE_S",equipDescribeS);
		 	 queryWrapper.eq("EQUIP_RATE_S",equipRateS);
		 	 queryWrapper.eq("EQUIP_I",equipI);
			 DevProducePlan producePlan = devProducePlanService.getOne(queryWrapper);
			 if (Objects.nonNull(producePlan)) {
				 result.setMessage("三相电能表的三相物料描述、三相频率、三相电流已存在，请重新选择");
				 return result;
			 }
		}else if ("C".equals(equipCateg)){
			 String equipDescribeC = devProducePlan.getEquipDescribeC();
			 String equipRateC = devProducePlan.getEquipRateC();
			 String equipSignalC = devProducePlan.getEquipSignalC();
			 queryWrapper.eq("EQUIP_DESCRIBE_C",equipDescribeC);
			 queryWrapper.eq("EQUIP_RATE_C",equipRateC);
			 queryWrapper.eq("EQUIP_SIGNAL_C",equipSignalC);
			 DevProducePlan producePlan = devProducePlanService.getOne(queryWrapper);
			 if (Objects.nonNull(producePlan)) {
				 result.setMessage("采集终端的终端物料描述、终端载波频率、终端电压/通信方式已存在，请重新选择");
				 return result;
			 }
		}else if ("H".equals(equipCateg)){
			 String equipScale = devProducePlan.getEquipScale();
			 queryWrapper.eq("EQUIP_SCALE",equipScale);
			 DevProducePlan producePlan = devProducePlanService.getOne(queryWrapper);
			 if (Objects.nonNull(producePlan)) {
				 result.setMessage("互感器的变比已存在，请重新选择");
				 return result;
			 }
		}
		 result.setSuccess(true);
		return result;
	 }

	 @AutoLog(value = "DEV_PRODUCE_PLAN-试算")
	 @ApiOperation(value="DEV_PRODUCE_PLAN-试算", notes="DEV_PRODUCE_PLAN-试算")
	 @PostMapping(value = "/trial")
	 public Result<?> trial(@RequestParam("categ") String categ,@RequestParam("produceCycleId")String produceCycleId) throws ParseException {
		//定义一组每月天数的数组
		 int[] months = {31,28,31,30,31,30,31,31,30,31,30,31};
		 DevProduceCycle produceCycle = devProduceCycleService.getById(produceCycleId);
		 DevProduceCycle cycle = devProduceCycleService.getById(produceCycle.getParentId());
		 if (cycle == null){
		 	return Result.error("数据不存在");
		 }
		 Integer year = Integer.parseInt(cycle.getCycleYear());
		 //判断闰平年
		 if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
		 	months[1] = 29;
		 }

		 if (StringUtils.isBlank(categ) || StringUtils.isBlank(produceCycleId)){
			return Result.error("数据不存在");
		}
		//构造条件语句
		 QueryWrapper<DevProducePlan> queryWrapper = new QueryWrapper();
		 //设备类型
		 queryWrapper.eq("EQUIP_CATEG",categ);
		 //年份
		 queryWrapper.eq("CYCLE_YEAR",cycle.getCycleYear());
		 //月份
		 queryWrapper.eq("CYCLE_MONTH",produceCycle.getCycleMonth());
		 //周期
		 queryWrapper.eq("PRODUCE_CYCLE_ID",produceCycleId);
		 List<DevProducePlan> list = devProducePlanService.list(queryWrapper);
		 if (CollectionUtils.isEmpty(list)){
		 	return Result.error("数据不存在");
		 }
		 //定义总检测数和总检测天数
		 double detectionTotal = list.stream().mapToDouble(item -> Double.parseDouble(item.getEquipNumber().toString())).sum();
		 int detectionDays = list.stream().mapToInt(item -> item.getPlanDate()).sum();
		 //判断总检测天数
		 //每月所对应的天数
		 int day = months[Integer.parseInt(produceCycle.getCycleMonth()) - 1];
		 if (detectionDays > day){
		 	return Result.error(produceCycle.getCycleMonth() + "月的总检测天数不能超过" + day + "天");
		 }
		 int avg = (int) Math.ceil(detectionTotal / detectionDays);
		 detectionDays = (int) detectionTotal % avg == 0 ? (int) detectionTotal / avg : (int) detectionTotal / avg + 1;
		 if (detectionDays > day){
			 return Result.error("试算失败，" + produceCycle.getCycleMonth() + "月试算的天数不允许超过" + day + "天");
		 }
		 //删除周期和生产编号对应的数据
		 list.forEach(item -> {
		 	//条件构造器
		     QueryWrapper<DevProduceDplan> queryWrapper2 = new QueryWrapper();
		     //周期编号
		     queryWrapper2.eq("PRODUCE_CYCLE_ID",produceCycleId);
		     //生产编号
		     queryWrapper2.eq("PRODUCE_PLAN_ID",item.getId());
			 queryWrapper2.eq("EQUIP_CATEG",categ);
			 //年份
			 queryWrapper2.eq("CYCLE_YEAR",item.getCycleYear());
			 //月份
			 queryWrapper2.eq("CYCLE_MONTH",item.getCycleMonth());
		     devProduceDplanService.remove(queryWrapper2);
		 });
		 //默认数据第一条
		 DevProducePlan devProducePlan = list.get(0);
		 //修改周期和编号
		 //复制属性
		 DevProduceDplan devProduceDplan = ConvertUtils.sourceToTarget(devProducePlan, DevProduceDplan.class);
		 devProduceDplan.setProduceCycleId(produceCycleId);
		 devProduceDplan.setProducePlanId(devProducePlan.getId());
		 //获取最大的id
		 String maxId = devProduceDplanService.getMaxId();
/*		 if (StringUtils.isBlank(maxId)){
		 	maxId = "0";
		 }
		 int id = Integer.parseInt(maxId);*/
		 for (int i = 1; i <= detectionDays; i++){
		 	//随机生成id
		 	devProduceDplan.setId(System.currentTimeMillis() + "");
		 	devProduceDplan.setCycleDay( i > 9 ? i + "" : "0" + i);
			 //状态01：试算02：生效 00：删除
		 	devProduceDplan.setPlanStruts("01");
		 	devProduceDplan.setPlanDayNumber(avg);
		 	if (i == detectionDays){
				devProduceDplan.setPlanDayNumber(avg * (detectionDays  - 1) - (int) detectionTotal > 0 ? avg * (detectionDays  - 1) - (int) detectionTotal :
						avg * (detectionDays  - 1) - (int) detectionTotal < 0 ? (int) detectionTotal - avg * (detectionDays  - 1) : avg);
			}
			 devProduceDplanService.saveDPlan(devProduceDplan);
		 }
		 //设置返回值
		 return result(detectionTotal,detectionDays);
	 }

	 private Result<?> result(Double detectionTotal,int detectionDays){
		 Result<Object> r = new Result<>();
		 r.setCode(200);
		 r.setMessage("试算成功");
		 Map map = new HashMap(256);
		 map.put("detectionTotal",detectionTotal);
		 map.put("detectionDays",detectionDays);
		 r.setResult(map);
		 return r;
	 }
	
	/**
	 *   添加
	 *
	 * @param devProducePlan
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_PLAN-添加")
	@ApiOperation(value="DEV_PRODUCE_PLAN-添加", notes="DEV_PRODUCE_PLAN-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevProducePlan devProducePlan) {
		String produceCycleId = devProducePlan.getProduceCycleId();
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("PARENT_ID",produceCycleId);
		DevProduceCycle d = devProduceCycleService.getOne(queryWrapper);
		DevProduceCycle devProduceCycle = devProduceCycleService.getById(produceCycleId);
		if (devProduceCycle != null) {
			devProducePlan.setCycleMonth(devProduceCycle.getCycleMonth());
			DevProduceCycle devProduceCycle1 = devProduceCycleService.getById(devProduceCycle.getParentId());
			if (devProduceCycle1 != null) {
				devProducePlan.setCycleYear(devProduceCycle1.getCycleYear());
			}
		}
		devProducePlanService.save(devProducePlan);
		List<Map<String, Object>> mapData = devProducePlanService.selectNumMap(devProduceCycle.getId());
		System.out.println(mapData);
		devProduceCycleService.updateById(setProductCycleNum(d,mapData));
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param devProducePlan
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_PLAN-编辑")
	@ApiOperation(value="DEV_PRODUCE_PLAN-编辑", notes="DEV_PRODUCE_PLAN-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevProducePlan devProducePlan) {
		devProducePlanService.updateById(devProducePlan);
		String produceCycleId = devProducePlan.getProduceCycleId();
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("PARENT_ID",produceCycleId);
		DevProduceCycle d = devProduceCycleService.getOne(queryWrapper);
		DevProduceCycle devProduceCycle = devProduceCycleService.getById(produceCycleId);
		if (devProduceCycle != null) {
			devProducePlan.setCycleMonth(devProduceCycle.getCycleMonth());
			DevProduceCycle devProduceCycle1 = devProduceCycleService.getById(devProduceCycle.getParentId());
			if (devProduceCycle1 != null) {
				devProducePlan.setCycleYear(devProduceCycle1.getCycleYear());
			}
		}
		List<Map<String, Object>> mapData = devProducePlanService.selectNumMap(devProduceCycle.getId());
		System.out.println(mapData);
		devProduceCycleService.updateById(setProductCycleNum(d,mapData));
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_PLAN-通过id删除")
	@ApiOperation(value="DEV_PRODUCE_PLAN-通过id删除", notes="DEV_PRODUCE_PLAN-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		DevProducePlan producePlan = devProducePlanService.getById(id);
		devProducePlanService.removeById(id);
		List<Map<String, Object>> mapData = devProducePlanService.selectNumMap(producePlan.getProduceCycleId());
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("PARENT_ID",producePlan.getProduceCycleId());
		DevProduceCycle produceCycle = devProduceCycleService.getOne(queryWrapper);
		System.out.println(mapData);
		devProduceCycleService.updateById(setProductCycleNum(produceCycle,mapData));
		return Result.ok("删除成功!");
	}

	private DevProduceCycle setProductCycleNum(DevProduceCycle produceCycle,List<Map<String, Object>> mapData){
		//D：单相电能表
		//S:三相电能表
		//H:互感器
		//C:采集终端
		//数据置空
		produceCycle.setCycleSumS(0);
		produceCycle.setCycleSumD(0);
		produceCycle.setCycleSumH(0);
		produceCycle.setCycleSumC(0);

		mapData.forEach(item -> {
			if ("D".equals(item.get("CATEG"))){
				produceCycle.setCycleSumD(Integer.valueOf(item.get("NUM").toString()));
			}else if ("S".equals(item.get("CATEG"))){
				produceCycle.setCycleSumS(Integer.valueOf(item.get("NUM").toString()));
			}else if ("H".equals(item.get("CATEG"))){
				produceCycle.setCycleSumH(Integer.valueOf(item.get("NUM").toString()));
			}else if ("C".equals(item.get("CATEG"))){
				produceCycle.setCycleSumC(Integer.valueOf(item.get("NUM").toString()));
			}
		});
		return produceCycle;
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_PLAN-批量删除")
	@ApiOperation(value="DEV_PRODUCE_PLAN-批量删除", notes="DEV_PRODUCE_PLAN-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devProducePlanService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_PLAN-通过id查询")
	@ApiOperation(value="DEV_PRODUCE_PLAN-通过id查询", notes="DEV_PRODUCE_PLAN-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevProducePlan devProducePlan = devProducePlanService.getById(id);
		if(devProducePlan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(devProducePlan);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devProducePlan
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevProducePlan devProducePlan) {
        return super.exportXls(request, devProducePlan, DevProducePlan.class, "DEV_PRODUCE_PLAN");
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
        return super.importExcel(request, response, DevProducePlan.class);
    }

}
