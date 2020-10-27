package com.nrjh.iop.modules.sys.dict.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.sys.dict.entry.IopSysDictItem;
import com.nrjh.iop.modules.sys.dict.service.IIopSysDictItemService;
import com.nrjh.iop.modules.sys.dict.service.IOraSysDictItemService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@RestController
@RequestMapping("/ora/sys/dictItem")
@Slf4j
public class OraSysDictItemController {

    @Autowired
    private IOraSysDictItemService sysDictItemService;

    /**
     * @param sysDictItem
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     * @功能：查询字典数据
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<IopSysDictItem>> queryPageList(IopSysDictItem sysDictItem, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<IopSysDictItem>> result = new Result<IPage<IopSysDictItem>>();
        QueryWrapper<IopSysDictItem> queryWrapper = QueryGenerator.initQueryWrapper(sysDictItem, req.getParameterMap());
        queryWrapper.orderByAsc("sort_order");
        Page<IopSysDictItem> page = new Page<IopSysDictItem>(pageNo, pageSize);
        IPage<IopSysDictItem> pageList = sysDictItemService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * @param sysDictItem
     * @return
     * @功能：新增
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public Result<IopSysDictItem> add(@RequestBody IopSysDictItem sysDictItem) {
        Result<IopSysDictItem> result = new Result<IopSysDictItem>();
        try {
            sysDictItem.setCreateTime(new Date());
            sysDictItemService.save(sysDictItem);
            result.success("保存成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * @param sysDictItem
     * @return
     * @功能：编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public Result<IopSysDictItem> edit(@RequestBody IopSysDictItem sysDictItem) {
        Result<IopSysDictItem> result = new Result<IopSysDictItem>();
        IopSysDictItem sysdict = sysDictItemService.getById(sysDictItem.getId());
        if (sysdict == null) {
            result.error500("未找到对应实体");
        } else {
            sysDictItem.setUpdateTime(new Date());
            boolean ok = sysDictItemService.updateById(sysDictItem);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("编辑成功!");
            }
        }
        return result;
    }

    /**
     * @param id
     * @return
     * @功能：删除字典数据
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public Result<IopSysDictItem> delete(@RequestParam(name = "id", required = true) String id) {
        Result<IopSysDictItem> result = new Result<IopSysDictItem>();
        IopSysDictItem joinSystem = sysDictItemService.getById(id);
        if (joinSystem == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysDictItemService.removeById(id);
            if (ok) {
                result.success("删除成功!");
            }
        }
        return result;
    }

    /**
     * @param ids
     * @return
     * @功能：批量删除字典数据
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public Result<IopSysDictItem> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<IopSysDictItem> result = new Result<IopSysDictItem>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysDictItemService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

}
