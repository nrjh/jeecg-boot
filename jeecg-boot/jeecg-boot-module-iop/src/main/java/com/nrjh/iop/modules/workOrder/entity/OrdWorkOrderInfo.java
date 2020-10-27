package com.nrjh.iop.modules.workOrder.entity;

import java.beans.Transient;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.DictIop;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 故障运维工单表
 * @Author: lei-li
 * @Date:   2020-09-01
 * @Version: V1.0
 */
@Data
@TableName("ORD_WORK_ORDER_INFO")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ord_work_order_info对象", description="故障运维工单表")
public class OrdWorkOrderInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**工单创建标识*/
	@Excel(name = "工单创建标识", width = 15)
    @ApiModelProperty(value = "工单创建标识")
    @DictIop(dicCode = "ALARM_SOURCE")
    private String orderCreLabe;
	/**工单类型*/
	@Excel(name = "工单类型", width = 15)
    @ApiModelProperty(value = "工单类型")
    @DictIop(dicCode = "ORA_ORD_WORK_ORDER_INFO_ORDER_TYPE")
    private String orderType;
	/**报警编号*/
	@Excel(name = "报警编号", width = 15)
    @ApiModelProperty(value = "报警编号")
    private String faultId;
	/**检定任务编号*/
	@Excel(name = "检定任务编号", width = 15)
    @ApiModelProperty(value = "检定任务编号")
    private String detectTaskNo;
	/**线体编号*/
	@Excel(name = "线体编号", width = 15)
    @DictIop(dictTable = "o_area", dicText = "area_name", dicCode = "area_id",dataSource = "ora")
    @ApiModelProperty(value = "线体编号")
    private String lineNo;
	/**原系统标识*/
	@Excel(name = "原系统标识", width = 15)
    @ApiModelProperty(value = "原系统标识")
    private String oldId;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String equipNo;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private String equipName;
	/**报警时间*/
	@Excel(name = "报警时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "报警时间")
    private Date alarmTime;
	/**报警级别*/
	@Excel(name = "报警级别", width = 15)
    @ApiModelProperty(value = "报警级别")
    @DictIop(dicCode = "ORA_ORD_WORK_ORDER_INFO_ALARM_LEVEL")
    private String alarmLevel;
	/**报警来源*/
	@Excel(name = "报警来源", width = 15)
    @ApiModelProperty(value = "报警来源")
    @DictIop(dicCode = "source")
    private String alarmSource;
	/**报警原因*/
	@Excel(name = "报警原因", width = 15)
    @ApiModelProperty(value = "报警原因")
    private String alarmReason;
	/**报警描述*/
	@Excel(name = "报警描述", width = 15)
    @ApiModelProperty(value = "报警描述")
    private String alarmDesc;
	/**处理建议*/
	@Excel(name = "处理建议", width = 15)
    @ApiModelProperty(value = "处理建议")
    private String alarmSugg;
	/**报警状态*/
	@Excel(name = "报警状态", width = 15)
    @ApiModelProperty(value = "报警状态")
    private String alarmStatus;
	/**超期完成时间*/
	@Excel(name = "超期完成时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "超期完成时间")
    private Date overdurTime;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**工单接单时间*/
	@Excel(name = "工单接单时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "工单接单时间")
    private Date acceptTime;
	/**工单派工时间*/
	@Excel(name = "工单派工时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "工单派工时间")
    private Date dispatchTime;
	/**工单催办时间*/
	@Excel(name = "工单催办时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "工单催办时间")
    private Date pressTime;
	/**催办标识*/
	@Excel(name = "催办标识", width = 15)
    @ApiModelProperty(value = "催办标识")
    private String pressLabe;
	/**工单锁定时间*/
	@Excel(name = "工单锁定时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "工单锁定时间")
    private Date lockTime;
	/**工单处理时间*/
	@Excel(name = "工单处理时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "工单处理时间")
    private Date processTime;
	/**异常恢复时间*/
	@Excel(name = "异常恢复时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "异常恢复时间")
    private Date resumeTime;
	/**工单归档时间*/
	@Excel(name = "工单归档时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "工单归档时间")
    private Date recoveryTime;
	/**异常处理方式*/
	@Excel(name = "异常处理方式", width = 15)
    @ApiModelProperty(value = "异常处理方式")
    private String processType;
	/**工单状态*/
	@Excel(name = "工单状态", width = 15)
    @ApiModelProperty(value = "工单状态")
    @DictIop(dicCode = "ORA_ORD_WORK_ORDER_INFO_PROCESS_STATUS")
    private String processStatus;
	/**创建人ID*/
	@Excel(name = "创建人ID", width = 15)
    @ApiModelProperty(value = "创建人ID")
    private String createId;
	/**创建人姓名*/
	@Excel(name = "创建人姓名", width = 15)
    @ApiModelProperty(value = "创建人姓名")
    private String createName;
	/**工单派发人*/
	@Excel(name = "工单派发人", width = 15)
    @ApiModelProperty(value = "工单派发人")
    @DictIop(dictTable = "sys_user", dicText = "realname", dicCode = "id",dataSource = "master")
    private String dispatchUser;
	/**工单现场处理人*/
	@Excel(name = "工单现场处理人", width = 15)
    @ApiModelProperty(value = "工单现场处理人")
    @DictIop(dictTable = "sys_user", dicText = "realname", dicCode = "id",dataSource = "master")
    private String processUser;
	/**远程处理状态*/
	@Excel(name = "远程处理状态", width = 15)
    @ApiModelProperty(value = "远程处理状态")
    private String remoteStatus;
	/**远程检测是否成功*/
	@Excel(name = "远程检测是否成功", width = 15)
    @ApiModelProperty(value = "远程检测是否成功")
    private String isRemoteSucc;
	/**是否更换设备*/
	@Excel(name = "是否更换设备", width = 15)
    @ApiModelProperty(value = "是否更换设备")
    private String meterReplace;
	/**领料单号*/
	@Excel(name = "领料单号", width = 15)
    @ApiModelProperty(value = "领料单号")
    private String orderOutId;
	/**掌机是否下载*/
	@Excel(name = "掌机是否下载", width = 15)
    @ApiModelProperty(value = "掌机是否下载")
    private String isLoad;
	/**知识库设备类型*/
	@Excel(name = "知识库设备类型", width = 15)
    @ApiModelProperty(value = "知识库设备类型")
    private String knEquipType;
	/**是否挂单*/
	@Excel(name = "是否挂单", width = 15)
    @ApiModelProperty(value = "是否挂单")
    @DictIop(dicCode = "FA_SP_STAUTS")
    private String isSuspend;
	/**反馈人*/
	@Excel(name = "反馈人", width = 15)
    @ApiModelProperty(value = "反馈人")
    private String fbUser;
	/**工单派发班组*/
	@Excel(name = "工单派发班组", width = 15)
    @ApiModelProperty(value = "工单派发班组")
    private String dispatchTeam;
	/**工单派发对象*/
	@Excel(name = "工单派发对象", width = 15)
    @ApiModelProperty(value = "工单派发对象")
    private String dispatchToUser;
	/**恢复类型*/
	@Excel(name = "恢复类型", width = 15)
    @ApiModelProperty(value = "恢复类型")
    private String resumeType;
	/**派发超期时间*/
	@Excel(name = "派发超期时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "派发超期时间")
    private Date dpOverdurTime;
	/**反馈超期时间*/
	@Excel(name = "反馈超期时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "反馈超期时间")
    private Date fbOverdurTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改人名称*/
	@Excel(name = "修改人名称", width = 15)
    @ApiModelProperty(value = "修改人名称")
    private String updateName;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    /**删除标识*/
    @Excel(name = "删除标识", width = 15)
    @ApiModelProperty(value = "删除标识")
    @TableLogic
    private Integer isDel;


    @TableField(exist = false)
    @DictIop(dictTable = "sys_user", dicText = "realname", dicCode = "id",dataSource = "master")
    private String ordPressBy;  //督办人

    @TableField(exist = false)
    @ApiModelProperty(value = "工单派发人工单号")
    private String dispatchID;

    @TableField(exist =  false)
    @ApiModelProperty("报警次数")
    private String alarmAmount;

    @TableField(exist = false)
    @ApiModelProperty("督办人")
    private String supervisor;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField(exist = false)
    @ApiModelProperty("查询条件开始时间")
    private Date startTime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField(exist = false)
    @ApiModelProperty("查询条件结束时间")
    private Date endTime;

    @TableField(exist = false)
    @ApiModelProperty("检定线名称")
    private String lineName;

    @TableField(exist = false)
    @ApiModelProperty("督办人")
    private String redepUser;
}
