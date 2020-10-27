package com.nrjh.iop.modules.prd.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "关联查询对象", description = "物品")
public class RefQuery {

    @ApiModelProperty(value = "品牌Id")
    private String brandId;

    @ApiModelProperty(value = "品类Id")
    private String categoryId;

    @ApiModelProperty(value = "属性Id")
    private String attributeId;

    @ApiModelProperty(value = "规格Id")
    private String attributeCategoryId;

}
