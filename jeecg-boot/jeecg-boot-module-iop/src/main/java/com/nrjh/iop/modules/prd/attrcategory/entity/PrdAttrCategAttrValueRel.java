package com.nrjh.iop.modules.prd.attrcategory.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 物品规格与产品属性值关系
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
@ApiModel(value="prd_attribute_category对象", description="规格")
@Data
@TableName("prd_attr_categ_attr_value_rel")
public class PrdAttrCategAttrValueRel implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "标识")
	private Integer id;
	/**启用*/
	@Excel(name = "启用", width = 15)
	@ApiModelProperty(value = "启用")
	private Integer active;
	/**物品规格*/
	@ApiModelProperty(value = "物品规格")
	private Integer attributeCategoryId;
	/**物品属性 可直接获取产品属性信息，与“属性[PRODUCT_ATTRIBUTE]”表的关联外键*/
	@Excel(name = "属性", width = 15, dictTable = "PRD_ATTRIBUTE", dicText = "NAME", dicCode = "ID")
	@ApiModelProperty(value = "属性")
	private Integer attributeId;
	/**属性值 可关联获取对应属性的属性值，与“属性值[product_attribute_value]“表的关联外键*/
	@Excel(name = "属性值", width = 15, dictTable = "PRD_ATTRIBUTE_VALUE", dicText = "NAME", dicCode = "ID")
	@ApiModelProperty(value = "属性值")
	private Integer attributeValueId;
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
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
}
