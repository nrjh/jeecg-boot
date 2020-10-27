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
@ApiModel(value="检测走势视图", description="检测走势视图")
public class DetectTrendVO implements Serializable {

    @ApiModelProperty(value = "区域编号")
    private String areaId;
    @ApiModelProperty(value = "区域名称")
    private String areaName;
    @ApiModelProperty(value = "每小时")
    private Integer hours;
    @ApiModelProperty(value = "数量")
    private Integer num;

}