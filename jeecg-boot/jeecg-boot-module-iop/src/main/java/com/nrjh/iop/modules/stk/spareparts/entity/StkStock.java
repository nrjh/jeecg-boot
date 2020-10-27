package com.nrjh.iop.modules.stk.spareparts.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 库存信息表
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
@Data
@TableName("stk_stock")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="stk_stock对象", description="库存信息表")
public class StkStock implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**物品*/
	@Excel(name = "物品", width = 15)
    @ApiModelProperty(value = "物品")
    private Integer productId;
	/**物品类型，equip:生产设施，spare:备品备件，office_supplies:办公用品*/
	@Excel(name = "物品类型，equip:生产设施，spare:备品备件，office_supplies:办公用品", width = 15)
    @ApiModelProperty(value = "物品类型，equip:生产设施，spare:备品备件，office_supplies:办公用品")
    private String categoryType;
	/**物品编号*/
	@Excel(name = "物品编号", width = 15)
    @ApiModelProperty(value = "物品编号")
    private String productNo;
	/**物品名称*/
	@Excel(name = "物品名称", width = 15)
    @ApiModelProperty(value = "物品名称")
    private String rpoductName;
	/**物品名称缩写*/
	@Excel(name = "物品名称缩写", width = 15)
    @ApiModelProperty(value = "物品名称缩写")
    private String rpoductNameAbb;
	/**采购批次号*/
	@Excel(name = "采购批次号", width = 15)
    @ApiModelProperty(value = "采购批次号")
    private Integer productLotId;
	/**品牌，作为单号使用，单号生成规则在 序号规则[ir_sequence]表 中定义*/
	@Excel(name = "品牌，作为单号使用，单号生成规则在 序号规则[ir_sequence]表 中定义", width = 15)
    @ApiModelProperty(value = "品牌，作为单号使用，单号生成规则在 序号规则[ir_sequence]表 中定义")
    private Integer brandId;
	/**品类*/
	@Excel(name = "品类", width = 15)
    @ApiModelProperty(value = "品类")
    private Integer categoryId;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private Integer attributeCategoryId;
	/**仓库位置*/
	@Excel(name = "仓库位置", width = 15)
    @ApiModelProperty(value = "仓库位置")
    private Integer locationId;
	/**实际库存数量*/
	@Excel(name = "实际库存数量", width = 15)
    @ApiModelProperty(value = "实际库存数量")
    private Integer productQty;
	/**虚拟库存数量*/
	@Excel(name = "虚拟库存数量", width = 15)
    @ApiModelProperty(value = "虚拟库存数量")
    private Integer virtualQty;
	/**物品计量单位*/
	@Excel(name = "物品计量单位", width = 15)
    @ApiModelProperty(value = "物品计量单位")
    private Integer productUomId;
	/**库存下限*/
	@Excel(name = "库存下限", width = 15)
    @ApiModelProperty(value = "库存下限")
    private Integer stockMin;
	/**库存上限*/
	@Excel(name = "库存上限", width = 15)
    @ApiModelProperty(value = "库存上限")
    private Integer stockMax;
	/**单价*/
	@Excel(name = "单价", width = 15)
    @ApiModelProperty(value = "单价")
    private Double price;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**初始数量*/
	@Excel(name = "初始数量", width = 15)
    @ApiModelProperty(value = "初始数量")
    private Integer initQty;
	/**初始总金额*/
	@Excel(name = "初始总金额", width = 15)
    @ApiModelProperty(value = "初始总金额")
    private Double intSumPrice;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
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
