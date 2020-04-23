package com.nrjh.iop.modules.prd.category.service;

import com.nrjh.iop.modules.prd.category.entity.PrdCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 品类
 * @Author: jeecg-boot
 * @Date: 2020-04-17
 * @Version: V1.0
 */
public interface IPrdCategoryService extends IService<PrdCategory> {

    public List<PrdCategory> selectCategoryListByBrandId(String brandId);

    public List<PrdCategory> selectCategoryListByCategoryId( String categoryId);
}
