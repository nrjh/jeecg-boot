package com.nrjh.iop.modules.prd.brand.entity;

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
 * @Description: 品牌
 * @Author: jeecg-boot
 * @Date:   2020-04-16
 * @Version: V1.0
 */
@Data
@TableName("prd_brand")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="prd_brand对象", description="品牌")
public class PrdBrand implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "标识")
    private java.lang.Integer id;
	/**启用*/
	@Excel(name = "启用", width = 15,dicCode = "IOP_PUB_ACTION")
    @ApiModelProperty(value = "启用")
    @Dict(dicCode = "IOP_PUB_ACTION")
    private java.lang.Integer active;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
	/**首字母*/
	@Excel(name = "首字母", width = 15)
    @ApiModelProperty(value = "首字母")
    private java.lang.String firstLetter;
	/**制造商*/
	@Excel(name = "制造商", width = 15)
    @ApiModelProperty(value = "制造商")
    private java.lang.Integer manufactorId;
	/**简介*/
	@Excel(name = "简介", width = 15)
    @ApiModelProperty(value = "简介")
    private java.lang.String description;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private java.lang.Integer companyId;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
}
