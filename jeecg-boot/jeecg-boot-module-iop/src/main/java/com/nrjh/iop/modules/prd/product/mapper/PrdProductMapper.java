package com.nrjh.iop.modules.prd.product.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nrjh.iop.modules.stk.in.entity.StockInOrder;
import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.prd.product.entity.PrdProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 物品
 * @Author: jeecg-boot
 * @Date: 2020-05-06
 * @Version: V1.0
 */
public interface PrdProductMapper extends BaseMapper<PrdProduct> {

    @Select("SELECT p.* FROM prd_product_attr_value_rel par INNER JOIN prd_product p ON par.PRODUCT_ID = p.ID WHERE par.ATTRIBUTE_ID = #{attributeId}")
    public IPage<PrdProduct> queryByAttributeId(IPage<PrdProduct> pagination, String attributeId);

    @Select("SELECT p.* FROM prd_product_attr_categ_rel par INNER JOIN prd_product p ON par.ATTRIBUTE_CATEGORY_ID = p.ID WHERE par.ATTRIBUTE_CATEGORY_ID = #{attributeCategoryId}")
    public IPage<PrdProduct> queryByAttributeCategoryId(IPage<PrdProduct> pagination, String attributeCategoryId);
}
