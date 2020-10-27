package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/28 11:08
 */
@Data
@Api("名称和数量")
public class CheckEquipOccupyRateVO implements Serializable {
    private static final long serialVersionUID = 7084780151069869914L;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("数量")
    private Integer value;
}
