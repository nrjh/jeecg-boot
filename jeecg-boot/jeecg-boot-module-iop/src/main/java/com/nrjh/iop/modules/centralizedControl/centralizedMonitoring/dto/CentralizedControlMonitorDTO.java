package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/25 15:38
 */
@Data
@ApiModel("集中监控概述条件检索查询")
public class CentralizedControlMonitorDTO implements Serializable {
    @ApiModelProperty("设备类型")
    private String equipCateg;
    @ApiModelProperty("设备规格")
    private String equipSpecCode;
    @ApiModelProperty("检定线")
    private String lineNo;
    @ApiModelProperty("统计维度")
    private Integer timeFlag;
    @ApiModelProperty("处理时间起")
    @DateTimeFormat(pattern="YYYY-MM-dd")
    private Date startDealTime;
    @ApiModelProperty("处理时间止")
    @DateTimeFormat(pattern="YYYY-MM-dd")
    private Date endDealTime;
    @ApiModelProperty("图标类型,OperationStatus = 运行情况统计,PrdTaskTrace = 生产任务跟踪图，checkEquipOccupyRate = 检定设备占比 , CheckPassRate检定合格率")
    private String chartType;
}
