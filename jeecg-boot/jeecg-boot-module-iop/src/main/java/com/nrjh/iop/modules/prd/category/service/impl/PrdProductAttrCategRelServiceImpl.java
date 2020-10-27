package com.nrjh.iop.modules.prd.category.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.prd.category.entity.PrdProductAttrCategRel;
import com.nrjh.iop.modules.prd.category.mapper.PrdProductAttrCategRelMapper;
import com.nrjh.iop.modules.prd.category.service.IPrdProductAttrCategRelService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/4 14:56
 */
@Service
@DS("iop")
@Slf4j
public class PrdProductAttrCategRelServiceImpl extends ServiceImpl<PrdProductAttrCategRelMapper, PrdProductAttrCategRel> implements IPrdProductAttrCategRelService {

    @Autowired
    private PrdProductAttrCategRelMapper prdProductAttrCategRelMapper;

    @Override
    public List<PrdProductAttrCategRel> getList(Integer productId) {
        QueryWrapper<PrdProductAttrCategRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PRODUCT_ID", productId);
        List<PrdProductAttrCategRel> prdProductAttrCategRels = prdProductAttrCategRelMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(prdProductAttrCategRels)) {
            log.error("查询列表为null,查询的商品id:{}",productId);
            throw new JeecgBootException("查询列表为null");
        }
        return prdProductAttrCategRels;
    }

}
