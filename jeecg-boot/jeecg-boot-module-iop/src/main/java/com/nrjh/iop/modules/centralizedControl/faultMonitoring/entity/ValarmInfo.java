package com.nrjh.iop.modules.centralizedControl.faultMonitoring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 故障监控实体
 * @author: lei-li
 * @create: 2020-09-25 11:33
 */
@Data
@TableName("V_ALARM_INFO")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="V_ALARM_INFO对象", description="故障监控")
public class ValarmInfo implements Serializable {
    /**主键*/
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
    /**报警排序*/
    @Excel(name = "报警排序", width = 15)
    @ApiModelProperty(value = "报警排序")
    private String alarmSort;
    /**设备编号*/
    @ApiModelProperty(value = "设备编号")
    private String equipNo;
    /**报警类型*/
    @Excel(name = "报警类型", width = 15)
    @ApiModelProperty(value = "报警类型")
    private String alarmType;
    /**设备编号*/
    @Excel(name = "设备编号", width = 20)
    @ApiModelProperty(value = "设备编号")
    private Date alarmNo;
    /**报警时间*/
    @Excel(name = "报警时间", width = 15)
    @ApiModelProperty(value = "报警时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date alarmTime;
    /**报警等级*/
    @Excel(name = "报警等级", width = 15)
    @ApiModelProperty(value = "报警等级")
    private String alarmLevel;
    /**报警原因*/
    @Excel(name = "报警原因", width = 15)
    @ApiModelProperty(value = "报警原因")
    private String anlReason;

    private String RealReason;

    /**意见*/
    @Excel(name = "意见", width = 15)
    @ApiModelProperty(value = "意见")
    private String anlSugg;

    private String realSugg;

    private String execMode;

    private String execStatus;

    private String execUserId;

    private Date rcvTime;

    private Date execTime;

    private String execDesc;

    private String execRslt;

    private String runBatchNo;

    private String traBatchNo;

    private String faultCode;

    private String isHandled;

    private String ararmSource;

    private String detectTaskNo;

    private String lineNo;

    private String oldId;

    private String equipName;

}
