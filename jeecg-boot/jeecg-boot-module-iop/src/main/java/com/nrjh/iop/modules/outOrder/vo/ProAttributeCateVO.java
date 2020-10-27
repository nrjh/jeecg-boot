package com.nrjh.iop.modules.outOrder.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/4 15:39
 */
@ApiModel("商品属性规格")
@Data
public class ProAttributeCateVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规格名称")
    private String skuName;
    @ApiModelProperty(value = "规格数量")
    private Integer skuNum;
}
