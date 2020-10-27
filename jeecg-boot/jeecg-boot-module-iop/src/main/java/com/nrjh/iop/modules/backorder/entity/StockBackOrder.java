package com.nrjh.iop.modules.backorder.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 退库单
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
@ApiModel(value="stock_back_order对象", description="退库单")
@Data
@TableName("stock_back_order")
public class StockBackOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**创建人id*/
	@Excel(name = "创建人id", width = 15)
    @ApiModelProperty(value = "创建人id")
    private String createOutBy;
	/**退回人名称*/
	@Excel(name = "退回人名称", width = 15)
    @ApiModelProperty(value = "退回人名称")
    private String createOutName;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateInBy;
	/**修改人名称*/
	@Excel(name = "修改人名称", width = 15)
    @ApiModelProperty(value = "修改人名称")
    private String updateInName;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateInTime;
	/**退库单号*/
	@Excel(name = "退库单号", width = 15)
    @ApiModelProperty(value = "退库单号")
    private String orderBackId;
	/**所属生产设施*/
	@Excel(name = "所属生产设施", width = 15)
    @ApiModelProperty(value = "所属生产设施")
    private String belongEquip;
	/**申请班组*/
	@Excel(name = "申请班组", width = 15)
    @ApiModelProperty(value = "申请班组")
    private String team;
	/**退还类型*/
	@Excel(name = "退还类型", width = 15)
    @ApiModelProperty(value = "退还类型")
    private String orderType;
	/**领料单号*/
	@Excel(name = "领料单号", width = 15)
    @ApiModelProperty(value = "领料单号")
    private String orderOutId;
	/**说明*/
	@Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private String note;
	/**审批状态*/
	@Excel(name = "审批状态", width = 15)
    @ApiModelProperty(value = "审批状态")
    private String state;
	/**入库状态*/
	@Excel(name = "入库状态", width = 15)
    @ApiModelProperty(value = "入库状态")
    private String inState;
	/**入库人*/
	@Excel(name = "入库人", width = 15)
    @ApiModelProperty(value = "入库人")
    private String inBy;
	/**入库人名称*/
	@Excel(name = "入库人名称", width = 15)
    @ApiModelProperty(value = "入库人名称")
    private String inName;
	/**入库时间*/
	@Excel(name = "入库时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "入库时间")
    private Date inTime;

	private String lineNo;

    @TableField(exist = false)
    private String saveFlag;

    @TableField(exist = false)
    private String prodLines;
    @TableLogic
    private Integer isDel;
}
