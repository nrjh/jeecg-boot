package com.nrjh.iop.modules.stk.goodsplaceadjustment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: stock_move_order
 * @Author: jeecg-boot
 * @Date:   2020-10-09
 * @Version: V1.0
 */
@Data
@TableName("stock_move_order")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="stock_move_order对象", description="stock_move_order")
public class StockMoveOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "标识")
    private String id;
	/**移库单号（源单据）*/
	@Excel(name = "移库单号（源单据）", width = 15)
    @ApiModelProperty(value = "移库单号（源单据）")
    private java.lang.String orderMoveId;
	/**调整人ID*/
	@Excel(name = "调整人ID", width = 15)
    @ApiModelProperty(value = "调整人ID")
    private java.lang.String createOutBy;
	/**调整人名称*/
	@Excel(name = "调整人名称", width = 15)
    @ApiModelProperty(value = "调整人名称")
    private java.lang.String createOutName;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createOutTime;
	/**说明*/
	@Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private java.lang.String note;
	/**审批状态*/
	@Excel(name = "审批状态", width = 15)
    @ApiModelProperty(value = "审批状态")
    private java.lang.String state;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateInBy;
	/**修改人名称*/
	@Excel(name = "修改人名称", width = 15)
    @ApiModelProperty(value = "修改人名称")
    private java.lang.String updateInName;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateInTime;
	/**入库状态*/
	@Excel(name = "入库状态", width = 15)
    @ApiModelProperty(value = "入库状态")
    private java.lang.String inState;
	/**入库人*/
	@Excel(name = "入库人", width = 15)
    @ApiModelProperty(value = "入库人")
    private java.lang.String 	inBy;
	/**入库人名称*/
	@Excel(name = "入库人名称", width = 15)
    @ApiModelProperty(value = "入库人名称")
    private java.lang.String inName;
	/**入库时间*/
	@Excel(name = "入库时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "入库时间")
    private java.util.Date 	inTime;
    @TableLogic
    private Integer isDel;
}
