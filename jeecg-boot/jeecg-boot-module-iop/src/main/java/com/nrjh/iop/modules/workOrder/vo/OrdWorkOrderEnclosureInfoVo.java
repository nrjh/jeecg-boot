package com.nrjh.iop.modules.workOrder.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-09-02 20:51
 */
@Data
public class OrdWorkOrderEnclosureInfoVo implements Serializable {
    /**主键*/
    private String id;
    /**工单创建标识*/
    private String orderCreLabe;
    /**工单类型*/
    private String orderType;
    /**报警编号*/
    private String faultId;
    /**检定任务编号*/
    private String detectTaskNo;
    /**线体编号*/
    private String lineNo;
    /**原系统标识*/
    private String oldId;
    /**设备编号*/
    private String equipNo;
    /**设备名称*/
    private String equipName;
    /**报警时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date alarmTime;
    /**报警级别*/
    private String alarmLevel;
    /**报警来源*/
    private String alarmSource;
    /**报警原因*/
    private String alarmReason;
    /**
     * 报警原因Id
     */
    private String alarmReasonId;
    /**报警描述*/
    private String alarmDesc;
    /**处理建议*/
    private String alarmSugg;
    /**报警状态*/
    private String alarmStatus;
    /**超期完成时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date overdurTime;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**工单接单时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date acceptTime;
    /**工单派工时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dispatchTime;
    /**工单催办时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date pressTime;
    /**催办标识*/
    private String pressLabe;
    /**工单锁定时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date lockTime;
    /**工单处理时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date processTime;
    /**异常恢复时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date resumeTime;
    /**工单归档时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date recoveryTime;
    /**异常处理方式*/
    private String processType;
    /**工单状态*/
    private String processStatus;
    /**创建人ID*/
    private String createId;
    /**创建人姓名*/
    private String createName;
    /**工单派发人*/
    private String dispatchUser;
    /**工单现场处理人*/
    private String processUser;
    /**远程处理状态*/
    private String remoteStatus;
    /**远程检测是否成功*/
    private String isRemoteSucc;
    /**是否更换设备*/
    private String meterReplace;
    /**领料单号*/
    private String orderOutId;
    /**掌机是否下载*/
    private String isLoad;
    /**知识库设备类型*/
    private String knEquipType;
    /**是否挂单*/
    private String isSuspend;
    /**反馈人*/
    private String fbUser;
    /**工单派发班组*/
    private String dispatchTeam;
    /**工单派发对象*/
    private String dispatchToUser;
    /**恢复类型*/
    private String resumeType;
    /**派发超期时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date dpOverdurTime;
    /**反馈超期时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fbOverdurTime;
    /**修改人*/
    private String updateBy;
    /**修改人名称*/
    private String updateName;
    /**修改时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    /**附件名称*/
    private String enclosureName;
    /**附件地址*/
    private String enclosureUrl;
    /**所属部门*/
    private String sysOrgCode;
    /**网省编号*/
    private String provinceCode;
}
