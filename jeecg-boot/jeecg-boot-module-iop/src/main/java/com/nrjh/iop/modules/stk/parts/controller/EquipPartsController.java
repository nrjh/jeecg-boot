package com.nrjh.iop.modules.stk.parts.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nrjh.iop.modules.stk.parts.entity.EquipParts;
import com.nrjh.iop.modules.stk.parts.entity.vo.EquipPartsVO;
import com.nrjh.iop.modules.stk.parts.entity.vo.StkSparePartsVO;
import com.nrjh.iop.modules.stk.parts.mapper.EquipPartsMapper;
import com.nrjh.iop.modules.stk.parts.service.IEquipPartsService;
import com.nrjh.iop.modules.stk.spareparts.entity.StkSpareParts;
import com.nrjh.iop.modules.stk.spareparts.service.IStkSparePartsService;
import com.nrjh.iop.modules.stk.stkscrap.entity.ScrapPro;
import com.nrjh.iop.modules.stk.stkscrap.service.IScrapProService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: O_EQUIP_PARTS
 * @Author: jeecg-boot
 * @Date:   2020-09-30
 * @Version: V1.0
 */
@Api(tags="O_EQUIP_PARTS")
@RestController
@RequestMapping("/parts/equipParts")
@Slf4j
public class EquipPartsController extends JeecgController<EquipParts, IEquipPartsService> {
	@Autowired
	private IEquipPartsService equipPartsService;

	 @Autowired
	 private IStkSparePartsService stkSparePartsService;

