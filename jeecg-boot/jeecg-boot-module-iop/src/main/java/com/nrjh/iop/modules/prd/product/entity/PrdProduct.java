package com.nrjh.iop.modules.prd.product.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.DictIop;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 物品
 * @Author: jeecg-boot
 * @Date:   2020-05-06
 * @Version: V1.0
 */
@ApiModel(value="prd_product对象", description="物品")
@Data
@TableName("prd_product")
public class PrdProduct implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "标识")
    private Integer id;
	/**启用*/
	@Excel(name = "启用", width = 15,dicCode = "IOP_PUB_ACTION")
    @ApiModelProperty(value = "启用")
    @DictIop(dicCode = "IOP_PUB_ACTION")
    private Integer active;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
	/**内部货号*/
	@Excel(name = "内部货号", width = 15)
    @ApiModelProperty(value = "内部货号")
    private String defaultCode;
	/**条形码*/
	@Excel(name = "条形码", width = 15)
    @ApiModelProperty(value = "条形码")
    private String barcode;
	/**品类*/
	@Excel(name = "品类", width = 15)
    @ApiModelProperty(value = "品类")
    @DictIop(dictTable = "PRD_CATEGORY" ,dicCode = "id",dicText = "name",dataSource = "iop")
    private Integer categoryId;
	/**品牌*/
	@Excel(name = "品牌", width = 15)
    @ApiModelProperty(value = "品牌")
    @DictIop(dictTable = "PRD_BRAND" ,dicCode = "id",dicText = "name",dataSource = "iop")
    private Integer brandId;
	/**制造商  选择品牌时从品牌信息带入*/
	@Excel(name = "制造商", width = 15)
    @ApiModelProperty(value = "制造商")
    private Integer manufactorId;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private Integer vendorId;
	/**所属生产设施  备品备件时，标明该备件可用于哪个生产设施（四线一库）*/
	@Excel(name = "所属生产设施", width = 15,dicCode = "IOP_PUB_CATEGORY")
    @ApiModelProperty(value = "所属生产设施")
    @DictIop(dicCode = "IOP_PUB_CATEGORY")
    private String belongEquip;
	/**计量单位 与 计量单位[UOM_UOM]表关联*/
	@Excel(name = "计量单位", width = 15)
    @ApiModelProperty(value = "计量单位")
    @DictIop(dictTable = "uom_uom" ,dicCode = "id",dicText = "name",dataSource = "iop")
    private Integer uomId;
	/**被替代 当该物品（设备/备件/办公用品等）被其他设备替代时，该字段为true，默认为false*/
	@Excel(name = "被替代", width = 15)
    @ApiModelProperty(value = "被替代")
    private Integer replaced;
	/**内部说明*/		
	@Excel(name = "内部说明", width = 15)
    @ApiModelProperty(value = "内部说明")
    private String description;
	/**重量*/
	@Excel(name = "重量", width = 15)
    @ApiModelProperty(value = "重量")
    private java.math.BigDecimal weight;
	/**体积*/
	@Excel(name = "体积", width = 15)
    @ApiModelProperty(value = "体积")
    private java.math.BigDecimal volumn;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String writeBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date writeTime;
}
