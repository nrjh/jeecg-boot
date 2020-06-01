package com.nrjh.iop.modules.stk.move.entity;

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
 * @Description: 库存移动明细
 * @Author: jeecg-boot
 * @Date:   2020-05-13
 * @Version: V1.0
 */
@ApiModel(value="stk_move对象", description="移库单")
@Data
@TableName("stk_move_line")
public class StkMoveLine implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "标识")
	private Integer id;
	/**调拨单*/
	@Excel(name = "调拨单", width = 15)
	@ApiModelProperty(value = "调拨单")
	private Integer pickingId;
	/**移库单*/
	@ApiModelProperty(value = "移库单")
	private Integer moveId;
	/**物品*/
	@Excel(name = "物品", width = 15)
	@ApiModelProperty(value = "物品")
	private String productId;
	/**物品计量单位*/
	@Excel(name = "物品计量单位", width = 15)
	@ApiModelProperty(value = "物品计量单位")
	private Integer productUomId;
	/**实际物品数量*/
	@Excel(name = "实际物品数量", width = 15)
	@ApiModelProperty(value = "实际物品数量")
	private java.math.BigDecimal productQty;
	/**已保留物品数量*/
	@Excel(name = "已保留物品数量", width = 15)
	@ApiModelProperty(value = "已保留物品数量")
	private java.math.BigDecimal productUomQty;
	/**完成物品数量*/
	@Excel(name = "完成物品数量", width = 15)
	@ApiModelProperty(value = "完成物品数量")
	private java.math.BigDecimal productDoneQty;
	/**批次/序列号*/
	@Excel(name = "批次/序列号", width = 15)
	@ApiModelProperty(value = "批次/序列号")
	private Integer productLotId;
	/**批次/序列名称*/
	@Excel(name = "批次/序列名称", width = 15)
	@ApiModelProperty(value = "批次/序列名称")
	private String productLotName;
	/**实际执行时间*/
	@Excel(name = "实际执行时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "实际执行时间")
	private Date dateDone;
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
	private String state;
	/**关联单据说明*/
	@Excel(name = "关联单据说明", width = 15)
	@ApiModelProperty(value = "关联单据说明")
	private String reference;
	/**源位置*/
	@Excel(name = "源位置", width = 15)
	@ApiModelProperty(value = "源位置")
	private Integer locationSrcId;
	/**目标位置*/
	@Excel(name = "目标位置", width = 15)
	@ApiModelProperty(value = "目标位置")
	private Integer locationDestId;
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
