package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/27 17:15
 */
@Data
@Api("检定合格率VO")
public class CheckPassRateVO implements Serializable {
    @ApiModelProperty("合格率")
    private Double passRate;
    @ApiModelProperty("任务数量")
    private Integer taskCount;
    @ApiModelProperty("x轴值的显示")
    private String xTime;
    @ApiModelProperty("合格量")
    private Integer qualifiedCount;
}
