package com.nrjh.iop.modules.workOrder.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/8 11:32
 */
@Data
public class OrdWorkOrderMenuVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单编号")
    private String id;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "报警时间")
    private Date alarmTime;

    @ApiModelProperty(value = "线体编号")
    private String lineNo;

    @ApiModelProperty(value = "设备名称")
    private String equipName;

    @ApiModelProperty(value = "故障等级")
    private String faultGrade;

    @ApiModelProperty(value = "故障类型")

    private String faultType;

    // 工单内容 - 报警描述
    @ApiModelProperty(value = "工单内容-报警内容-报警描述-描述")
    private String alarmDesc;

    @ApiModelProperty(value = "处理人")
    private String processUser;
    // 工单名称 - 报警原因
    @ApiModelProperty(value = "报警原因")
    private String alarmReason;

    // 附件
    @ApiModelProperty(value = "附件")
    private String enclosurenName;

    // 班组
    @ApiModelProperty(value = "班组")
    private String dispatchTeam;

    // 上报时间
    @ApiModelProperty(value = "上报时间")
    private Date notifyTime;
    // 上报人
    @ApiModelProperty(value = "上报人")
    private String notifyPerson;
    // 上报人联系方式
    @ApiModelProperty(value = "联系方式")
    private String notifyContactWay;
    // 登记人
    @ApiModelProperty("登记人")
    private String registerPerson;
    // 登记人联系方式
    @ApiModelProperty("登记人联系方式")
    private String registerContactWay;

    /**
     * 处理信息
     */
    @ApiModelProperty(value = "处理结果")
    private String solveResult;

    @ApiModelProperty(value = "修复方法")
    private String repairName;

    @ApiModelProperty(value = "领料编号")
    private String orderOutId;

    @ApiModelProperty(value = "处理备注")
    private String resolveReason;

    @ApiModelProperty(value = "处理时间")
    private Date solveTime;

    /**
     * 转派
     */
    @ApiModelProperty(value = "转派处理人")
    private String turnResolvePerson;

    /**
     * 结单
     */
    @ApiModelProperty(value = "结单时间")
    private Date updateTime;
    @ApiModelProperty(value = "结单类型")
    @Dict(dicCode = "ord_work_order_type")
    private String orderType;
    // 工单催办时间- 创建时间
    @ApiModelProperty(value = "工单时长")
    private String howTime;


    /**
     * 挂单
     */
    @ApiModelProperty(value = "挂单内容")
    private String listContent;
    @ApiModelProperty(value = "挂单结束时间")
    private Date listTime;

    @ApiModelProperty(value = "解除")
    private String untieContent;
    @ApiModelProperty(value = "解除时间")
    private Date untieTime;

}
