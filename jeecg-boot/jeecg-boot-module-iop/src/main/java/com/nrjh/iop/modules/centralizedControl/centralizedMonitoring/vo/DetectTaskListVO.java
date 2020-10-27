package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/29 14:19
 */
@ApiModel("检测列表")
@Data
public class DetectTaskListVO implements Serializable {
    private static final long serialVersionUID = 4508672073313949823L;
    @ApiModelProperty("日期")
    private String checkDateTime;
    @ApiModelProperty("任务类型 , 0-常规检定、1-非常规检定")
    private String isNormalCheck;
    @ApiModelProperty("检定线")
    private String lineNo;
    @ApiModelProperty("任务单号")
    private String taskNo;
    @ApiModelProperty("批次")
    private String arriveBatchNo;
    @ApiModelProperty("供应商")
    private String supplyNo;
    @ApiModelProperty("任务类型")
    private String detectType;
    @ApiModelProperty("费率")
    private String useRate;
    @ApiModelProperty("设备描述")
    private String equipDescribe;
    @ApiModelProperty("检定时间")
    private Date taskBgnDate;
    @ApiModelProperty("任务状态")
    private String taskStatus;
    @ApiModelProperty("任务总量")
    private Integer detectQty;
    @ApiModelProperty("当天完成量")
    private Integer detectConcP;
    @ApiModelProperty("当天合格率")
    private Double passRate;
    @ApiModelProperty("累计完成量")
    private Integer cumulativeFinishCount;
    @ApiModelProperty("累计合格率")
    private Double cumulativePassRate;
    @ApiModelProperty("审核状态 - 是否已审核")
    private String auditStatus;
    @ApiModelProperty("方案核对人")
    private String makerName;
    @ApiModelProperty("备注")
    private String remarks;
}

