package com.nrjh.iop.modules.stk.in.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.DictIop;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 入库单
 * @Author: jeecg-boot
 * @Date:   2020-08-16
 * @Version: V1.0
 */
@ApiModel(value="stock_in_order对象", description="入库单")
@Data
@TableName("stock_in_order")
public class StockInOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "标识")
    private java.lang.Integer id;
	/**采购入库单号*/
	@Excel(name = "采购入库单号", width = 15)
    @ApiModelProperty(value = "采购入库单号")
    private java.lang.String orderInId;
	/**采购单号*/
	@Excel(name = "采购单号", width = 15)
    @ApiModelProperty(value = "采购单号")
    private java.lang.String pickingNo;
	/**合同单号*/
	@Excel(name = "合同单号", width = 15)
    @ApiModelProperty(value = "合同单号")
    private java.lang.String contract;
	/**供应商id*/
	@Excel(name = "供应商id", width = 15, dicCode = "vendorCode")
    @Dict(dicCode = "vendorCode")
    @ApiModelProperty(value = "供应商id")
    private java.lang.Integer partnerId;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private java.lang.String partnerName;
	/**预计到货时间*/
	@Excel(name = "预计到货时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预计到货时间")
    private java.util.Date datePlanned;
	/**采购人ID*/
	@Excel(name = "采购人ID", width = 15)
    @ApiModelProperty(value = "采购人ID")
    private java.lang.String createId;
	/**采购员*/
	@Excel(name = "采购员", width = 15)
    @ApiModelProperty(value = "采购员")
    private java.lang.String createName;
	/**采购日期*/
	@Excel(name = "采购日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "采购日期")
    private java.util.Date createTime;
	/**下单时间*/
	@Excel(name = "下单时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "下单时间")
    private java.util.Date dateOrder;
	/**入库完成时间*/
	@Excel(name = "入库完成时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "入库完成时间")
    private java.util.Date orderInDate;
	/**单据级别*/
	@Excel(name = "单据级别", width = 15, dicCode = "sex")
    @Dict(dicCode = "sex")
    @ApiModelProperty(value = "单据级别")
    private java.lang.String orderType;
	/**提交状态*/
	@Excel(name = "提交状态", width = 15)
    @ApiModelProperty(value = "提交状态")
    @DictIop(dicCode = "IOP_STK_IN_STATE")
    private java.lang.String state;
	/**审批时间*/
	@Excel(name = "审批时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审批时间")
    private java.util.Date dateApprove;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;
	/**创建人ID*/
	@Excel(name = "创建人ID", width = 15)
    @ApiModelProperty(value = "创建人ID")
    private java.lang.String createInBy;
	/**创建人姓名*/
	@Excel(name = "创建人姓名", width = 15)
    @ApiModelProperty(value = "创建人姓名")
    private java.lang.String createInName;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createInTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateInBy;
	/**修改人名称*/
	@Excel(name = "修改人名称", width = 15)
    @ApiModelProperty(value = "修改人名称")
    private java.lang.String updateInName;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateInTime;
    /**入库人员*/
    @Excel(name = "入库人员", width = 15)
    @ApiModelProperty(value = "入库人员")
    private String orderInName;


    private String nowRoleId;//当前角色id
    private String nextRoleId;//下一个个角色id
    private Integer businessSequence;// 当前角色在 定义表的顺序
}
