package com.nrjh.iop.modules.prd.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.prd.category.entity.PrdProductAttrCategRel;

import java.util.List;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/4 14:46
 */
public interface IPrdProductAttrCategRelService extends IService<PrdProductAttrCategRel> {
    public List<PrdProductAttrCategRel> getList(Integer productId);
}
