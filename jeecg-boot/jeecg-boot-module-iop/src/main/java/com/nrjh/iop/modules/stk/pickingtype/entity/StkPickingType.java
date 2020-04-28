package com.nrjh.iop.modules.stk.pickingtype.entity;

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
 * @Description: 作业类型
 * @Author: jeecg-boot
 * @Date:   2020-04-28
 * @Version: V1.0
 */
@Data
@TableName("stk_picking_type")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="stk_picking_type对象", description="作业类型")
public class StkPickingType implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "标识")
    private Integer id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
	/**作业（出入库）类型*/
	@Excel(name = "作业（出入库）类型", width = 15)
    @ApiModelProperty(value = "作业（出入库）类型")
    private String code;
	/**颜色*/
	@Excel(name = "颜色", width = 15)
    @ApiModelProperty(value = "颜色")
    private java.lang.Integer color;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private java.lang.Integer sequence;
	/**参考序列   关联IR.SEQUENCE表主键。
            用于按照IR.SEQUENCE对应的规则生成单据编号（该表的NAME字段）。*/
	@Excel(name = "参考序列", width = 15)
    @ApiModelProperty(value = "参考序列")
    private java.lang.Integer sequenceId;
	/**代码*/
	@Excel(name = "代码", width = 15)
    @ApiModelProperty(value = "代码")
    private java.lang.String sequenceCode;
	/**默认源位置*/
	@Excel(name = "默认源位置", width = 15)
    @ApiModelProperty(value = "默认源位置")
    private java.lang.Integer defaultLoactionSrcId;
	/**默认目标位置*/
	@Excel(name = "默认目标位置", width = 15)
    @ApiModelProperty(value = "默认目标位置")
    private java.lang.Integer defaultLocationDestId;
	/**退回作业类型*/
	@Excel(name = "退回作业类型", width = 15)
    @ApiModelProperty(value = "退回作业类型")
    private java.lang.Integer returnPickingTypeId;
	/**整个包裹*/
	@Excel(name = "整个包裹", width = 15)
    @ApiModelProperty(value = "整个包裹")
    private java.lang.Integer showEntirePacks;
	/**仓库*/
	@Excel(name = "仓库", width = 15)
    @ApiModelProperty(value = "仓库")
    private java.lang.Integer warehouseId;
	/**启用*/
	@Excel(name = "启用", width = 15)
    @ApiModelProperty(value = "启用")
    private java.lang.Integer active;
	/**使用新建批号*/
	@Excel(name = "使用新建批号", width = 15)
    @ApiModelProperty(value = "使用新建批号")
    private java.lang.Integer useCreateLots;
	/**利用已存在批号*/
	@Excel(name = "利用已存在批号", width = 15)
    @ApiModelProperty(value = "利用已存在批号")
    private java.lang.Integer useExstingLots;
	/**详细作业*/
	@Excel(name = "详细作业", width = 15)
    @ApiModelProperty(value = "详细作业")
    private java.lang.Integer showOperations;
	/**被保留详细作业*/
	@Excel(name = "被保留详细作业", width = 15)
    @ApiModelProperty(value = "被保留详细作业")
    private java.lang.Integer showReserved;
	/**条码*/
	@Excel(name = "条码", width = 15)
    @ApiModelProperty(value = "条码")
    private java.lang.String barcode;
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
