package com.nrjh.iop.modules.stk.move.entity;

import java.io.Serializable;
import java.util.Date;
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

/**
 * @Description: 移库单
 * @Author: jeecg-boot
 * @Date:   2020-05-13
 * @Version: V1.0
 */
@ApiModel(value="stk_move对象", description="移库单")
@Data
@TableName("stk_move")
public class StkMove implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "标识")
    private Integer id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
	/**物品*/
	@Excel(name = "物品", width = 15)
    @DictIop(dictTable = "PRD_PRODUCT", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "物品")
    private String productId;
	/**物品计量单位*/
	@Excel(name = "物品计量单位", width = 15)
    @DictIop(dictTable = "UOM_UOM", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "物品计量单位")
    private Integer productUomId;
	/**批次/序列号*/
	@Excel(name = "批次/序列号", width = 15)
    @DictIop(dictTable = "stk_production_lot", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "批次/序列号")
    private Integer productLotId;
	/**实际物品数量*/
	@Excel(name = "实际物品数量", width = 15)
    @ApiModelProperty(value = "实际物品数量")
    private java.math.BigDecimal productQty;
	/**需求物品数量*/
	@Excel(name = "需求物品数量", width = 15)
    @ApiModelProperty(value = "需求物品数量")
    private java.math.BigDecimal productUomQty;
	/**状态*/
	@Excel(name = "状态", width = 15,dicCode = "IOP_STK_MOVE_STATE")
    @DictIop(dicCode = "IOP_STK_MOVE_STATE")
    @ApiModelProperty(value = "状态")
    private String state;
	/**源单据*/
	@Excel(name = "源单据", width = 15)
    @ApiModelProperty(value = "源单据")
    private String origin;
	/**作业类型*/
	@Excel(name = "作业类型", width = 15)
    @DictIop(dictTable = "stk_picking_type", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "作业类型")
    private Integer pickingTypeId;
	/**调拨单*/
	@Excel(name = "调拨单", width = 15)
    @ApiModelProperty(value = "调拨单")
    private Integer pickingId;
	/**盘点单*/
	@Excel(name = "盘点单", width = 15)
    @ApiModelProperty(value = "盘点单")
    private Integer inventoryId;
	/**是否退回移库*/
	@Excel(name = "是否退回移库", width = 15,dicCode = "yn")
    @DictIop(dicCode = "yn")
    @ApiModelProperty(value = "是否退回移库")
    private Integer toRefund;
	/**关联源移库单*/
	@Excel(name = "关联源移库单", width = 15)
    @ApiModelProperty(value = "关联源移库单")
    private Integer originReturnedMoveId;
	/**关联单据说明*/
	@Excel(name = "关联单据说明", width = 15)
    @ApiModelProperty(value = "关联单据说明")
    private String reference;
	/**仓库*/
	@Excel(name = "仓库", width = 15)
    @DictIop(dictTable = "stk_warehouse", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "仓库")
    private Integer warehouseId;
	/**源位置*/
	@Excel(name = "源位置", width = 15)
    @DictIop(dictTable = "stk_location", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "源位置")
    private Integer locationSrcId;
	/**目标位置*/
	@Excel(name = "目标位置", width = 15)
    @DictIop(dictTable = "stk_location", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "目标位置")
    private Integer locationDestId;
	/**实际执行时间*/
	@Excel(name = "实际执行时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际执行时间")
    private Date dateDone;
	/**预计执行时间*/
	@Excel(name = "预计执行时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预计执行时间")
    private Date dateExpected;
	/**说明*/
	@Excel(name = "说明", width = 15)
    @ApiModelProperty(value = "说明")
    private String note;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
    private Integer companyId;
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
}
