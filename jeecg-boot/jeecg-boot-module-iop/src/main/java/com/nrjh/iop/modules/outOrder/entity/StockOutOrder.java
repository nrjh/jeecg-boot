package com.nrjh.iop.modules.outOrder.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.aspect.annotation.DictIop;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 领料出库单
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
@ApiModel(value="stock_out_order对象", description="领料出库单")
@Data
@TableName("stock_out_order")
public class StockOutOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**出库单号*/
	@Excel(name = "出库单号", width = 15)
    @ApiModelProperty(value = "出库单号")
    private java.lang.String orderOutId;
	/**创建人ID*/
	@Excel(name = "创建人ID", width = 15)
    @ApiModelProperty(value = "创建人ID")
    private java.lang.String createOutBy;
	/**创建人姓名*/
	@Excel(name = "创建人姓名", width = 15)
    @ApiModelProperty(value = "创建人姓名")
    private java.lang.String createOutName;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**物料类型*/
	@Excel(name = "物料类型", width = 15)
    @ApiModelProperty(value = "物料类型")
    @DictIop(dicCode = "IOP_PRD_CATEGORY_TYPE")
    private java.lang.String prdCategory;
	/**所属设施*/
	@Excel(name = "所属设施", width = 15)
    @ApiModelProperty(value = "所属设施")
    private java.lang.Integer belongEquip;
	/**申请班组*/
	@Excel(name = "申请班组", width = 15)
    @ApiModelProperty(value = "申请班组")
    @DictIop(dictTable = "O_SHIFT",dicCode = "SHIFT_ID", dicText = "SHIFT_NAME",dataSource = "ora")
    private java.lang.String team;

	/**申请类型*/
	@Excel(name = "申请类型", width = 15)
    @ApiModelProperty(value = "申请类型")
    @DictIop(dicCode = "IOP_STOCK_OUT_ORDER_ORDER_TYPE")
    private java.lang.String orderType;
	/**预计需求时间*/
	@Excel(name = "预计需求时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预计需求时间")
    private java.util.Date datePlanned;
	/**说明*/
	@Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private java.lang.String note;
	/**审批状态*/
	@Excel(name = "审批状态", width = 15)
    @ApiModelProperty(value = "审批状态")
    @DictIop(dicCode = "ORDER_STATUS")
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
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateInTime;
	/**出库状态*/
	@Excel(name = "出库状态", width = 15)
    @ApiModelProperty(value = "出库状态")
    private java.lang.String outState;
	/**出库人*/
	@Excel(name = "出库人", width = 15)
    @ApiModelProperty(value = "出库人")
    private java.lang.String ouBy;
	/**出库人姓名*/
	@Excel(name = "出库人姓名", width = 15)
    @ApiModelProperty(value = "出库人姓名")
    private java.lang.String ouName;
	/**出库时间*/
	@Excel(name = "出库时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出库时间")
    private java.util.Date outTime;
    /**检定线id*/
    @Excel(name = "检定线id", width = 15)
    @ApiModelProperty(value = "检定线id")
//    @DictIop(dicCode = "o_area,area_name,area_id")
    private java.lang.String lineId;
    /**检定线名称*/
    @Excel(name = "检定线名称", width = 15)
    @ApiModelProperty(value = "检定线名称")
    private java.lang.String lineName;
    /**设备id*/
    @Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
    private java.lang.String equipId;
    /**设备名称*/
    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String equipName;
    /**数据来源*/
    @Excel(name = "数据来源", width = 15)
    @ApiModelProperty(value = "数据来源")
    private java.lang.String dataFrom;

    /**
     * 物料详情
     */
    @TableField(exist = false)
    private String prodDetails;

    @TableLogic
    private Integer isDel;

    /**
     * 查询条件开始日期
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd" )
    @TableField(exist = false)
    private String startDate;

    /**
     * 查询条件结束日期
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd" )
    @TableField(exist = false)
    private String endDate;

    private String nowRoleId;//当前角色id
    private String nextRoleId;//下一个个角色id
    private Integer businessSequence;// 当前角色在 定义表的顺序

}
