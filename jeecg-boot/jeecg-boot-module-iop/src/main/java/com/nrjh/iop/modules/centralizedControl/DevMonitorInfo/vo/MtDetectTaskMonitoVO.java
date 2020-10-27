package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.vo;

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
 * @since 1.0.0 2020/9/12 16:23
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="检定监控任务列表", description="检定监控任务列表")
public class MtDetectTaskMonitoVO implements Serializable {
    private static final long serialVersionUID = -4450290207477986649L;
    /**检定任务编号*/
    @ApiModelProperty(value = "任务号")
    private String detectTaskNo;
    @ApiModelProperty(value = "批次号")
    private String arriveBatchNo;
    @ApiModelProperty(value = "线体名称")
    private String lineNo;
    @ApiModelProperty(value = "任务类别")
    private String taskType;
    @ApiModelProperty(value = "供应商")
    private String supplyNo;
    @ApiModelProperty(value = "模块类型")
    private String modularType;
    @ApiModelProperty(value = "状态")
    private String taskStatus;
    @ApiModelProperty(value = "启动时间")
    private Date detectStartDate;
    @ApiModelProperty(value = "预计完成时间")
    private Date detectEndDate;
    @ApiModelProperty(value = "运行状态")
    private String workingStatus;
    @ApiModelProperty(value = "任务量")
    private Integer equipQty;
    @ApiModelProperty(value = "已检定量")
    private Integer hadCheckNum;
    @ApiModelProperty(value = "合格量")
    private Integer detectConcP;
    @ApiModelProperty(value = "合格率")
    private String passRate;
    @ApiModelProperty(value = "执行进度")
    private String taskRate;
}
