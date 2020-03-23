package com.nrjh.iop.modules.reserve.store.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.nrjh.iop.modules.reserve.store.entity.OReserveStore;
import com.nrjh.iop.modules.reserve.store.service.IOReserveStoreService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: O_RESERVE_STORE
 * @Author: jeecg-boot
 * @Date: 2020-03-06
 * @Version: V1.0
 */
@RestController
@RequestMapping("/iop/reserve/store")
@Slf4j
public class OReserveStoreController extends JeecgController<OReserveStore, IOReserveStoreService> {
    @Autowired
    private IOReserveStoreService oReserveStoreService;

    /**
     * 分页列表查询
     *
     * @param oReserveStore
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(OReserveStore oReserveStore,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<OReserveStore> queryWrapper = QueryGenerator.initQueryWrapper(oReserveStore, req.getParameterMap());
        Page<OReserveStore> page = new Page<OReserveStore>(pageNo, pageSize);
        IPage<OReserveStore> pageList = oReserveStoreService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param oReserveStore
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OReserveStore oReserveStore) {
        oReserveStoreService.save(oReserveStore);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param oReserveStore
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OReserveStore oReserveStore) {
        oReserveStoreService.updateById(oReserveStore);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        oReserveStoreService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.oReserveStoreService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        OReserveStore oReserveStore = oReserveStoreService.getById(id);
        if (oReserveStore == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(oReserveStore);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param oReserveStore
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OReserveStore oReserveStore) {
        return super.exportXls(request, oReserveStore, OReserveStore.class, "库房维护");
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
        return super.importExcel(request, response, OReserveStore.class);
    }

}
