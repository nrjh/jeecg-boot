package com.nrjh.iop.modules.stk.quant.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 库存数量
 * @Author: jeecg-boot
 * @Date:   2020-06-23
 * @Version: V1.0
 */
@Data
@TableName("stk_quant")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="stk_quant对象", description="库存数量")
public class StkQuant implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "标识")
    private Integer id;
	/**物品*/
	@Excel(name = "物品", width = 15, dictTable = "PRD_PRODUCT", dicText = "NAME", dicCode = "ID",dataSource = "iop")
	@DictIop(dictTable = "PRD_PRODUCT", dicText = "NAME", dicCode = "ID",dataSource = "iop")
    @ApiModelProperty(value = "物品")
    private Integer productId;
	/**批次/序列号*/
	@Excel(name = "批次/序列号", width = 15, dictTable = "stk_production_lot", dicText = "name", dicCode = "id",dataSource = "iop")
	@DictIop(dictTable = "stk_production_lot", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "批次/序列号")
    private Integer productLotId;
	/**仓库位置*/
	@Excel(name = "仓库位置", width = 15, dictTable = "stk_location", dicText = "complete_name", dicCode = "id",dataSource = "iop")
	@DictIop(dictTable = "stk_location", dicText = "complete_name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "仓库位置")
    private Integer locationId;
	/**完成数量*/
	@Excel(name = "完成数量", width = 15)
    @ApiModelProperty(value = "完成数量")
    private Double quantity;
	/**已保留数量*/
	@Excel(name = "已保留数量", width = 15)
    @ApiModelProperty(value = "已保留数量")
    private Double reservedQuantity;
	/**执行时间*/
	@Excel(name = "执行时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "执行时间")
    private Date incomingDate;
	/**单位*/
	@Excel(name = "单位", width = 15, dictTable = "uom_uom", dicText = "name", dicCode = "id",dataSource = "iop")
	@DictIop(dictTable = "uom_uom", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "单位")
    private Integer companyId;
	/**创建人 公共字段，所有业务表都必须有该字段*/
	@Excel(name = "创建人  公共字段，所有业务表都必须有该字段", width = 15)
    @ApiModelProperty(value = "创建人   公共字段，所有业务表都必须有该字段")
    private java.lang.String createBy;
	/**创建时间 公共字段，所有业务表都必须有该字段*/
	@Excel(name = "创建时间  公共字段，所有业务表都必须有该字段", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间  公共字段，所有业务表都必须有该字段")
    private java.util.Date createTime;
	/**修改人  公共字段，所有业务表都必须有该字段*/
	@Excel(name = "修改人  公共字段，所有业务表都必须有该字段", width = 15)
    @ApiModelProperty(value = "修改人   公共字段，所有业务表都必须有该字段")
    private java.lang.String updateBy;
	/**修改时间   公共字段，所有业务表都必须有该字段*/
	@Excel(name = "修改时间  公共字段，所有业务表都必须有该字段", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间   公共字段，所有业务表都必须有该字段")
    private java.util.Date updateTime;
}
