package com.nrjh.iop.modules.uom.category.entity;

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
 * @Description: 计量单位
 * @Author: jeecg-boot
 * @Date:   2020-04-29
 * @Version: V1.0
 */
@ApiModel(value="uom_category对象", description="计量单位类别")
@Data
@TableName("uom_uom")
public class UomUom implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "标识")
	private Integer id;
	/**计量类别*/
	@ApiModelProperty(value = "计量类别")
	private Integer categoryId;
	/**比例*/
	@Excel(name = "比例", width = 15)
	@ApiModelProperty(value = "比例")
	private java.math.BigDecimal factor;
	/**精度*/
	@Excel(name = "精度", width = 15)
	@ApiModelProperty(value = "精度")
	private java.math.BigDecimal rounding;
	/**启用*/
	@Excel(name = "启用", width = 15, dicCode = "IOP_PUB_ACTION")
	@ApiModelProperty(value = "启用")
	private Integer active;
	/**单位类型*/
	@Excel(name = "单位类型", width = 15, dicCode = "IOP_UOM_UOM_TYPE")
	@ApiModelProperty(value = "单位类型")
	private String uomType;
	/**名称*/
	@Excel(name = "名称", width = 15)
	@ApiModelProperty(value = "名称")
	private String name;
	/**计量类型*/
	@Excel(name = "计量类型", width = 15, dicCode = "IOP_UOM_MEASURE_TYPE")
	@ApiModelProperty(value = "计量类型")
	private String measureType;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	@ApiModelProperty(value = "创建人")
	private String createUid;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	@ApiModelProperty(value = "修改人")
	private String writeUid;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "修改时间")
	private Date writeTime;
}
