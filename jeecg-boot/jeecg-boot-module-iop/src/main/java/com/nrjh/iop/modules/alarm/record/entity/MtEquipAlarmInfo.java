package com.nrjh.iop.modules.alarm.record.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * @Description: MT_EQUIP_ALARM_INFO
 * @Author: jeecg-boot
 * @Date: 2020-08-28
 * @Version: V1.0
 */
@Data
@TableName("midhd.FAULT_EQUIP_ALARM_INFO")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "FAULT_EQUIP_ALARM_INFO对象", description = "FAULT_EQUIP_ALARM_INFO")
public class MtEquipAlarmInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * IOP在中间库创建对应序列
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "IOP在中间库创建对应序列")
    private Integer id;
    /**
     * 检定任务编号
     */
    @Excel(name = "检定任务编号", width = 15)
    @ApiModelProperty(value = "检定任务编号")
    private String detectTaskNo;
    /**
     * 线体编号
     */
    @Excel(name = "线体编号", width = 15)
    @ApiModelProperty(value = "线体编号")
    private String lineNo;
    /**
     * 原系统标识
     */
    @Excel(name = "原系统标识", width = 15)
    @ApiModelProperty(value = "原系统标识")
    private String oldId;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String equipNo;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private String equipName;
    /**
     * 报警时间
     */
    @Excel(name = "报警时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "报警时间")
    private Date alarmTime;
    /**
     * 报警级别
     */
    @Excel(name = "报警级别", width = 15)
    @ApiModelProperty(value = "报警级别")
    private String alarmLevel;
    /**
     * 报警来源
     */
    @Excel(name = "报警来源", width = 15)
    @ApiModelProperty(value = "报警来源")
    private String alarmSource;
    /**
     * 报警原因
     */
    @Excel(name = "报警原因", width = 15)
    @ApiModelProperty(value = "报警原因")
    private String alarmReason;
    /**
     * 报警描述
     */
    @Excel(name = "报警描述", width = 15)
    @ApiModelProperty(value = "报警描述")
    private String alarmDesc;
    /**
     * 处理建议
     */
    @Excel(name = "处理建议", width = 15)
    @ApiModelProperty(value = "处理建议")
    private String alarmSugg;
    /**
     * 报警状态
     */
    @Excel(name = "报警状态", width = 15)
    @ApiModelProperty(value = "报警状态")
    private String alarmStatus;
    /**
     * 解除时间
     */
    @Excel(name = "解除时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "解除时间")
    private Date endTime;
    /**
     * 处理方式
     */
    @Excel(name = "处理方式", width = 15)
    @ApiModelProperty(value = "处理方式")
    private String execMode;
    /**
     * 处理过程描述
     */
    @Excel(name = "处理过程描述", width = 15)
    @ApiModelProperty(value = "处理过程描述")
    private String execDesc;
    /**
     * 备注信息
     */
    @Excel(name = "备注信息", width = 15)
    @ApiModelProperty(value = "备注信息")
    private String remark;
    /**
     * 操作标志
     */
    @Excel(name = "操作标志", width = 15)
    @ApiModelProperty(value = "操作标志")
    private String opFlag;
    /**
     * 数据写入时间
     */
    @Excel(name = "数据写入时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "数据写入时间")
    private Date writeTime;

    @Excel(name = "处理人", width = 15)
    @ApiModelProperty(value = "处理人")
    private String createBy;
    @Excel(name = "处理人班组", width = 15)
    @ApiModelProperty(value = "处理人班组")
    private String createByGroup;

    @Excel(name = "数据来源", width = 15)
    @ApiModelProperty(value = "数据来源")
    private String dataFrom;


    @Excel(name = "故障类型", width = 15)
    @ApiModelProperty(value = "故障类型")
    private String faultLabe;
    /**网省编号*/
    @TableField(exist = false)
    private String provinceCode;

    @Excel(name = "报警编号", width = 15)
    @ApiModelProperty(value = "报警编号")
    private String alarmNo;

    @ApiModelProperty(value = "系统名称")
    private String systemName;

    @TableLogic
    private Integer isDel;
}
