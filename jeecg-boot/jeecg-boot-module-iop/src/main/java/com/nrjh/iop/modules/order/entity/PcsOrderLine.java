package com.nrjh.iop.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 采购明细
 * @Author: jeecg-boot
 * @Date:   2020-08-15
 * @Version: V1.0
 */
@ApiModel(value="pcs_purchase_order对象", description="采购单")
@Data
@TableName("pcs_order_line")
public class PcsOrderLine implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
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
	/**采购单*/
	@ApiModelProperty(value = "采购单")
	private String orderId;
	/**物品id*/
	@Excel(name = "物品id", width = 15)
	@ApiModelProperty(value = "物品id")
	private Integer productId;
	/**物品名称*/
	@Excel(name = "物品名称", width = 15)
	@ApiModelProperty(value = "物品名称")
	private String productName;
	/**物品类型*/
	@Excel(name = "物品类型", width = 15)
	@ApiModelProperty(value = "物品类型")
	private String categoryType;
	/**内部货号*/
	@Excel(name = "内部货号", width = 15)
	@ApiModelProperty(value = "内部货号")
	private String defaultCode;
	/**规格*/
	@Excel(name = "规格", width = 15)
	@ApiModelProperty(value = "规格")
	private Integer attributeCategoryId;
	/**计量单位*/
	@Excel(name = "计量单位", width = 15)
	@ApiModelProperty(value = "计量单位")
	private Integer productUomId;
	/**单价*/
	@Excel(name = "单价", width = 15)
	@ApiModelProperty(value = "单价")
	private java.math.BigDecimal priceUnit;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
	@ApiModelProperty(value = "供应商")
	private Integer partnerId;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
	@ApiModelProperty(value = "供应商名称")
	private String partnerName;
	/**需求数量*/
	@Excel(name = "计划需求数量", width = 15)
	@ApiModelProperty(value = "需求数量")
	private Integer productPlyQty;
	/**总价*/
	@Excel(name = "总价", width = 15)
	@ApiModelProperty(value = "总价")
	private java.math.BigDecimal priceTotal;
	/**单位*/
	@Excel(name = "单位", width = 15)
	@ApiModelProperty(value = "单位")
	private Integer companyId;
	/**合同号*/
	@Excel(name = "合同号", width = 15)
	@ApiModelProperty(value = "合同号")
	private String contract;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String remark;
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
	private String status;
}
