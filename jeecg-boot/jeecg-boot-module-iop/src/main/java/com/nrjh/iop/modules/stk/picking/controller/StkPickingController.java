package com.nrjh.iop.modules.stk.picking.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nrjh.iop.modules.stk.location.service.IStkLocationService;
import com.nrjh.iop.modules.stk.move.service.IStkMoveLineService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.nrjh.iop.modules.stk.picking.entity.StkPicking;
import com.nrjh.iop.modules.stk.picking.vo.StkPickingPage;
import com.nrjh.iop.modules.stk.picking.service.IStkPickingService;
import com.nrjh.iop.modules.stk.move.service.IStkMoveService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 调拨单
 * @Author: jeecg-boot
 * @Date: 2020-05-18
 * @Version: V1.0
 */
@Api(tags = "调拨单")
@RestController
@RequestMapping("/iop/stk/picking")
@Slf4j
public class StkPickingController {
    @Autowired
    private IStkPickingService stkPickingService;
    @Autowired
    private IStkMoveService stkMoveService;

    /**
     * 分页列表查询
     *
     * @param stkPicking
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "调拨单-分页列表查询")
    @ApiOperation(value = "调拨单-分页列表查询", notes = "调拨单-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(StkPicking stkPicking,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<StkPicking> queryWrapper = QueryGenerator.initQueryWrapper(stkPicking, req.getParameterMap());
        Page<StkPicking> page = new Page<StkPicking>(pageNo, pageSize);
        IPage<StkPicking> pageList = stkPickingService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param stkPickingPage
     * @return
     */
    @AutoLog(value = "调拨单-添加")
    @ApiOperation(value = "调拨单-添加", notes = "调拨单-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody StkPickingPage stkPickingPage) {
        StkPicking stkPicking = new StkPicking();
        BeanUtils.copyProperties(stkPickingPage, stkPicking);
        // 调拨单生成时，创建移库单
        stkPickingService.saveMain(stkPicking, stkPickingPage.getStkMoveList());
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param stkPickingPage
     * @return
     */
    @AutoLog(value = "调拨单-编辑")
    @ApiOperation(value = "调拨单-编辑", notes = "调拨单-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody StkPickingPage stkPickingPage) {
        StkPicking stkPicking = new StkPicking();
        BeanUtils.copyProperties(stkPickingPage, stkPicking);
        StkPicking stkPickingEntity = stkPickingService.getById(stkPicking.getId());
        if (stkPickingEntity == null) {
            return Result.error("未找到对应数据");
        }
        stkPickingService.updateMain(stkPicking, stkPickingPage.getStkMoveList());
        return Result.ok("编辑成功!");
    }


    @AutoLog(value = "调拨单-状态变更")
    @ApiOperation(value = "调拨单-状态变更", notes = "调拨单-状态变更")
    @GetMapping(value = "/updateState")
    public Result<?> updateState(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "state", required = true) String state) {
        // 更新 调拨单状态
        StkPicking stkPicking = stkPickingService.getById(id);
        stkPicking.setState(state);
        if ("done".equals(state)){
            stkPicking.setDateDone(new Date());
        }
        stkPickingService.updateById(stkPicking);
        // 更新 移库单状态
        List<StkMove> list = stkMoveService.selectByPickingId(id);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                StkMove stkmove = list.get(i);
                stkmove.setState(state);
                stkmove.setDateDone(stkPicking.getDateDone());
                stkMoveService.updateById(stkmove);
                // 判断state变成完成时，通过移动单生成明细单
                if ("done".equals(state)) {
                    stkMoveService.createMoveLineByMove(stkmove);
                }
            }
        }
        return Result.ok("状态变更成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "调拨单-通过id删除")
    @ApiOperation(value = "调拨单-通过id删除", notes = "调拨单-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        stkPickingService.delMain(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "调拨单-批量删除")
    @ApiOperation(value = "调拨单-批量删除", notes = "调拨单-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.stkPickingService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "调拨单-通过id查询")
    @ApiOperation(value = "调拨单-通过id查询", notes = "调拨单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        StkPicking stkPicking = stkPickingService.getById(id);
        if (stkPicking == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(stkPicking);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "移库单集合-通过id查询")
    @ApiOperation(value = "移库单集合-通过id查询", notes = "移库单-通过id查询")
    @GetMapping(value = "/queryStkMoveByMainId")
    public Result<?> queryStkMoveListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<StkMove> stkMoveList = stkMoveService.selectByPickingId(id);
        return Result.ok(stkMoveList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param stkPicking
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StkPicking stkPicking) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<StkPicking> queryWrapper = QueryGenerator.initQueryWrapper(stkPicking, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<StkPicking> queryList = stkPickingService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<StkPicking> stkPickingList = new ArrayList<StkPicking>();
        if (oConvertUtils.isEmpty(selections)) {
            stkPickingList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            stkPickingList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<StkPickingPage> pageList = new ArrayList<StkPickingPage>();
        for (StkPicking main : stkPickingList) {
            StkPickingPage vo = new StkPickingPage();
            BeanUtils.copyProperties(main, vo);
            List<StkMove> stkMoveList = stkMoveService.selectByPickingId(main.getId().toString());
            vo.setStkMoveList(stkMoveList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "调拨单列表");
        mv.addObject(NormalExcelConstants.CLASS, StkPickingPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("调拨单数据", "导出人:" + sysUser.getRealname(), "调拨单"));
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
                List<StkPickingPage> list = ExcelImportUtil.importExcel(file.getInputStream(), StkPickingPage.class, params);
                for (StkPickingPage page : list) {
                    StkPicking po = new StkPicking();
                    BeanUtils.copyProperties(page, po);
                    stkPickingService.saveMain(po, page.getStkMoveList());
                }
                return Result.ok("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
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
