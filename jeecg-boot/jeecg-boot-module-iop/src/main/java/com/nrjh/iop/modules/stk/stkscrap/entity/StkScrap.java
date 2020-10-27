package com.nrjh.iop.modules.stk.stkscrap.entity;

import java.io.Serializable;
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
 * @Description: 报废管理
 * @Author: jeecg-boot
 * @Date:   2020-08-06
 * @Version: V1.0
 */
@Data
@TableName("stk_scrap")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="stk_scrap对象", description="报废管理")
public class StkScrap implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**确认人*/
	@Excel(name = "确认人", width = 15)
	@ApiModelProperty(value = "确认人")
	private String sureBy;
	/**确认日期*/
	@Excel(name = "确认日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "确认日期")
	private Date sureTime;
	/**审核人*/
	@Excel(name = "审核人", width = 15)
	@ApiModelProperty(value = "审核人")
	private String shenheBy;
	/**审核日期*/
	@Excel(name = "审核日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "审核日期")
	private Date shenheTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**报废盘点单号*/
	@Excel(name = "报废盘点单号", width = 15)
    @ApiModelProperty(value = "报废盘点单号")
    private String scrapInventoryOrder;
	/**类别*/
	@Excel(name = "类别", width = 15, dicCode = "scrap_type")
	@Dict(dicCode = "scrap_type")
    @ApiModelProperty(value = "类别")
    private String type;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称")
    private String materialName;
	/**物料编号*/
	@Excel(name = "物料编号", width = 15)
    @ApiModelProperty(value = "物料编号")
    private String materialCode;
	/**入库类型*/
	@Excel(name = "入库类型", width = 15)
    @ApiModelProperty(value = "入库类型")
    private String stockInType;
	/**申请人*/
	@Excel(name = "申请人", width = 15)
    @ApiModelProperty(value = "申请人")
    private String applicationWorker;
	/**申请日期*/
	@Excel(name = "申请日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "申请日期")
    private Date applicationDate;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "scrap_status")
	@Dict(dicCode = "scrap_status")
    @ApiModelProperty(value = "状态")
    private String status;
	/**报废/盘点详情*/
	@Excel(name = "报废/盘点详情", width = 15)
    @ApiModelProperty(value = "报废/盘点详情")
    private String scrapDetail;
	/**审批信息*/
	@Excel(name = "审批信息", width = 15)
    @ApiModelProperty(value = "审批信息")
    private String checkInfo;
	/**申请说明*/
	@Excel(name = "申请说明", width = 15)
    @ApiModelProperty(value = "申请说明")
    private String applicationAbout;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    private String spec;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private String productUom;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private String supplier;
	/**库存状况*/
	@Excel(name = "库存状况", width = 15)
    @ApiModelProperty(value = "库存状况")
    private String location;
	/**库存件数*/
	@Excel(name = "库存件数", width = 15)
    @ApiModelProperty(value = "库存件数")
    private Integer productQty;
	/**报废件数*/
	@Excel(name = "报废件数", width = 15)
    @ApiModelProperty(value = "报废件数")
    private Integer scrapQty;
	/**金额*/
	@Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额")
    private BigDecimal price;
}
