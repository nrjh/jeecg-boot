package com.nrjh.iop.modules.workOrder.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.DictIop;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/7 15:17
 */
@Data
@ApiModel(value = "工单列表")
public class OrdWorkOrderListVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**工单单号**/
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "工单类型")
    private String orderType;
    @DictIop(dictTable = "o_area", dicText = "area_name", dicCode = "area_id",dataSource = "ora")
    @ApiModelProperty(value = "线体编号")
    private String lineNo;
    @ApiModelProperty(value = "设备名称")
    private String equipName;
    @ApiModelProperty(value = "故障等级")
    private String faultGrade;
    @ApiModelProperty("报警次数")
    private Integer alarmAmount;
    @ApiModelProperty("报警内容")
    private String alarmReason;

    @DictIop(dictTable = "sys_user", dicText = "work_no", dicCode = "id",dataSource = "master")
    @ApiModelProperty(value = "派发人工号")
    private String dispatchToUser;
    @ApiModelProperty(value = "派发人")
    private String dispatchToUserName;
    @ApiModelProperty(value = "掌机是否下载")
    private String isLoad;
    @DictIop(dictTable = "sys_user", dicText = "realname", dicCode = "id",dataSource = "master")
    @ApiModelProperty(value = "处理人")
    private String processUser;
    @DictIop(dictTable = "sys_user", dicText = "realname", dicCode = "id",dataSource = "master")
    @ApiModelProperty(value = "督办人")
    private String urgeName;
    @ApiModelProperty(value = "是否督办")
    private String pressLabe;
    @ApiModelProperty(value = "工单状态")
    private String processStatus;
    @ApiModelProperty("创建人姓名")
    private String createName;
    @ApiModelProperty(value = "工单来源")
    private String orderCreLabe;
    @ApiModelProperty(value = "采集侧任务来源")
    private String collectSideTaskSource;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "派发时间")
    private Date dispatchTime;
    @ApiModelProperty(value = "要求完成时间")
    private Date pressTime;
    @ApiModelProperty(value = "下载时间")
    private Date downTime;
    @ApiModelProperty(value = "处理时间")
    private Date processTime;
    @ApiModelProperty(value = "数据上传时间")
    private Date dataUploadTime;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结单时间")
    private Date updateTime;
    private Date alarmTime;
}