	 @Autowired
	 private IScrapProService scrapProService;
	/**
	 * 分页列表查询
	 *
	 * @param equipParts
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "O_EQUIP_PARTS-分页列表查询")
	@ApiOperation(value="O_EQUIP_PARTS-分页列表查询", notes="O_EQUIP_PARTS-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(EquipParts equipParts,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<EquipParts> queryWrapper = QueryGenerator.initQueryWrapper(equipParts, req.getParameterMap());
		Page<EquipParts> page = new Page<EquipParts>(pageNo, pageSize);
		IPage<EquipParts> pageList = equipPartsService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 @AutoLog(value = "O_EQUIP_PARTS-树列表")
	 @ApiOperation(value="O_EQUIP_PARTS-树列表", notes="O_EQUIP_PARTS-树列表")
	 @GetMapping(value = "/tree")
	 public Result<?> tree() {
		return Result.ok(equipPartsService.selectTreeList());
	 }
	
	/**
	 *   添加
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "O_EQUIP_PARTS-添加")
	@ApiOperation(value="O_EQUIP_PARTS-添加", notes="O_EQUIP_PARTS-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody EquipPartsVO equipPartsVO) {
		//equipPartsService.save(equipParts);
		System.out.println("-----------------------------------------");
		System.out.println(equipPartsVO);
		System.out.println("--------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		//增加备品备件
		EquipParts equipParts = new EquipParts();
		equipParts.setEquipId(equipPartsVO.getEquipId());
		equipParts.setPartsName(equipPartsVO.getPartsName());
		equipParts.setPartsNo(equipPartsVO.getPartsNo());
		equipParts.setPartsSort(equipParts.getPartsSort());
		//equipParts.setPartsType(equipParts.getPartsType());
		equipParts.setNewFlag("1");
		equipParts.setOptDate(new Date());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录人
		equipParts.setOptCode(sysUser.getRealname());
		equipPartsService.save(equipParts);
		List<StkSparePartsVO> stkSparePartsList = equipPartsVO.getStkSparePartsList();
		List<Map<String,Object>> list = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(stkSparePartsList)) {
			for (StkSparePartsVO stkSparePartsVO : stkSparePartsList) {
				Map<String, Object> map = new HashMap<>();
				map.put("prId", stkSparePartsVO.getId());
				map.put("pId",equipParts.getPartsId());
				list.add(map);
			}
			equipPartsService.insertStkPart(list);
		}
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "O_EQUIP_PARTS-编辑")
	@ApiOperation(value="O_EQUIP_PARTS-编辑", notes="O_EQUIP_PARTS-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody EquipPartsVO equipPartsVO) {
		//equipPartsService.updateById(equipParts);
		System.out.println("----111111111111111111111111111111111111111111-------------------------------------");
		System.out.println(equipPartsVO);
		System.out.println("--------->>>>>>>>333333333333333333333333333333333333333333>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		EquipParts equipParts = equipPartsService.getById(equipPartsVO.getPartsId());
		System.out.println(equipParts);
		System.out.println(equipPartsVO.getEquipId());
		equipParts.setEquipId(equipPartsVO.getEquipId());
		equipParts.setPartsName(equipPartsVO.getPartsName());
		equipParts.setPartsNo(equipPartsVO.getPartsNo());
		equipParts.setPartsSort(equipPartsVO.getPartsSort());
		//删除该节点，并新增
		equipPartsService.deleteStkPartByPartId(equipPartsVO.getPartsId());
		List<StkSparePartsVO> stkSparePartsList = equipPartsVO.getStkSparePartsList();
		List<Map<String,Object>> list = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(stkSparePartsList)) {
			for (StkSparePartsVO stkSparePartsVO : stkSparePartsList) {
				Map<String, Object> map = new HashMap<>();
				map.put("prId", stkSparePartsVO.getId());
				map.put("pId",equipParts.getPartsId());
				list.add(map);
			}
			equipPartsService.insertStkPart(list);
		}
		//equipParts.setPartsType(equipPartsVO.getPartsType());
		equipPartsService.updateById(equipParts);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "O_EQUIP_PARTS-通过id删除")
	@ApiOperation(value="O_EQUIP_PARTS-通过id删除", notes="O_EQUIP_PARTS-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		equipPartsService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "O_EQUIP_PARTS-批量删除")
	@ApiOperation(value="O_EQUIP_PARTS-批量删除", notes="O_EQUIP_PARTS-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.equipPartsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "O_EQUIP_PARTS-通过id查询")
	@ApiOperation(value="O_EQUIP_PARTS-通过id查询", notes="O_EQUIP_PARTS-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			@RequestParam("id") String id,@RequestParam("parentId")String parentId) {
		EquipPartsVO equipPartsVO = new EquipPartsVO();
		//查询部件
		EquipParts equipParts = equipPartsService.getById(id);
		System.out.println(equipParts);
		//查询设备
		Map<String, Object> map = equipPartsService.selectEquipById(parentId);
		equipPartsVO.setEquipId(map.get("ID").toString());
		equipPartsVO.setEquipName(map.get("TITLE").toString());
		//查询所属区域（查询o_area）
		Map<String, Object> map2 = equipPartsService.selectAreaById(map.get("PID").toString());
		equipPartsVO.setAreaId(map2.get("ID").toString());
		equipPartsVO.setAreaName(map2.get("TITLE").toString());
		//查询设备类别（查询P_COMM_CODE）
		Map<String, Object> map3 = equipPartsService.selectEquipCategById(map2.get("PID").toString());
		equipPartsVO.setEquipCategId(map3.get("ID").toString());
		equipPartsVO.setEquipCategName(map3.get("TITLE").toString());
		equipPartsVO.setPartsSort(equipParts == null ? "" : equipParts.getPartsSort());
		//equipPartsVO.setPartsType(equipParts == null ? "" : equipParts.getPartsType());
		equipPartsVO.setPartsNo(equipParts == null ? "" : equipParts.getPartsNo());
		equipPartsVO.setPartsId(equipParts == null ? "" : equipParts.getPartsId());
		equipPartsVO.setPartsName(equipParts == null ? "" : equipParts.getPartsName());
		//查询备品备件(查中间表)
		List<Map<String, Object>> selectStkPartList = equipPartsService.selectStkPart(id);
		if (!CollectionUtils.isEmpty(selectStkPartList)){
			List<StkSparePartsVO> list = new ArrayList<>();
			for (Map<String,Object> dataMap : selectStkPartList ) {
				StkSparePartsVO stkSparePartsVO = new StkSparePartsVO();
				//备品备件编号
				Integer prid = Integer.valueOf(dataMap.get("PRID").toString());
				ScrapPro scrapPro = new ScrapPro();
				scrapPro.setProductId(prid);
				List<ScrapPro> records = scrapProService.queryProdList(scrapPro);
				if (CollectionUtils.isEmpty(records)) {
					continue;
				}
				for (ScrapPro scrap : records) {
					stkSparePartsVO.setAttributeCategoryId(scrap == null ? null : scrap.getAttributeCategoryId());
					stkSparePartsVO.setProductName(scrap == null ? "" : scrap.getRpoductName());
					stkSparePartsVO.setSupplier(scrap == null ? null : scrap.getVendorId());
					stkSparePartsVO.setId(scrap == null ? null : scrap.getProductId());
					stkSparePartsVO.setProductNo(scrap == null ? "" : scrap.getProductNo());
					list.add(stkSparePartsVO);
				}
			}
			equipPartsVO.setStkSparePartsList(list);
		}else{
			equipPartsVO.setStkSparePartsList(null);
		}
		Map dataMap = new HashMap();
		dataMap.put("","");
		return Result.ok(equipPartsVO);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param equipParts
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EquipParts equipParts) {
        return super.exportXls(request, equipParts, EquipParts.class, "O_EQUIP_PARTS");
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
        return super.importExcel(request, response, EquipParts.class);
    }

}
