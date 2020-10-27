package com.nrjh.iop.modules.backorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: 退回入库明细
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
@ApiModel(value="stock_back_order对象", description="退库单")
@Data
@TableName("stock_back_order_line")
public class StockBackOrderLine implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键")
	private Integer id;
	/**入库完成时间*/
	@Excel(name = "入库完成时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "入库完成时间")
	private Date createTime;
	/**退库单id*/
	@ApiModelProperty(value = "退库单id")
	private Integer orderId;
	/**商品id*/
	@Excel(name = "商品id", width = 15)
	@ApiModelProperty(value = "商品id")
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
	/**单价*/
	@Excel(name = "单价", width = 15)
	@ApiModelProperty(value = "单价")
	private java.math.BigDecimal priceUnit;
	/**供应商id*/
	@Excel(name = "供应商id", width = 15)
	@ApiModelProperty(value = "供应商id")
	private String partnerId;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
	@ApiModelProperty(value = "供应商名称")
	private String partnerName;
	/**应入库数量*/
	@Excel(name = "应入库数量", width = 15)
	@ApiModelProperty(value = "应入库数量")
	private Integer productQty;
	/**实际入库数量*/
	@Excel(name = "实际入库数量", width = 15)
	@ApiModelProperty(value = "实际入库数量")
	private Integer productActualQty;
	/**物品计量单位*/
	@Excel(name = "物品计量单位", width = 15)
	@ApiModelProperty(value = "物品计量单位")
	private Integer productUomId;
	/**总价*/
	@Excel(name = "总价", width = 15)
	@ApiModelProperty(value = "总价")
	private Integer priceTotal;
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
	/**退回*/
	@ApiModelProperty(value = "退回")
	private Integer returnCount;
	@TableLogic
	private Integer isDel;
}
