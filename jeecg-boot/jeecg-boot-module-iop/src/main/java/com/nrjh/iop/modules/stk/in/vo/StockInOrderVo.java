package com.nrjh.iop.modules.stk.in.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nrjh.iop.modules.stk.in.entity.StockInOrderLine;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.DictIop;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 入库单
 * @Author: jeecg-boot
 * @Date:   2020-08-16
 * @Version: V1.0
 */
@Data
@ApiModel(value="stock_in_orderPage对象", description="入库单")
public class StockInOrderVo {

	/**标识*/
	@ApiModelProperty(value = "标识")
	private Integer id;
	/**采购入库单号*/
	@Excel(name = "采购入库单号", width = 15)
	@ApiModelProperty(value = "采购入库单号")
	private String orderInId;
	/**采购单号*/
	@Excel(name = "采购单号", width = 15)
	@ApiModelProperty(value = "采购单号")
	private String pickingNo;
	/**合同单号*/
	@Excel(name = "合同单号", width = 15)
	@ApiModelProperty(value = "合同单号")
	private String contract;
	/**供应商id*/
	@Excel(name = "供应商id", width = 15)
	@ApiModelProperty(value = "供应商id")
	private Integer partnerId;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
	@ApiModelProperty(value = "供应商")
	private String partnerName;
	/**预计到货时间*/
	@Excel(name = "预计到货时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "预计到货时间")
	private Date datePlanned;
	/**采购人ID*/
	@Excel(name = "采购人ID", width = 15)
	@ApiModelProperty(value = "采购人ID")
	private String createId;
	/**采购员*/
	@Excel(name = "采购员", width = 15)
	@ApiModelProperty(value = "采购员")
	private String createName;
	/**采购日期*/
	@Excel(name = "采购日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "采购日期")
	private Date createTime;
	/**下单时间*/
	@Excel(name = "下单时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "下单时间")
	private Date dateOrder;
	/**入库完成时间*/
	@Excel(name = "入库完成时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "入库完成时间")
	private Date orderInDate;
	/**单据级别*/
	@Excel(name = "单据级别", width = 15)
	@ApiModelProperty(value = "单据级别")
	private String orderType;
	/**提交状态*/
	@Excel(name = "提交状态", width = 15)
	@ApiModelProperty(value = "提交状态")
	@DictIop(dicCode = "IOP_STK_IN_STATE")
	private String state;
	/**审批时间*/
	@Excel(name = "审批时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "审批时间")
	private Date dateApprove;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String note;
	/**创建人ID*/
	@Excel(name = "创建人ID", width = 15)
	@ApiModelProperty(value = "创建人ID")
	private String createInBy;
	/**创建人姓名*/
	@Excel(name = "创建人姓名", width = 15)
	@ApiModelProperty(value = "创建人姓名")
	private String createInName;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
	private Date createInTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	@ApiModelProperty(value = "修改人")
	private String updateInBy;
	/**修改人名称*/
	@Excel(name = "修改人名称", width = 15)
	@ApiModelProperty(value = "修改人名称")
	private String updateInName;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "修改时间")
	private Date updateInTime;

	/**入库人员*/
	@Excel(name = "入库人员", width = 15)
	@ApiModelProperty(value = "入库人员")
	private String orderInName;

	@ExcelCollection(name="入库明细")
	@ApiModelProperty(value = "入库明细")
	private List<StockInOrderLineVo> stockInOrderLineList;

}
