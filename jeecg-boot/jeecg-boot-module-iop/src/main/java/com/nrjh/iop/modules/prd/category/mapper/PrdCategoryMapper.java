package com.nrjh.iop.modules.prd.category.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.prd.category.entity.PrdCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 品类
 * @Author: jeecg-boot
 * @Date: 2020-04-17
 * @Version: V1.0
 */
public interface PrdCategoryMapper extends BaseMapper<PrdCategory> {

    /**
     * 品牌相关品类 （同品牌下品类信息）
     * @param brandId
     * @return
     */
    public List<PrdCategory> selectCategoryListByBrandId(@Param("brandId") String brandId);

    /**
     * 品类相关品类 （同父级品类）
     * @param categoryId
     * @return
     */
    public List<PrdCategory> selectCategoryListByCategoryId(@Param("categoryId") String categoryId);
}
