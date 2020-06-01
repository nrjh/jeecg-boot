package com.nrjh.iop.modules.stk.picking.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.DictIop;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 调拨单
 * @Author: jeecg-boot
 * @Date:   2020-05-18
 * @Version: V1.0
 */
@ApiModel(value="stk_picking对象", description="调拨单")
@Data
@TableName("stk_picking")
public class StkPicking implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "标识")
    private Integer id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
	/**源单据*/
	@Excel(name = "源单据", width = 15)
    @ApiModelProperty(value = "源单据")
    private String orign;
	/**作业类型*/
	@Excel(name = "作业类型", width = 15)
    @DictIop(dictTable = "stk_picking_type", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "作业类型")
    private Integer pickingTypeId;
    /**优先级*/
	@Excel(name = "优先级", width = 15, dicCode = "IOP_STK_PICKING_PRIORITY")
    @DictIop(dicCode = "IOP_STK_PICKING_PRIORITY")
    @ApiModelProperty(value = "优先级")
    private String priority;
	/**移动类型*/
	@Excel(name = "移动类型", width = 15, dicCode = "IOP_STK_MOVE_TYPE")
    @DictIop(dicCode = "IOP_STK_PICKING_MOVE_TYPE")
    @ApiModelProperty(value = "移动类型")
    private String moveType;
	/**计划执行时间*/
	@Excel(name = "计划执行时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "计划执行时间")
    private Date scheduledDate;
	/**实际完成时间*/
	@Excel(name = "实际完成时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "实际完成时间")
    private Date dateDone;
	/**伙伴*/
	@Excel(name = "伙伴", width = 15)
    @ApiModelProperty(value = "伙伴")
    private Integer partnerId;
	/**已打印*/
	@Excel(name = "已打印", width = 15,dicCode = "yn")
    @DictIop(dicCode = "yn")
    @ApiModelProperty(value = "已打印")
    private Integer printed;
	/**被锁定*/
	@Excel(name = "被锁定", width = 15,dicCode = "yn")
    @DictIop(dicCode = "yn")
    @ApiModelProperty(value = "被锁定")
    private Integer izLocked;
	/**立即转移 */
	@Excel(name = "立即转移 ", width = 15,dicCode = "yn")
    @DictIop(dicCode = "yn")
    @ApiModelProperty(value = "立即转移 ")
    private Integer immediateTranster;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "IOP_STK_PICKING_STATE")
    @DictIop(dicCode = "IOP_STK_PICKING_STATE")
    @ApiModelProperty(value = "状态")
    private String state;
	/**欠单*/
	@Excel(name = "欠单", width = 15)
    @ApiModelProperty(value = "欠单")
    private Integer backorderId;
	/**源位置*/
	@Excel(name = "源位置", width = 15)
    @DictIop(dictTable = "stk_location", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "源位置")
    private Integer locationSrcId;
	/**目标位置*/
	@Excel(name = "目标位置", width = 15)
    @DictIop(dictTable = "stk_location", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "目标位置")
    private Integer locationDestId;
	/**负责人*/
	@Excel(name = "负责人", width = 32)
    @ApiModelProperty(value = "负责人")
    @DictIop(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    private String userId;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String note;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private Integer companyId;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
