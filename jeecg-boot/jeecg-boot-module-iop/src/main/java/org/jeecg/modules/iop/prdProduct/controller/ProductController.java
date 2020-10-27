package org.jeecg.modules.iop.prdProduct.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.iop.prdProduct.entity.Product;
import org.jeecg.modules.iop.prdProduct.entity.ProductAttrValueRel;
import org.jeecg.modules.iop.prdProduct.service.IProductAttrValueRelService;
import org.jeecg.modules.iop.prdProduct.service.IProductService;
import org.jeecg.modules.iop.prdProduct.vo.ProductPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

 /**
 * @Description: 备品备件
 * @Author: jeecg-boot
 * @Date:   2020-08-06
 * @Version: V1.0
 */
@Api(tags="备品备件")
@RestController
@RequestMapping("/product/product")
@Slf4j
public class ProductController {
	@Autowired
	private IProductService productService;
	@Autowired
	private IProductAttrValueRelService productAttrValueRelService;
	
	/**
	 * 分页列表查询
	 *
	 * @param product
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "备品备件-分页列表查询")
	@ApiOperation(value="备品备件-分页列表查询", notes="备品备件-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Product product,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Product> queryWrapper = QueryGenerator.initQueryWrapper(product, req.getParameterMap());
		Page<Product> page = new Page<Product>(pageNo, pageSize);
		IPage<Product> pageList = productService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param productPage
	 * @return
	 */
	@AutoLog(value = "备品备件-添加")
	@ApiOperation(value="备品备件-添加", notes="备品备件-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ProductPage productPage) {
		Product product = new Product();
		BeanUtils.copyProperties(productPage, product);
		productService.saveMain(product, productPage.getProductAttrValueRelList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param productPage
	 * @return
	 */
	@AutoLog(value = "备品备件-编辑")
	@ApiOperation(value="备品备件-编辑", notes="备品备件-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ProductPage productPage) {
		Product product = new Product();
		BeanUtils.copyProperties(productPage, product);
		Product productEntity = productService.getById(product.getId());
		if(productEntity==null) {
			return Result.error("未找到对应数据");
		}
		productService.updateMain(product, productPage.getProductAttrValueRelList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "备品备件-通过id删除")
	@ApiOperation(value="备品备件-通过id删除", notes="备品备件-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		productService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "备品备件-批量删除")
	@ApiOperation(value="备品备件-批量删除", notes="备品备件-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.productService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "备品备件-通过id查询")
	@ApiOperation(value="备品备件-通过id查询", notes="备品备件-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Product product = productService.getById(id);
		if(product==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(product);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "备品备件属性值关系集合-通过id查询")
	@ApiOperation(value="备品备件属性值关系集合-通过id查询", notes="备品备件属性值关系-通过id查询")
	@GetMapping(value = "/queryProductAttrValueRelByMainId")
	public Result<?> queryProductAttrValueRelListByMainId(@RequestParam(name="id",required=true) String id) {
		List<ProductAttrValueRel> productAttrValueRelList = productAttrValueRelService.selectByMainId(id);
		return Result.ok(productAttrValueRelList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param product
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Product product) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Product> queryWrapper = QueryGenerator.initQueryWrapper(product, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<Product> queryList = productService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<Product> productList = new ArrayList<Product>();
      if(oConvertUtils.isEmpty(selections)) {
          productList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          productList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<ProductPage> pageList = new ArrayList<ProductPage>();
      for (Product main : productList) {
          ProductPage vo = new ProductPage();
          BeanUtils.copyProperties(main, vo);
          List<ProductAttrValueRel> productAttrValueRelList = productAttrValueRelService.selectByMainId(main.getId()+"");
          vo.setProductAttrValueRelList(productAttrValueRelList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "备品备件列表");
      mv.addObject(NormalExcelConstants.CLASS, ProductPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("备品备件数据", "导出人:"+sysUser.getRealname(), "备品备件"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<ProductPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ProductPage.class, params);
              for (ProductPage page : list) {
                  Product po = new Product();
                  BeanUtils.copyProperties(page, po);
                  productService.saveMain(po, page.getProductAttrValueRelList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
    }

}
