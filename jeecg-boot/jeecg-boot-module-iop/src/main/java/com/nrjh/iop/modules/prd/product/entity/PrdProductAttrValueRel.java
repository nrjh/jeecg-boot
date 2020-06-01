package com.nrjh.iop.modules.prd.product.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.DictIop;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 物品属性值关系
 * @Author: jeecg-boot
 * @Date:   2020-05-06
 * @Version: V1.0
 */
@ApiModel(value="prd_product对象", description="物品与属性与属性值关系")
@Data
@TableName("prd_product_attr_value_rel")
public class PrdProductAttrValueRel implements Serializable {
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
	/**物品*/
	@ApiModelProperty(value = "物品")
	private Integer productId;
	/**产品属性*/
	@Excel(name = "产品属性", width = 15)
	@ApiModelProperty(value = "产品属性")
	@DictIop(dictTable = "PRD_ATTRIBUTE" ,dicCode = "id",dicText = "name",dataSource = "iop")
	private Integer attributeId;
	/**属性值*/
	@Excel(name = "属性值", width = 15)
	@ApiModelProperty(value = "属性值")
	@DictIop(dictTable = "PRD_ATTRIBUTE_VALUE" ,dicCode = "id",dicText = "name",dataSource = "iop")
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
	private String writeBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "修改时间")
	private Date writeTime;
}
