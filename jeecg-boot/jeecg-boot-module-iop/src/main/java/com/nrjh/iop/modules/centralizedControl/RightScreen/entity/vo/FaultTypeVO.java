package com.nrjh.iop.modules.centralizedControl.RightScreen.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * TODO
 *
 * @author Kou Shenhai 2413176044@leimingtech.com
 * @version 1.0
 * @date 2020/9/22 0022 上午 10:51
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="故障类型视图", description="故障类型视图")
public class FaultTypeVO  implements Serializable {

    @ApiModelProperty(value = "区域名称")
    private String areaName;
    @ApiModelProperty(value = "区域编号")
    private String areaId;
    @ApiModelProperty(value = "故障类型")
    private String labe;
    @ApiModelProperty(value = "次数")
    private Integer num;

}