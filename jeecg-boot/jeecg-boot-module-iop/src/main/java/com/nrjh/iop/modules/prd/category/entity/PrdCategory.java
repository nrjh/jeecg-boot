package com.nrjh.iop.modules.prd.category.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: 品类
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
@Data
@TableName("prd_category")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="prd_category对象", description="品类")
public class PrdCategory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "标识")
    private Integer id;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private Integer sequence;
	/**代码路径*/
	@Excel(name = "代码路径", width = 15)
    @ApiModelProperty(value = "代码路径")
    private String idPath;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
	/**名称路径*/
	@Excel(name = "名称路径", width = 15)
    @ApiModelProperty(value = "名称路径")
    private String completeName;
	/**父级*/
	@Excel(name = "父级", width = 15)
    @ApiModelProperty(value = "父级")
    private String pid;
	/**类型 equip:生产设施;spare:备品备件;office_supplies:办公用品*/
	@Excel(name = "类型", width = 15,dicCode = "IOP_PRD_CATEGORY_TYPE")
    @ApiModelProperty(value = "类型")
	@Dict(dicCode="IOP_PRD_CATEGORY_TYPE")
    private java.lang.String categoryType;
	/**下架策略*/
	@Excel(name = "下架策略", width = 15)
    @ApiModelProperty(value = "下架策略")
    private java.lang.String removalStrategyId;
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
	/**修改时间

            公共字段，所有业务表都必须有该字段*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
	/**状态*/
	@Excel(name = "状态", width = 15,dicCode = "IOP_PUB_ACTION")
    @ApiModelProperty(value = "状态")
	@Dict(dicCode="IOP_PUB_ACTION")
    private java.lang.Integer active;
	@TableLogic
	private Integer isDel;
}
