package com.nrjh.iop.modules.stk.inventory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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

/**
 * @Description: stk_inventory_line
 * @Author: jeecg-boot
 * @Date:   2020-08-07
 * @Version: V1.0
 */
@Data
@TableName("stk_inventory_line")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="stk_inventory_line对象", description="stk_inventory_line")
public class StkInventoryLine implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "标识")
    private Integer id;
	/**盘点单*/
	@Excel(name = "盘点单", width = 15)
    @ApiModelProperty(value = "盘点单")
    private Integer inventoryId;
	/**合作伙伴*/
	@Excel(name = "合作伙伴", width = 15)
    @ApiModelProperty(value = "合作伙伴")
    private Integer partnerId;
	/**物品品类*/
	@Excel(name = "物品品类", width = 15)
    @ApiModelProperty(value = "物品品类")
    private Integer categId;
	/**物品*/
	@Excel(name = "物品", width = 15)
    @ApiModelProperty(value = "物品")
    private Integer productId;
	/**物品计量单位*/
	@Excel(name = "物品计量单位", width = 15)
    @ApiModelProperty(value = "物品计量单位")
    private Integer productUomId;
	/**实际盘点数量
            
            即实际在库存中点查出的设备数量*/
	@Excel(name = "实际盘点数量 即实际在库存中点查出的设备数量", width = 15)
    @ApiModelProperty(value = "实际盘点数量即实际在库存中点查出的设备数量")
    private java.math.BigDecimal productQty;
	/**理论库存数量
            
            即在手数量*/
	@Excel(name = "理论库存数量即在手数量", width = 15)
    @ApiModelProperty(value = "理论库存数量即在手数量")
    private java.math.BigDecimal theoreticalQty;
	/**仓库位置*/
	@Excel(name = "仓库位置", width = 15)
    @ApiModelProperty(value = "仓库位置")
    private java.lang.Integer locationId;
	/**批次/序列号*/
	@Excel(name = "批次/序列号", width = 15)
    @ApiModelProperty(value = "批次/序列号")
    private java.lang.Integer productLotId;
	/**盘点时间*/
	@Excel(name = "盘点时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "盘点时间")
    private java.util.Date inventoryDate;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.Integer companyId;
	/**创建人
            
            公共字段，所有业务表都必须有该字段*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**修改人公共字段，所有业务表都必须有该字段*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
	/**修改时间公共字段，所有业务表都必须有该字段*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
	/**物品编号*/
	@Excel(name = "物品编号", width = 15)
    @ApiModelProperty(value = "物品编号")
    private java.lang.String productNo;
	/**	品牌*/
	@Excel(name = "	品牌", width = 15)
    @ApiModelProperty(value = "	品牌")
    private java.lang.Integer brandId;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private java.lang.Integer attributeCategoryId;
	/**物品状态*/
	@Excel(name = "物品状态", width = 15)
    @ApiModelProperty(value = "物品状态")
    private java.lang.String productStatus;
	/**	采购单号*/
	@Excel(name = "	采购单号", width = 15)
    @ApiModelProperty(value = "	采购单号")
    private java.lang.String buildNo;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private java.lang.Double price;
}
