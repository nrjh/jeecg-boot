package com.nrjh.iop.modules.prd.attrcategory.vo;

import java.util.List;
import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttributeCategory;
import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttrCategAttrValueRel;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 规格
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="prd_attribute_categoryPage对象", description="规格")
public class PrdAttributeCategoryPage {
	
	/**标识*/
	@ApiModelProperty(value = "标识")
	private Integer id;
	/**启用*/
	@Excel(name = "启用", width = 15)
	@ApiModelProperty(value = "启用")
	private Integer active;
	/**序号*/
	@Excel(name = "序号", width = 15)
	@ApiModelProperty(value = "序号")
	private Integer sequence;
	/**名称*/
	@Excel(name = "名称", width = 15)
	@ApiModelProperty(value = "名称")
	private String name;
	/**编码*/
	@Excel(name = "编码", width = 15)
	@ApiModelProperty(value = "编码")
	private String code;
	/**品类  物品品类外键*/
	@Excel(name = "品类  物品品类外键", width = 15)
	@ApiModelProperty(value = "品类  物品品类外键")
	private Integer categoryId;
	/**属性数量*/
	@Excel(name = "属性数量", width = 15)
	@ApiModelProperty(value = "属性数量")
	private Integer attributeQty;
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
	
	@ExcelCollection(name="物品规格与产品属性值关系")
	@ApiModelProperty(value = "物品规格与产品属性值关系")
	private List<PrdAttrCategAttrValueRel> prdAttrCategAttrValueRelList;
	
}
