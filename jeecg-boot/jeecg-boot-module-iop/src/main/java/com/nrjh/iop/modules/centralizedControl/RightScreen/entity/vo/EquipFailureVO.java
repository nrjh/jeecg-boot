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
 * @date 2020/9/18 0018 下午 2:17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="故障设备报警视图", description="故障设备报警视图")
public class EquipFailureVO implements Serializable {

    @ApiModelProperty(value = "检定线编号")
    private String areaId;
    @ApiModelProperty(value = "设备名称")
    private String equipName;
    @ApiModelProperty(value = "故障频率")
    private Integer frequency;
    @ApiModelProperty(value = "处理总时长")
    private Integer handleTimeTotal;
    @ApiModelProperty(value = "检定区域名称")
    private String areaName;

}
