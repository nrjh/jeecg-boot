package com.nrjh.iop.modules.stk.goodsplaceadjustment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: stock_move_order_line
 * @Author: jeecg-boot
 * @Date:   2020-10-09
 * @Version: V1.0
 */
@Data
@TableName("stock_move_order_line")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="stock_move_order_line对象", description="stock_move_order_line")
public class StockMoveOrderLine implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
    @TableId(type = IdType.AUTO)
	@Excel(name = "标识", width = 15)
    @ApiModelProperty(value = "标识")
    private Integer stockInOrderId;
	/**移库单号*/
	@Excel(name = "移库单号", width = 15)
    @ApiModelProperty(value = "移库单号")
    private String orderId;
	/**物品id*/
	@Excel(name = "物品id", width = 15)
    @ApiModelProperty(value = "物品id")
    private String productId;
	/**物品名称*/
	@Excel(name = "物品名称", width = 15)
    @ApiModelProperty(value = "物品名称")
    private String 	productName;
	/**物品类型（equip:生产设施spare:备品备件office_supplies:办公用品）*/
	@Excel(name = "物品类型（equip:生产设施 spare:备品备件 office_supplies:办公用品）", width = 15)
    @ApiModelProperty(value = "物品类型（equip:生产设施 spare:备品备件 office_supplies:办公用品）")
    private java.lang.String 	categoryType;
	/**内部货号（SKU#）*/
	@Excel(name = "内部货号（SKU#）", width = 15)
    @ApiModelProperty(value = "内部货号（SKU#）")
    private java.lang.String defaultCode;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private java.lang.Integer attributeCategoryId;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.math.BigDecimal priceUnit;
	/**
     * 移出库存位置（入库区库存位置，在创建仓库时，自动关联创建该位置[stk_location]信息）
     * 可存入多个位置id
     * */
	@Excel(name = "移出库存位置（入库区库存位置，在创建仓库时，自动关联创建该位置[stk_location]信息）", width = 15)
    @ApiModelProperty(value = "移出库存位置（入库区库存位置，在创建仓库时，自动关联创建该位置[stk_location]信息）")
    private java.lang.String whInputStockLotId;
	/**
     * 移出库位置名称（入库区库存位置，在创建仓库时，自动关联创建该位置[stk_location]信息）
     * 可存入多个位置名称
     * */
	@Excel(name = "移出库位置名称（入库区库存位置，在创建仓库时，自动关联创建该位置[stk_location]信息）", width = 15)
    @ApiModelProperty(value = "移出库位置名称（入库区库存位置，在创建仓库时，自动关联创建该位置[stk_location]信息）")
    private java.lang.String whInputStockLotName;
	/**移出库前库数量*/
	@Excel(name = "移出库前库数量", width = 15)
    @ApiModelProperty(value = "移出库前库数量")
    private java.math.BigDecimal productBeforeActualQty;
	/**计划移库数量*/
	@Excel(name = "计划移库数量", width = 15)
    @ApiModelProperty(value = "计划移库数量")
    private java.math.BigDecimal productMoveQty;
	/**实际库数量*/
	@Excel(name = "实际库数量", width = 15)
    @ApiModelProperty(value = "实际库数量")
    private java.lang.Integer productActualQty;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private java.lang.Integer partnerId;
	/**	供应商名称*/
	@Excel(name = "	供应商名称", width = 15)
    @ApiModelProperty(value = "	供应商名称")
    private java.lang.String partnerName;
	/**物品计量单位*/
	@Excel(name = "物品计量单位", width = 15)
    @ApiModelProperty(value = "物品计量单位")
    private java.lang.Integer productUomId;
	/**总价*/
	@Excel(name = "总价", width = 15)
    @ApiModelProperty(value = "总价")
    private java.math.BigDecimal 	priceTotal;
	/**入库完成时间*/
	@Excel(name = "入库完成时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "入库完成时间")
    private Date createTime;
	/**	合同号*/
	@Excel(name = "	合同号", width = 15)
    @ApiModelProperty(value = "	合同号")
    private String contract;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
    @TableLogic
    private Integer isDel;
}
