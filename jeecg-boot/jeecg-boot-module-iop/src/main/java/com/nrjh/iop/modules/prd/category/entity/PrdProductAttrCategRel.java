package com.nrjh.iop.modules.prd.category.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/4 14:43
 */
@Data
@TableName("prd_product_attr_categ_rel")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="prd_product_attr_categ_rel", description="商品规格属性")
public class PrdProductAttrCategRel implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("商品id")
    Integer productId;
    @ApiModelProperty("物品规格id")
    Integer attributeCategoryId;

}
