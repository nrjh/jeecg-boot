package com.nrjh.iop.modules.stk.parts.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.DictIop;

import java.io.Serializable;

/**
 * TODO
 *
 * @author Kou Shenhai 2413176044@leimingtech.com
 * @version 1.0
 * @date 2020/10/9 0009 下午 7:10
 */
@ApiModel(value="部件视图", description="部件视图")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class StkSparePartsVO implements Serializable{

    @ApiModelProperty(value = "编号")
    private Integer id;

    @ApiModelProperty(value = "物料编号")
    private String productNo;

    @ApiModelProperty(value = "物料名称")
    private String productName;

    @ApiModelProperty(value = "供应商")
    @DictIop(dicCode = "vendorCode")
    private Integer supplier;

    @ApiModelProperty(value = "规格")
    @DictIop(dictTable = "prd_attribute_category", dicText = "name", dicCode = "id",dataSource = "iop")
    private Integer attributeCategoryId;

}
