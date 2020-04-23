package com.nrjh.iop.modules.prd.category.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.prd.category.entity.PrdCategory;
import com.nrjh.iop.modules.prd.category.mapper.PrdCategoryMapper;
import com.nrjh.iop.modules.prd.category.service.IPrdCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 品类
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
@Service
@DS("iop")
public class PrdCategoryServiceImpl extends ServiceImpl<PrdCategoryMapper, PrdCategory> implements IPrdCategoryService {

    @Autowired
    private PrdCategoryMapper prdCategoryMapper;
    @Override
    public List<PrdCategory> selectCategoryListByBrandId(String brandId) {
        return prdCategoryMapper.selectCategoryListByBrandId(brandId);
    }

    @Override
    public List<PrdCategory> selectCategoryListByCategoryId(String categoryId) {
        return prdCategoryMapper.selectCategoryListByCategoryId(categoryId);
    }


}
