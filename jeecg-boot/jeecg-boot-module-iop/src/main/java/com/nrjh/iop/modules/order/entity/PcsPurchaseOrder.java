package com.nrjh.iop.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 采购单
 * @Author: jeecg-boot
 * @Date:   2020-08-15
 * @Version: V1.0
 */
@ApiModel(value="pcs_purchase_order对象", description="采购单")
@Data
@TableName("pcs_purchase_order")
public class PcsPurchaseOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
    @ApiModelProperty(value = "订单编号")
    private String pickingNo;
	/**采购计划-编号*/
	@Excel(name = "采购计划-编号", width = 15)
    @ApiModelProperty(value = "采购计划-编号")
    private String orign;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    @Dict(dicCode = "vendorCode")
    private Integer partnerId;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称")
    private String partnerName;
	/**预计到货时间*/
	@Excel(name = "预计到货时间", width = 15)
    @ApiModelProperty(value = "预计到货时间")
    private String datePlanned;
	/**合同号*/
	@Excel(name = "合同号", width = 15)
    @ApiModelProperty(value = "合同号")
    private String contract;
	/**下单时间*/
	@Excel(name = "下单时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "下单时间")
    private Date dateOrder;
	/**创建人名字*/
	@Excel(name = "创建人名字", width = 15)
    @ApiModelProperty(value = "创建人名字")
    private String createName;
	/**修改人名字*/
	@Excel(name = "修改人名字", width = 15)
    @ApiModelProperty(value = "修改人名字")
    private String updateName;
	/**单据级别*/
	@Excel(name = "单据级别", width = 15)
    @ApiModelProperty(value = "单据级别")
    private String orderType;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String state;
	/**总件数*/
	@Excel(name = "总件数", width = 15)
    @ApiModelProperty(value = "总件数")
    private Integer numberTotal;
	/**总价*/
	@Excel(name = "总价", width = 15)
    @ApiModelProperty(value = "总价")
    private java.math.BigDecimal amountTotal;
	/**说明*/
	@Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private String note;
	/**审批时间*/
	@Excel(name = "审批时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审批时间")
    private Date dateApprove;

    /**删除标识*/
    @Excel(name = "删除标识", width = 15)
    @ApiModelProperty(value = "删除标识")
    private Integer isDel;

    @TableField(exist = false)
	private  int pickingTypeId;
    @TableField(exist = false)
	private  int maxRank;

    /**
     * 采购详情
     */
    @Excel(name = "采购详情", width = 15)
    @TableField(exist = false)
    private String orignContent;
    @TableField(exist = false)
    private String ids;


    private String nowRoleId;//当前角色id
    private String nextRoleId;//下一个个角色id
    private Integer businessSequence;// 当前角色在 定义表的顺序
}
