package com.nrjh.iop.modules.outOrder.vo;

import java.util.List;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrder;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrderLine;
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
 * @Description: 领料出库单
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
@Data
@ApiModel(value="stock_out_orderPage对象", description="领料出库单")
public class StockOutOrderPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
	private java.lang.Integer id;
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
	private java.util.Date createOutTime;
	/**物料类型*/
	@Excel(name = "物料类型", width = 15)
	@ApiModelProperty(value = "物料类型")
	private java.lang.String prdCategory;
	/**所属设施*/
	@Excel(name = "所属设施", width = 15)
	@ApiModelProperty(value = "所属设施")
	private java.lang.Integer belongEquip;
	/**申请班组*/
	@Excel(name = "申请班组", width = 15)
	@ApiModelProperty(value = "申请班组")
	private java.lang.String team;
	/**申请类型*/
	@Excel(name = "申请类型", width = 15)
	@ApiModelProperty(value = "申请类型")
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

	@ExcelCollection(name="出库明细表")
	@ApiModelProperty(value = "出库明细表")
	private List<StockOutOrderLine> stockOutOrderLineList;

}
