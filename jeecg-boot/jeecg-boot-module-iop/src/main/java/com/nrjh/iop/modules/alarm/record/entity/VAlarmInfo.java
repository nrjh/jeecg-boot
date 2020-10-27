package com.nrjh.iop.modules.alarm.record.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.DictIop;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 报警信息视图
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Data
@TableName("v_alarm_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="v_alarm_info对象", description="报警信息视图")
public class VAlarmInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**alarmSort*/
	@Excel(name = "alarmSort", width = 15)
    @ApiModelProperty(value = "alarmSort")
    private String alarmSort;
	/**equipNo*/
	@Excel(name = "equipNo", width = 15)
    @ApiModelProperty(value = "equipNo")
    private String equipNo;
	/**alarmType*/
	@Excel(name = "alarmType", width = 15)
    @ApiModelProperty(value = "alarmType")
    private String alarmType;
	/**alarmNo*/
	@Excel(name = "alarmNo", width = 15)
    @ApiModelProperty(value = "alarmNo")
    private String alarmNo;
	/**alarmTime*/
	@Excel(name = "alarmTime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "alarmTime")
    private Date alarmTime;
	/**alarmLevel*/
	@Excel(name = "alarmLevel", width = 15)
    @ApiModelProperty(value = "alarmLevel")
    private String alarmLevel;
	/**anlReason*/
	@Excel(name = "anlReason", width = 15)
    @ApiModelProperty(value = "anlReason")
    private String anlReason;
	/**realReason*/
	@Excel(name = "realReason", width = 15)
    @ApiModelProperty(value = "realReason")
    private String realReason;
	/**anlSugg*/
	@Excel(name = "anlSugg", width = 15)
    @ApiModelProperty(value = "anlSugg")
    private String anlSugg;
	/**realSugg*/
	@Excel(name = "realSugg", width = 15)
    @ApiModelProperty(value = "realSugg")
    private String realSugg;
	/**execMode*/
	@Excel(name = "execMode", width = 15)
    @ApiModelProperty(value = "execMode")
    private String execMode;
	/**execStatus*/
	@Excel(name = "execStatus", width = 15)
    @ApiModelProperty(value = "execStatus")
    private String execStatus;
	/**execUserId*/
	@Excel(name = "execUserId", width = 15)
    @ApiModelProperty(value = "execUserId")
    private String execUserId;
	/**rcvTime*/
	@Excel(name = "rcvTime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "rcvTime")
    private Date rcvTime;
	/**execTime*/
	@Excel(name = "execTime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "execTime")
    private Date execTime;
	/**exceDesc*/
	@Excel(name = "exceDesc", width = 15)
    @ApiModelProperty(value = "exceDesc")
    private String exceDesc;
	/**execRslt*/
	@Excel(name = "execRslt", width = 15)
    @ApiModelProperty(value = "execRslt")
    private String execRslt;
	/**runBatchNo*/
	@Excel(name = "runBatchNo", width = 15)
    @ApiModelProperty(value = "runBatchNo")
    private Integer runBatchNo;
	/**traBatchNo*/
	@Excel(name = "traBatchNo", width = 15)
    @ApiModelProperty(value = "traBatchNo")
    private Integer traBatchNo;
	/**faultCode*/
	@Excel(name = "faultCode", width = 15)
    @ApiModelProperty(value = "faultCode")
    private String faultCode;
	/**isHandled*/
	@Excel(name = "isHandled", width = 15)
    @ApiModelProperty(value = "isHandled")
    private Integer isHandled;
	/**alarmNum*/
	@Excel(name = "alarmNum", width = 15)
    @ApiModelProperty(value = "alarmNum")
    private Integer alarmNum;
	/**faultDesc*/
	@Excel(name = "faultDesc", width = 15)
    @ApiModelProperty(value = "faultDesc")
    private String faultDesc;
	/**tableName*/
	@Excel(name = "tableName", width = 15)
    @ApiModelProperty(value = "tableName")
    private String tableName;
	/**alarmSource*/
	@Excel(name = "alarmSource", width = 15)
    @ApiModelProperty(value = "alarmSource")
    private String alarmSource;
	/**detectTaskNo*/
	@Excel(name = "detectTaskNo", width = 15)
    @ApiModelProperty(value = "detectTaskNo")
    private String detectTaskNo;
	/**lineNo*/
	@Excel(name = "lineNo", width = 15)
    @DictIop(dictTable = "o_area", dicText = "area_name", dicCode = "area_id",dataSource = "ora")
    @ApiModelProperty(value = "lineNo")
    private String lineNo;
	/**oldId*/
	@Excel(name = "oldId", width = 15)
    @ApiModelProperty(value = "oldId")
    private String oldId;
	/**equipName*/
	@Excel(name = "equipName", width = 15)
    @ApiModelProperty(value = "equipName")
    private String equipName;

    @TableField(exist = false)
    private String alarmAmount;

    @TableField(exist = false)
    private String isOrder;

    @TableField(exist = false)
    private OrdWorkOrderInfo ordWorkOrderInfo;

    @TableField(exist = false)
    private String faultType;

    @TableField(exist = false)
    private String lineName;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField(exist = false)
    private Date alarmTimeStart;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField(exist = false)
    private Date alarmTimeEnd;


}
