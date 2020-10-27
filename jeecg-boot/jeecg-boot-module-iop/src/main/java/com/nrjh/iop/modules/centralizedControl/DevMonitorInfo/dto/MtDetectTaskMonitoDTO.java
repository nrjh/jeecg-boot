package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/12 17:39
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="检定监控任务列表DTO", description="检定监控任务列表")
public class MtDetectTaskMonitoDTO implements Serializable {
    private static final long serialVersionUID = -5437102897606448638L;
    @ApiModelProperty(value = "任务号")
    private String detectTaskNo;

    @ApiModelProperty(value = "线体名称")
    private String lineNo;

    @ApiModelProperty(value = "供应商")
    private String supplyNo;

    @ApiModelProperty(value = "状态")
    private String taskStatus;

}
