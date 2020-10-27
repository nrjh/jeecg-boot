package com.nrjh.iop.modules.stk.oEquip.controller;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.stk.oEquip.entity.EquipImg;
import com.nrjh.iop.modules.stk.oEquip.entity.OEquip;
import com.nrjh.iop.modules.stk.oEquip.service.IEquipImgService;
import com.nrjh.iop.modules.stk.oEquip.service.IOEquipService;
import com.nrjh.iop.modules.stk.oEquip.vo.OEquipVo;
import com.nrjh.iop.modules.stk.parts.entity.EquipParts;
import com.nrjh.iop.modules.stk.parts.service.IEquipPartsService;
import com.nrjh.iop.modules.stk.spareparts.entity.StkSpareParts;
import com.nrjh.iop.modules.stk.spareparts.service.IStkSparePartsService;
import com.nrjh.iop.modules.util.QRCodeGenerator.QRCodeGenerator;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: o_equip
 * @Author: jeecg-boot
 * @Date:   2020-10-10
 * @Version: V1.0
 */
@Api(tags="o_equip")
@RestController
@RequestMapping("/OEquip/oEquip")
@Slf4j
public class OEquipController extends JeecgController<OEquip, IOEquipService> {
	@Autowired
	private IOEquipService oEquipService;
	@Autowired
	private IEquipImgService iEquipImgService;
	 @Autowired
	 private IEquipPartsService equipPartsService;

	 @Autowired
	 private IStkSparePartsService stkSparePartsService;

	/**
	 * 分页列表查询
	 *
	 * @param oEquip
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "o_equip-分页列表查询")
	@ApiOperation(value="o_equip-分页列表查询", notes="o_equip-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(OEquip oEquip,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<OEquip> queryWrapper = QueryGenerator.initQueryWrapper(oEquip, req.getParameterMap());
		Page<OEquip> page = new Page<OEquip>(pageNo, pageSize);
		IPage<OEquip> pageList = oEquipService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param oEquip
	 * @return
	 */
	@AutoLog(value = "o_equip-添加")
	@ApiOperation(value="o_equip-添加", notes="o_equip-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody OEquip oEquip) {
		Result result=new Result();
		oEquip.setNewFlag("1");
		oEquipService.save(oEquip);
		EquipImg equipImg=new EquipImg();
		equipImg.setEquipId(oEquip.getEquipId());
		equipImg.setEquipNo(oEquip.getEquipNo());
		equipImg.setImgUrl(oEquip.getEquipImgs());
		iEquipImgService.save(equipImg);
		result.setSuccess(true);
		result.setResult(oEquip);
		return result;
	}

	/**
	 *  编辑
	 *
	 * @param oEquip
	 * @return
	 */
	@AutoLog(value = "o_equip-编辑")
	@ApiOperation(value="o_equip-编辑", notes="o_equip-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody OEquip oEquip) {
		Result result=new Result();
		oEquipService.updateById(oEquip);
		EquipImg equipImg=new EquipImg();
		equipImg.setId(oEquip.getEquipImgsId());
		equipImg.setEquipId(oEquip.getEquipId());
		equipImg.setEquipNo(oEquip.getEquipNo());
		equipImg.setImgUrl(oEquip.getEquipImgs());
		iEquipImgService.updateById(equipImg);
		result.setSuccess(true);
		result.setMessage("编辑成功");
		result.setResult(oEquip);
		return result;
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "o_equip-通过id删除")
	@ApiOperation(value="o_equip-通过id删除", notes="o_equip-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		OEquip oEquip=new OEquip();
		oEquip.setEquipId(id);
		oEquip.setNewFlag("0");
		oEquip.setIsDel(BigDecimal.ONE);
		oEquipService.updateById(oEquip);
//		oEquipService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "o_equip-批量删除")
	@ApiOperation(value="o_equip-批量删除", notes="o_equip-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.oEquipService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "o_equip-通过id查询")
	@ApiOperation(value="o_equip-通过id查询", notes="o_equip-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		OEquip oEquip = oEquipService.getById(id);
		if(oEquip==null) {
			return Result.error("未找到对应数据");
		}
		String base64String = QRCodeGenerator.getImage2Base64String(QRCodeGenerator.generateBarcodeWithoutWhite(oEquip.getEquipNo(), Color.BLACK));
		oEquip.setQRStr(base64String);
		if(iEquipImgService.selectBymainId(id)!=null){
			oEquip.setEquipImgs(iEquipImgService.selectBymainId(id).getImgUrl());
			oEquip.setEquipImgsId(iEquipImgService.selectBymainId(id).getId());
		}
		OEquipVo oEquipVo=new OEquipVo();
		BeanUtils.copyProperties(oEquip, oEquipVo);
		QueryWrapper<EquipParts> queryWrapper=new QueryWrapper<EquipParts>();
		queryWrapper.eq("EQUIP_ID",id);
		List<EquipParts> equipPartsList=equipPartsService.list(queryWrapper);
		oEquipVo.setEquipParts(equipPartsList);

		QueryWrapper<StkSpareParts> queryWrapperSpare=new QueryWrapper<StkSpareParts>();

		queryWrapperSpare.eq("JOIN_PARTS",oEquip.getEquipType());
		queryWrapperSpare.eq("FLAG","1");
		List<StkSpareParts> stkSpareParts=stkSparePartsService.list(queryWrapperSpare);
		oEquipVo.setStkSpareParts(stkSpareParts);

		return Result.ok(oEquipVo);
	}

}
