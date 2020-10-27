package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProduceCycle;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.mapper.DevProduceCycleMapper;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.service.IDevProduceCycleService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.vo.TreeModel;
 /**
 * @Description: DEV_PRODUCE_CYCLE
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Api(tags="DEV_PRODUCE_CYCLE")
@RestController
@RequestMapping("/prod/devProduceCycle")
@Slf4j
public class DevProduceCycleController extends JeecgController<DevProduceCycle, IDevProduceCycleService> {
	@Autowired
	@Qualifier("devProduceCycleService")
	private IDevProduceCycleService devProduceCycleService;

	@Autowired
	private DevProduceCycleMapper devProduceCycleMapper;
	
	/**
	 * 分页列表查询
	 *
	 * @param devProduceCycle
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_CYCLE-分页列表查询")
	@ApiOperation(value="DEV_PRODUCE_CYCLE-分页列表查询", notes="DEV_PRODUCE_CYCLE-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevProduceCycle devProduceCycle,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DevProduceCycle> queryWrapper = QueryGenerator.initQueryWrapper(devProduceCycle, req.getParameterMap());
		Page<DevProduceCycle> page = new Page<DevProduceCycle>(pageNo, pageSize);
		IPage<DevProduceCycle> pageList = devProduceCycleService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 @AutoLog(value = "DEV_PRODUCE_CYCLE-菜单列表")
	 @ApiOperation(value="DEV_PRODUCE_CYCLE-菜单列表", notes="DEV_PRODUCE_CYCLE-菜单列表")
	 @GetMapping(value = "/menu")
	 private Result<?> queryMenu(){
		List<TreeModel> treeModelList = new ArrayList<>();
		 if (devProduceCycleService == null){
			 devProduceCycleService = SpringContextUtils.getBean(IDevProduceCycleService.class);
		 }

		 //组装树的头部数据
		 TreeModel model = new TreeModel();
		 model.setId("1");
		 model.setPid("0");
		 model.setTitle("生产计划");
		 model.setIsEnd("0");
		 model.setIsMonth("0");
		 model.setIsYear("1");
		 treeModelList.add(model);

		 List<TreeModel> yearModel = devProduceCycleService.selectYearList();

		 if(CollectionUtils.isEmpty(yearModel)){
		 	//数据为空，则为子节点
			 model.setIsEnd("1");
		 	return Result.ok(treeModelList);
		 }

		 List<TreeModel> monthModel = devProduceCycleService.selectMonthList();

		 if (CollectionUtils.isEmpty(monthModel)){
		 	//数据为空，则为子节点
			 treeModelList.addAll(yearModel.stream().map(item -> updateTreeModel(item,"年","1")).collect(Collectors.toList()));
		 	return Result.ok(treeModelList);
		 }
		 //将年份数据放入list中
		 treeModelList.addAll(yearModel.stream().map(item -> updateTreeModel(item,"年","0")).collect(Collectors.toList()));

		 //查询月份下所有的数据
		 List<DevProduceCycle> monthData = devProduceCycleService.selectMonthData();

		 if (CollectionUtils.isEmpty(monthData)){
			 //将月份数据加入list中
			 treeModelList.addAll(monthModel.stream().map(item -> updateTreeModel(item,"月","1")).collect(Collectors.toList()));
		 	return Result.ok(treeModelList);
		 }

		 //将月份数据加入list中
		 treeModelList.addAll(monthModel.stream().map(item -> updateTreeModel(item,"月","0")).collect(Collectors.toList()));
		 monthData.forEach(item -> {
			 treeModelList.addAll(getTreeNodes(item));
		 });
		 return Result.ok(treeModelList);
	 }

	 private TreeModel updateTreeModel(TreeModel treeModel,String str,String isEnd){
		treeModel.setTitle(treeModel.getTitle() + str);
		treeModel.setIsEnd(isEnd);
		return treeModel;
	 }

	 private List<TreeModel> getTreeNodes(DevProduceCycle devProduceCycle){
		 //D：单相电能表
		 //S:三相电能表
		 //H:互感器
		 //C:采集终端
		 List<TreeModel> tree = new ArrayList<>();
		 String[] codes = new String[]{"D","S","H","C"};
		 for (int i = 0; i < codes.length; i++){
		 	Code c = Code.get(codes[i]);
		 	String title;
		 	switch (c){
				case S: title = c.getValue() + " : " + devProduceCycle.getCycleSumS(); break;
				case C: title = c.getValue() + " : " + devProduceCycle.getCycleSumC(); break;
				case D: title = c.getValue() + " : " + devProduceCycle.getCycleSumD(); break;
				case H: title = c.getValue() + " : " + devProduceCycle.getCycleSumH(); break;
				default: title = null; break;
		 	}
			 TreeModel treeModel = new TreeModel();
			 treeModel.setId(devProduceCycle.getId() + "_" + c.getCode());
			 treeModel.setTitle(title);
			 treeModel.setIsEnd("1");
			 treeModel.setPid(devProduceCycle.getParentId());
			 treeModel.setIsYear("0");
			 treeModel.setIsMonth("0");
			 tree.add(treeModel);
		 }
		 return tree;
	 }

	 private enum Code{
		 //D：单相电能表
		 //S:三相电能表
		 //H:互感器
		 //C:采集终端
	     S("S","三相电能表"),
		 D("D","单相电能表"),
		 H("H","互感器"),
		 C("C","采集终端");

		 Code(String code,String value){
			this.code = code;
			this.value = value;
		 }

		 public static Code get(String code){
			 Code[] values = Code.values();
			 for (Code c : values){
			 	if (code.equals(c.getCode())){
			 		return c;
				}
			 }
			 return null;
		 }

		 private String value;
		private String code;

		 public String getValue() {
			 return value;
		 }

		 public String getCode() {
			 return code;
		 }

	 }
	
	/**
	 *   添加
	 *
	 * @param devProduceCycle
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_CYCLE-添加")
	@ApiOperation(value="DEV_PRODUCE_CYCLE-添加", notes="DEV_PRODUCE_CYCLE-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevProduceCycle devProduceCycle) {
		System.out.println(devProduceCycle);
		if (!StringUtils.isEmpty(devProduceCycle.getCycleMonth()) || !StringUtils.isEmpty(devProduceCycle.getCycleYear())) {
			QueryWrapper queryWrapper = new QueryWrapper();
			queryWrapper.eq("IS_DEL", "0");
			if (!StringUtils.isEmpty(devProduceCycle.getCycleYear())) {
				queryWrapper.eq("CYCLE_YEAR", devProduceCycle.getCycleYear());
			}
			queryWrapper.eq("PARENT_ID", devProduceCycle.getParentId());
			if (!StringUtils.isEmpty(devProduceCycle.getCycleMonth())) {
				queryWrapper.eq("CYCLE_MONTH", devProduceCycle.getCycleMonth());
			}
			DevProduceCycle produceCycle = devProduceCycleService.getOne(queryWrapper);
			if (Objects.nonNull(produceCycle)) {
				return Result.error("数据已存在，请重新输入！！！");
			}
		}
	    devProduceCycleService.save(devProduceCycle);
		if (!StringUtils.isEmpty(devProduceCycle.getCycleMonth())){
			DevProduceCycle devProduceCycle1 = new DevProduceCycle();
			devProduceCycle1.setId(System.currentTimeMillis() + "");
			devProduceCycle1.setParentId(devProduceCycle.getId());
			devProduceCycleService.save(devProduceCycle1);
		}
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param devProduceCycle
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_CYCLE-编辑")
	@ApiOperation(value="DEV_PRODUCE_CYCLE-编辑", notes="DEV_PRODUCE_CYCLE-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevProduceCycle devProduceCycle) {
		devProduceCycleService.updateById(devProduceCycle);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_CYCLE-通过id删除")
	@ApiOperation(value="DEV_PRODUCE_CYCLE-通过id删除", notes="DEV_PRODUCE_CYCLE-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id") String id) {
		DevProduceCycle devProduceCycle = devProduceCycleService.getById(id);
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("PARENT_ID", id);
		DevProduceCycle productionCycle = devProduceCycleService.getOne(queryWrapper);
		//是否年份下还有没有月份，有则则提示用户删除年份节点下的所有的月份后再来删除年份
		if ("1".equals(devProduceCycle.getParentId()) && Objects.nonNull(productionCycle)) {
			return Result.error("请删除年份下的所有的月份");
		}
		devProduceCycleService.removeById(id);
		devProduceCycleService.remove(queryWrapper);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_CYCLE-批量删除")
	@ApiOperation(value="DEV_PRODUCE_CYCLE-批量删除", notes="DEV_PRODUCE_CYCLE-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devProduceCycleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "DEV_PRODUCE_CYCLE-通过id查询")
	@ApiOperation(value="DEV_PRODUCE_CYCLE-通过id查询", notes="DEV_PRODUCE_CYCLE-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevProduceCycle devProduceCycle = devProduceCycleService.getById(id);
		if(devProduceCycle==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(devProduceCycle);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devProduceCycle
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevProduceCycle devProduceCycle) {
        return super.exportXls(request, devProduceCycle, DevProduceCycle.class, "DEV_PRODUCE_CYCLE");
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
        return super.importExcel(request, response, DevProduceCycle.class);
    }

}
