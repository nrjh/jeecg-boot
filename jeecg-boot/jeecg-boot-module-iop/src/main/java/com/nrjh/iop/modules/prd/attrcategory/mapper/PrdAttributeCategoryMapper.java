package com.nrjh.iop.modules.prd.attrcategory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttributeCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 规格
 * @Author: jeecg-boot
 * @Date: 2020-04-17
 * @Version: V1.0
 */
public interface PrdAttributeCategoryMapper extends BaseMapper<PrdAttributeCategory> {

    /**
     * 获取品类相关规格信息
     * @param categoryId
     * @return
     */
    public List<PrdAttributeCategory> selectAttrCategoryListByCategoryId(@Param("categoryId") String categoryId);

    PrdAttributeCategory selectAttributeByProductId(@Param("productId") String productId);


}
