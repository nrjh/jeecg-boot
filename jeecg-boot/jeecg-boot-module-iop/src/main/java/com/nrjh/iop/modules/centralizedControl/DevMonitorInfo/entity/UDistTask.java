package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: U_DIST_TASK
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Data
@TableName("midhd_mds.u_dist_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="u_dist_task对象", description="U_DIST_TASK")
public class UDistTask implements Serializable {
    private static final long serialVersionUID = 1L;

	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**设备类别*/
	@Excel(name = "设备类别", width = 15)
    @ApiModelProperty(value = "设备类别")
    private String equipCateg;
	/**是否解除。01是、02否*/
	@Excel(name = "是否解除。01是、02否", width = 15)
    @ApiModelProperty(value = "是否解除。01是、02否")
    private String isRemove;
	/**接收单位，接收库房所属单位*/
	@Excel(name = "接收单位，接收库房所属单位", width = 15)
    @ApiModelProperty(value = "接收单位，接收库房所属单位")
    private String orgNo;
	/**返配送任务类型：01 报废鉴定02 报废03 运行抽检04 故障检测05 临时检定06 委托检定07 省中心召回08 配送质量检测09 地市超期10 用户申校99 其他*/
	@Excel(name = "返配送任务类型： 01 报废鉴定 02 报废 03 运行抽检 04 故障检测 05 临时检定 06 委托检定 07 省中心召回 08 配送质量检测 09 地市超期 10 用户申校 99 其他", width = 15)
    @ApiModelProperty(value = "返配送任务类型： 01 报废鉴定 02 报废 03 运行抽检 04 故障检测 05 临时检定 06 委托检定 07 省中心召回 08 配送质量检测 09 地市超期 10 用户申校 99 其他")
    private java.lang.String returnTaskType;
	/**返配送备注*/
	@Excel(name = "返配送备注", width = 15)
    @ApiModelProperty(value = "返配送备注")
    private java.lang.String returnRemark;
	/**配送任务数量*/
	@Excel(name = "配送任务数量", width = 15)
    @ApiModelProperty(value = "配送任务数量")
    private java.lang.Integer distTaskNum;
	/**配送确认数量*/
	@Excel(name = "配送确认数量", width = 15)
    @ApiModelProperty(value = "配送确认数量")
    private java.lang.Integer distConfirmNum;
	/**订单类型，参照视图VW_PROP_LIST*/
	@Excel(name = "订单类型，参照视图VW_PROP_LIST", width = 15)
    @ApiModelProperty(value = "订单类型，参照视图VW_PROP_LIST")
    private java.lang.String propListId;
	/**业扩工单号*/
	@Excel(name = "业扩工单号", width = 15)
    @ApiModelProperty(value = "业扩工单号")
    private java.lang.String propListNum;
	/**审批人员*/
	@Excel(name = "审批人员", width = 15)
    @ApiModelProperty(value = "审批人员")
    private java.lang.String approverNo;
	/**审批人员姓名*/
	@Excel(name = "审批人员姓名", width = 15)
    @ApiModelProperty(value = "审批人员姓名")
    private java.lang.String approverName;
	/**审批时间*/
	@Excel(name = "审批时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审批时间")
    private Date approverDate;
	/**拒收原因*/
	@Excel(name = "拒收原因", width = 15)
    @ApiModelProperty(value = "拒收原因")
    private String rejestReason;
	/**营销配送入库单号*/
	@Excel(name = "营销配送入库单号", width = 15)
    @ApiModelProperty(value = "营销配送入库单号")
    private String sgIoAppNo;
	/**是否直配：0普通配送，1直配*/
	@Excel(name = "是否直配：0普通配送，1直配", width = 15)
    @ApiModelProperty(value = "是否直配：0普通配送，1直配")
    private String unionRule;
	/**直配库房编号*/
	@Excel(name = "直配库房编号", width = 15)
    @ApiModelProperty(value = "直配库房编号")
    private String distWhNo;
	/**排程任务编号*/
	@Excel(name = "排程任务编号", width = 15)
    @ApiModelProperty(value = "排程任务编号")
    private String scheTaskNo;
	/**配送任务序号*/
	@Excel(name = "配送任务序号", width = 15)
    @ApiModelProperty(value = "配送任务序号")
    private String seqNo;
	/**业扩名称/工程名称*/
	@Excel(name = "业扩名称/工程名称", width = 15)
    @ApiModelProperty(value = "业扩名称/工程名称")
    private String projectName;
	/**配送签收人员姓名*/
	@Excel(name = "配送签收人员姓名", width = 15)
    @ApiModelProperty(value = "配送签收人员姓名")
    private String signName;
	/**配送签收人员电话*/
	@Excel(name = "配送签收人员电话", width = 15)
    @ApiModelProperty(value = "配送签收人员电话")
    private String signTel;
	/**配送签收时间*/
	@Excel(name = "配送签收时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "配送签收时间")
    private Date signDate;
	/**合同所属 01：城网，02：农网*/
	@Excel(name = "合同所属 01：城网，02：农网", width = 15)
    @ApiModelProperty(value = "合同所属 01：城网，02：农网")
    private String contractType;
	/**是否主动配送，1：是，0：否。*/
	@Excel(name = "是否主动配送，1：是，0：否。", width = 15)
    @ApiModelProperty(value = "是否主动配送，1：是，0：否。")
    private String isActiveDist;
	/**配送任务标识*/
	@Excel(name = "配送任务标识", width = 15)
    @ApiModelProperty(value = "配送任务标识")
    private Integer distTaskId;
	/**配送任务编号*/
	@Excel(name = "配送任务编号", width = 15)
    @ApiModelProperty(value = "配送任务编号")
    private String taskNo;
	/**配送计划编号*/
	@Excel(name = "配送计划编号", width = 15)
    @ApiModelProperty(value = "配送计划编号")
    private String distPlanNo;
	/**任务级别
（参照视图：待定）*/
	@Excel(name = "任务级别（参照视图：待定）", width = 15)
    @ApiModelProperty(value = "任务级别（参照视图：待定）")
    private java.lang.String taskPrio;
	/**配送单位*/
	@Excel(name = "配送单位", width = 15)
    @ApiModelProperty(value = "配送单位")
    private java.lang.String distDeptNo;
	/**配送人员*/
	@Excel(name = "配送人员", width = 15)
    @ApiModelProperty(value = "配送人员")
    private java.lang.String distName;
	/**任务状态：01 未提交02 已提交03 已归档04 终止05 配送中06 已接收07 已拒收02 已提交03 已归档04 终止05 配送中06 已接收07 已拒收*/
	@Excel(name = "任务状态： 01 未提交 02 已提交 03 已归档 04 终止 05 配送中 06 已接收 07 已拒收 02 已提交 03 已归档 04 终止 05 配送中 06 已接收 07 已拒收", width = 15)
    @ApiModelProperty(value = "任务状态： 01 未提交 02 已提交 03 已归档 04 终止 05 配送中 06 已接收 07 已拒收 02 已提交 03 已归档 04 终止 05 配送中 06 已接收 07 已拒收")
    private java.lang.String taskStatus;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String taskMaker;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date madeTime;
	/**配送任务模式 01:单点多品规配送 02:多点单品规配送。
（直配默认写01）*/
	@Excel(name = "配送任务模式 01:单点多品规配送 02:多点单品规配送。（直配默认写01）", width = 15)
    @ApiModelProperty(value = "配送任务模式 01:单点多品规配送 02:多点单品规配送。（直配默认写01）")
    private java.lang.String distMode;
	/**派车信息标识*/
	@Excel(name = "派车信息标识", width = 15)
    @ApiModelProperty(value = "派车信息标识")
    private java.lang.Integer autoInfo;
	/**线路信息标识*/
	@Excel(name = "线路信息标识", width = 15)
    @ApiModelProperty(value = "线路信息标识")
    private java.lang.Integer pathInfo;
	/**干预人*/
	@Excel(name = "干预人", width = 15)
    @ApiModelProperty(value = "干预人")
    private java.lang.String intervenorNo;
	/**干预类型01-暂停02-终止03-重启04-提升优先级05-降低优先级*/
	@Excel(name = "干预类型 01-暂停 02-终止 03-重启 04-提升优先级 05-降低优先级", width = 15)
    @ApiModelProperty(value = "干预类型 01-暂停 02-终止 03-重启 04-提升优先级 05-降低优先级")
    private java.lang.String interveneType;
	/**干预时间*/
	@Excel(name = "干预时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "干预时间")
    private java.util.Date interveneTime;
    @TableLogic
    private Integer isDel;
}
