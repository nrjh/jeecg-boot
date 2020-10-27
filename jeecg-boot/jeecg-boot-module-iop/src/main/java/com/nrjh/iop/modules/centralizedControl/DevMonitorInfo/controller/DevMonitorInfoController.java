package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.zxing.WriterException;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.dto.DevMonitorVo;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.DevMonitorInfo;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.EquipRemoteUser;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.IDevMonitorInfoService;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.IEquipRemoteUserService;
import com.nrjh.iop.modules.util.QRCodeGenerator.QRCodeGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @Description: 监控设备管理
 * @Author: jeecg-boot
 * @Date:   2020-09-11
 * @Version: V1.0
 */
@Api(tags="DEV_MONITOR_INFO")
@RestController
@RequestMapping("/DevMonitorInfo/devMonitorInfo")
@Slf4j
public class DevMonitorInfoController extends JeecgController<DevMonitorInfo, IDevMonitorInfoService> {
	@Autowired
	private IDevMonitorInfoService devMonitorInfoService;

	@Autowired
	private ISysBaseAPI  iSysBaseAPI;
	@Autowired
	private IEquipRemoteUserService iEquipRemoteUserService;

	/**
	 * 分页列表查询
	 *
	 * @param devMonitorInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "监控设备管理-分页列表查询")
	@ApiOperation(value="监控设备管理-分页列表查询", notes="监控设备管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DevMonitorInfo devMonitorInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DevMonitorInfo> queryWrapper = QueryGenerator.initQueryWrapper(devMonitorInfo, req.getParameterMap());
		Page<DevMonitorInfo> page = new Page<DevMonitorInfo>(pageNo, pageSize);
		IPage<DevMonitorInfo> pageList = devMonitorInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param devMonitorInfo
	 * @return
	 */
	@AutoLog(value = "监控设备管理-添加")
	@ApiOperation(value="监控设备管理-添加", notes="监控设备管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DevMonitorInfo devMonitorInfo) {
		devMonitorInfo.setEquipName(devMonitorInfo.getEquipNo());
		devMonitorInfoService.save(devMonitorInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param devMonitorInfo
	 * @return
	 */
	@AutoLog(value = "监控设备管理-编辑")
	@ApiOperation(value="监控设备管理-编辑", notes="监控设备管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DevMonitorInfo devMonitorInfo) {
		devMonitorInfo.setEquipName(devMonitorInfo.getEquipNo());
		devMonitorInfoService.updateById(devMonitorInfo);
		return Result.ok("编辑成功!");
	}

	/**
	 * 更新设备 检定线 使用状态
	 */
	@PutMapping(value = "/updateEquiopStatue")
	public Result<?> updateEquiopStatue(@RequestBody EquipRemoteUser equipRemoteUser) {
		equipRemoteUser.setStatue("1");
		QueryWrapper<EquipRemoteUser> queryWrapper = new QueryWrapper<EquipRemoteUser>();
		queryWrapper.eq("AREA_ID",equipRemoteUser.getAreaId());
		queryWrapper.eq("EQUIP_NO",equipRemoteUser.getEquipNo());
	    List<EquipRemoteUser> list=	iEquipRemoteUserService.list(queryWrapper);
	    if(CollectionUtils.isEmpty(list)){
			iEquipRemoteUserService.save(equipRemoteUser);
		}else {
			equipRemoteUser.setId(list.get(0).getId());
			iEquipRemoteUserService.updateById(equipRemoteUser);
		}
		Result result = new Result();
		result.setCode(200);
		result.setMessage("占用成功!");
		result.setSuccess(true);
		result.setResult(list);
		return result;
	}

	/**
	 * 更新设备 检定线 使用状态
	 */
	@PutMapping(value = "/cancleEquiopStatue")
	public Result<?> cancleEquiopStatue(@RequestBody EquipRemoteUser equipRemoteUser) {
		equipRemoteUser.setStatue("0");
		QueryWrapper<EquipRemoteUser> queryWrapper = new QueryWrapper<EquipRemoteUser>();
		queryWrapper.eq("AREA_ID",equipRemoteUser.getAreaId());
		queryWrapper.eq("EQUIP_NO",equipRemoteUser.getEquipNo());
		List<EquipRemoteUser> list=	iEquipRemoteUserService.list(queryWrapper);
		if(CollectionUtils.isEmpty(list)){
			iEquipRemoteUserService.save(equipRemoteUser);
		}else {
			equipRemoteUser.setId(list.get(0).getId());
			iEquipRemoteUserService.updateById(equipRemoteUser);
		}
		return Result.ok("释放成功!");
	}

	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监控设备管理-通过id删除")
	@ApiOperation(value="监控设备管理-通过id删除", notes="监控设备管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		devMonitorInfoService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "监控设备管理-批量删除")
	@ApiOperation(value="监控设备管理-批量删除", notes="监控设备管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.devMonitorInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监控设备管理-通过id查询")
	@ApiOperation(value="监控设备管理-通过id查询", notes="监控设备管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		DevMonitorInfo devMonitorInfo = devMonitorInfoService.getById(id);
		if(devMonitorInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(devMonitorInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param devMonitorInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DevMonitorInfo devMonitorInfo) {
        return super.exportXls(request, devMonitorInfo, DevMonitorInfo.class, "监控设备管理");
    }


	@GetMapping(value="/qrimage")
	public  Result<?> getQRImage(String equipNo) {

		//二维码内的信息
		if(equipNo==null){
			equipNo="this is null img";
		}

//		byte[] qrcode = null;
//		try {
//			qrcode = QRCodeGenerator.getQRCodeImage(equipNo, 360, 360);
//		} catch (WriterException e) {
//			System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
//			e.printStackTrace();
//		} catch (IOException e) {
//			System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
//		}
//		final HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.IMAGE_PNG);
		String base64String = QRCodeGenerator.getImage2Base64String(QRCodeGenerator.generateBarcodeWithoutWhite(equipNo, Color.BLACK));


		return Result.ok(base64String);
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
        return super.importExcel(request, response, DevMonitorInfo.class);
    }


	 @AutoLog(value = "集控管理-左屏")
	 @ApiOperation(value="集控管理-左屏", notes="集控管理-左屏")
	 @GetMapping(value = "/queryDevList")
	 public Result<?> queryDevList() {

		 DevMonitorVo devResult=new DevMonitorVo();
		 List<DevMonitorVo> list=devMonitorInfoService.queryDevList(null);

		 List<DictModel>  areaResult = iSysBaseAPI.queryDictItemsByCode("AREA_ID");

		 List<DevMonitorVo> list1=new ArrayList<DevMonitorVo>();//采集Ⅰ线综合检定区
		 List<DevMonitorVo> list2=new ArrayList<DevMonitorVo>();//采集Ⅱ线综合检定区
		 List<DevMonitorVo> list3=new ArrayList<DevMonitorVo>();//三相Ⅰ线综合检定区
		 List<DevMonitorVo> list4=new ArrayList<DevMonitorVo>();//三相Ⅱ线综合检定区
		 List<DevMonitorVo> list5=new ArrayList<DevMonitorVo>();//单相Ⅰ线综合检定区
		 List<DevMonitorVo> list6=new ArrayList<DevMonitorVo>();//单相Ⅱ线综合检定区
		 List<DevMonitorVo> list7=new ArrayList<DevMonitorVo>();//互感器
		 List<DevMonitorVo> list8=new ArrayList<DevMonitorVo>();//智能仓储
			 if(!CollectionUtils.isEmpty(list)){
				for(int i=0;i<list.size();i++){
					DevMonitorVo devMonitorVo=list.get(i);
//					if(i % 2==0){
//						devMonitorVo.setDirection("down");
//					}else {
//						devMonitorVo.setDirection("up");
//					}
					if(StringUtils.equals(areaResult.get(0).getValue(),devMonitorVo.getAreaName())){
						list1.add(devMonitorVo);
					}
					else if (StringUtils.equals(areaResult.get(1).getValue(),devMonitorVo.getAreaName())){
						list2.add(devMonitorVo);
					}else if (StringUtils.equals(areaResult.get(2).getValue(),devMonitorVo.getAreaName())){
						list3.add(devMonitorVo);
					}else if (StringUtils.equals(areaResult.get(3).getValue(),devMonitorVo.getAreaName())){
						list4.add(devMonitorVo);
					}else if (StringUtils.equals(areaResult.get(4).getValue(),devMonitorVo.getAreaName())){
						list5.add(devMonitorVo);
					}else if (StringUtils.equals(areaResult.get(5).getValue(),devMonitorVo.getAreaName())){
						list6.add(devMonitorVo);
					}else if (StringUtils.equals(areaResult.get(6).getValue(),devMonitorVo.getAreaName())){
						list7.add(devMonitorVo);
					}else if (StringUtils.equals(areaResult.get(7).getValue(),devMonitorVo.getAreaName())){
						list8.add(devMonitorVo);
					}
				}
			}
		 devResult.setListCollectionOne(totalFaulNum(list1));
		 devResult.setListCollectionTwo(totalFaulNum(list2));
		 devResult.setListThreephaseOne(totalFaulNum(list3));
		 devResult.setListThreephaseTwo(totalFaulNum(list4));
		 devResult.setListSimplexOne(totalFaulNum(list5));
		 devResult.setListSimplexTwo(totalFaulNum(list6));
		 devResult.setListTransformer(totalFaulNum(list7));
		 devResult.setListcangchu(totalFaulNum(list8));
		 return Result.ok(devResult);
	 }


	 public List<DevMonitorVo> totalFaulNum(List<DevMonitorVo> list){
    	int num=0;
    	if(!CollectionUtils.isEmpty(list)){
    		for(DevMonitorVo  devMonitorVo :list){
    			if("0".equals(devMonitorVo.getIsHandled())){
					num++;
				}
			}
			list.get(0).setFaultNum(String.valueOf(num));
		}
    	return  list;
	 }

}
