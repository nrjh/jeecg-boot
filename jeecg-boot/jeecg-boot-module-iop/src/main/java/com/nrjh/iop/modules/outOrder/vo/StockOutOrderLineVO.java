package com.nrjh.iop.modules.outOrder.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nrjh.iop.modules.stk.in.entity.OrderStockInList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.DictIop;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 出库明细表
 * @Author: jeecg-boot
 * @Date: 2020-08-19
 * @Version: V1.0
 */
@ApiModel(value = "stock_out_order对象", description = "领料出库单")
@Data
@TableName("stock_out_order_line")
public class StockOutOrderLineVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 出库单号
     */
    @ApiModelProperty(value = "出库单号")
    private Integer orderId;
    /**
     * 物品id
     */
    @Excel(name = "物品id", width = 15)
    @ApiModelProperty(value = "物品id")
    private java.lang.Integer productId;
    /**
     * 物品名称
     */
    @Excel(name = "物品名称", width = 15)
    @ApiModelProperty(value = "物品名称")
    private java.lang.String productName;
    /**
     * 内部货号
     */
    @Excel(name = "内部货号", width = 15)
    @ApiModelProperty(value = "内部货号")
    private java.lang.String defaultCode;
    /**
     * 物品类型
     */
    @Excel(name = "物品类型", width = 15)
    @ApiModelProperty(value = "物品类型")
    private java.lang.String categoryType;
    /**
     * 规格
     */
    @Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private java.lang.Integer attributeCategoryId;
    /**
     * 物品计量单位
     */
    @Excel(name = "物品计量单位", width = 15)
    @ApiModelProperty(value = "物品计量单位")
    @DictIop(dictTable = "uom_uom", dicText = "name", dicCode = "id",dataSource = "iop")
    private java.lang.Integer productUomId;
    /**
     * 合同号
     */
    @Excel(name = "合同号", width = 15)
    @ApiModelProperty(value = "合同号")
    private java.lang.String contract;
    /**
     * 单价
     */
    @Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.lang.Double priceUnit;
    /**
     * 供应商
     */
    @Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private java.lang.Integer partnerId;
    /**
     * 供应商名称
     */
    @Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称")
    private java.lang.String partnerName;
    /**
     * 虚拟库存数量
     */
    @Excel(name = "虚拟库存数量", width = 15)
    @ApiModelProperty(value = "虚拟库存数量")
    private java.lang.Integer virtualQty;
    /**
     * 出库数量
     */
    @Excel(name = "出库数量", width = 15)
    @ApiModelProperty(value = "出库数量")
    private java.lang.Integer productPlyQty;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.String status;
    /**
     * 实际出库数量
     */
    @Excel(name = "实际出库数量", width = 15)
    @ApiModelProperty(value = "实际出库数量")
    private java.lang.Integer productActualQty;


    @Excel(name = "实际库存数量", width = 15)
    @ApiModelProperty(value = "实际库存数量")
    @TableField(exist = false)
    private java.lang.Integer productQty;

    @ApiModelProperty(value = "货位")
    private String whInputStockLotName;

    @ExcelCollection(name = "出库货位表")
    @ApiModelProperty(value = "出库货位表")
    private List<OrderStockInList> orderStockInLists;
}
