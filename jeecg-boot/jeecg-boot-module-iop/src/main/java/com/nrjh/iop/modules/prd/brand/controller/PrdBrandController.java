package com.nrjh.iop.modules.prd.brand.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nrjh.iop.modules.prd.category.entity.PrdCategory;
import com.nrjh.iop.modules.prd.category.service.IPrdCategoryService;
import com.nrjh.iop.modules.prd.product.entity.PrdProduct;
import com.nrjh.iop.modules.prd.product.service.IPrdProductService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.prd.brand.entity.PrdBrand;
import com.nrjh.iop.modules.prd.brand.service.IPrdBrandService;

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
 * @Description: 品牌
 * @Author: jeecg-boot
 * @Date: 2020-04-16
 * @Version: V1.0
 */
@Api(tags = "品牌")
@RestController
@RequestMapping("/iop/prd/brand")
@Slf4j
public class PrdBrandController extends JeecgController<PrdBrand, IPrdBrandService> {
    @Autowired
    private IPrdBrandService prdBrandService;
    @Autowired
    private IPrdCategoryService prdCategoryService;
    @Autowired
    private IPrdProductService prdProductService;

    /**
     * 分页列表查询
     *
     * @param prdBrand
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "品牌-分页列表查询")
    @ApiOperation(value = "品牌-分页列表查询", notes = "品牌-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PrdBrand prdBrand,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<PrdBrand> queryWrapper = QueryGenerator.initQueryWrapper(prdBrand, req.getParameterMap());
        Page<PrdBrand> page = new Page<PrdBrand>(pageNo, pageSize);
        IPage<PrdBrand> pageList = prdBrandService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param prdBrand
     * @return
     */
    @AutoLog(value = "品牌-添加")
    @ApiOperation(value = "品牌-添加", notes = "品牌-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PrdBrand prdBrand) {
        prdBrandService.save(prdBrand);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param prdBrand
     * @return
     */
    @AutoLog(value = "品牌-编辑")
    @ApiOperation(value = "品牌-编辑", notes = "品牌-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody PrdBrand prdBrand) {
        prdBrandService.updateById(prdBrand);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "品牌-通过id删除")
    @ApiOperation(value = "品牌-通过id删除", notes = "品牌-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        List<PrdCategory> prdCategoryList = prdCategoryService.selectCategoryListByBrandId(id);
        QueryWrapper<PrdProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id", id);
        List<PrdProduct> prdProductList = prdProductService.list(wrapper);
        if(prdCategoryList.size() != 0 || prdProductList.size() != 0){
            return Result.error("设备有备品备件关联,不可删除");
        }
        prdBrandService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "品牌-批量删除")
    @ApiOperation(value = "品牌-批量删除", notes = "品牌-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.prdBrandService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "品牌-通过id查询")
    @ApiOperation(value = "品牌-通过id查询", notes = "品牌-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        PrdBrand prdBrand = prdBrandService.getById(id);
        if (prdBrand == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(prdBrand);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "品牌-通过id查询")
    @ApiOperation(value = "品牌-通过id查询相关品类", notes = "品牌-通过id查询相关品类")
    @GetMapping(value = "/queryPrdCategoryListByMainId")
    public Result<?> queryPrdCategoryListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<PrdCategory> prdCategoryList = prdCategoryService.selectCategoryListByBrandId(id);
        return Result.ok(prdCategoryList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param prdBrand
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PrdBrand prdBrand) {
        return super.exportXls(request, prdBrand, PrdBrand.class, "品牌");
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
        return super.importExcel(request, response, PrdBrand.class);
    }

}
