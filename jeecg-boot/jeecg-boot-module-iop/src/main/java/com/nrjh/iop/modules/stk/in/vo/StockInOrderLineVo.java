package com.nrjh.iop.modules.stk.in.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nrjh.iop.modules.stk.in.entity.OrderStockInList;
import com.nrjh.iop.modules.stk.in.entity.StockInOrderLine;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.aspect.annotation.DictIop;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.awt.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 入库明细
 * @Author: jeecg-boot
 * @Date:   2020-08-16
 * @Version: V1.0
 */
@ApiModel(value="stock_in_order对象", description="入库单")
@Data
@TableName("stock_in_order_line")
public class StockInOrderLineVo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.ID_WORKER_STR)
	@ApiModelProperty(value = "标识")
	private Integer id;
	/**采购单*/
	@ApiModelProperty(value = "采购单")
	private Integer orderId;
	/**物料id*/
	@Excel(name = "物料id", width = 15)
	@ApiModelProperty(value = "物料id")
	private Integer productId;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
	@ApiModelProperty(value = "物料名称")
	private String productName;
	/**物品类型*/
	@Excel(name = "物品类型", width = 15)
	@ApiModelProperty(value = "物品类型")
	@Dict(dicCode = "CATEGORY_TYPE")
	private String categoryType;
	/**物料编号*/
	@Excel(name = "物料编号", width = 15)
	@ApiModelProperty(value = "物料编号")
	private String defaultCode;
	/**规格*/
	@Excel(name = "规格", width = 15)
	@ApiModelProperty(value = "规格")
	@DictIop(dicCode = "id",dictTable="prd_attribute_category",dicText="name")
	private Integer attributeCategoryId;
	/**单价*/
	@Excel(name = "单价", width = 15)
	@ApiModelProperty(value = "单价")
	private java.math.BigDecimal priceUnit;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
	@ApiModelProperty(value = "供应商")
	private String partnerName;
	/**应入库数量*/
	@Excel(name = "应入库数量", width = 15)
	@ApiModelProperty(value = "应入库数量")
	private java.math.BigDecimal productPlyQty;
	/**实际入库数量*/
	@Excel(name = "实际入库数量", width = 15)
	@ApiModelProperty(value = "实际入库数量")
	private java.math.BigDecimal productActualQty;
	/**单位*/
	@Excel(name = "单位", width = 15)
	@ApiModelProperty(value = "单位")
	private Integer productUomId;
	/**总价*/
	@Excel(name = "总价", width = 15)
	@ApiModelProperty(value = "总价")
	private java.math.BigDecimal priceTotal;
	/**供应商id*/
	@Excel(name = "供应商id", width = 15)
	@ApiModelProperty(value = "供应商id")
	private Integer partnerId;
	/**入货位时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**合同编号*/
	@Excel(name = "合同编号", width = 15)
	@ApiModelProperty(value = "合同编号")
	private String contract;
	/**标识*/
	@Excel(name = "标识", width = 15)
	@ApiModelProperty(value = "标识")
	private String remark;
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
	private String status;

	@Excel(name = "入货位时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "入货位时间")
	private Date completeTime;

	@ExcelCollection(name="库位明细")
	@ApiModelProperty(value = "库位明细")
	private java.util.List<OrderStockInList> orderStockInLists;
}
