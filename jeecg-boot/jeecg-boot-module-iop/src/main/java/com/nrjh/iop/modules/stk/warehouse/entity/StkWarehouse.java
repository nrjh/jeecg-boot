package com.nrjh.iop.modules.stk.warehouse.entity;

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
 * @Description: 库房
 * @Author: jeecg-boot
 * @Date:   2020-04-26
 * @Version: V1.0
 */
@Data
@TableName("stk_warehouse")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="stk_warehouse对象", description="库房")
public class StkWarehouse implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "标识")
    private Integer id;
	/**启用*/
	@Excel(name = "启用", width = 15,dicCode = "IOP_PUB_ACTION")
    @ApiModelProperty(value = "启用")
    @Dict(dicCode = "IOP_PUB_ACTION")
    private Integer active;
	/**代码*/
	@Excel(name = "代码", width = 15)
    @ApiModelProperty(value = "代码")
    private String code;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
	/**合作伙伴*/
	@Excel(name = "合作伙伴", width = 15)
    @ApiModelProperty(value = "合作伙伴")
    private Integer partnerId;
	/**视图位置*/
	@Excel(name = "视图位置", width = 15)
    @ApiModelProperty(value = "视图位置")
    private Integer viewLocationId;
	/**库存位置*/
	@Excel(name = "库存位置", width = 15)
    @ApiModelProperty(value = "库存位置")
    private Integer stockLocationId;
	/**入库步骤*/
	@Excel(name = "入库步骤", width = 15)
    @ApiModelProperty(value = "入库步骤")
    private Integer receptionSteps;
	/**出库步骤 */
	@Excel(name = "出库步骤 ", width = 15)
    @ApiModelProperty(value = "出库步骤 ")
    private Integer deliverySteps;
	/**入库区库存位置*/
	@Excel(name = "入库区库存位置", width = 15)
    @ApiModelProperty(value = "入库区库存位置")
    private Integer whInputStockLotId;
	/**质检区库存位置*/
	@Excel(name = "质检区库存位置", width = 15)
    @ApiModelProperty(value = "质检区库存位置")
    private Integer whQcStockLocId;
	/**出库区库存位置*/
	@Excel(name = "出库区库存位置", width = 15)
    @ApiModelProperty(value = "出库区库存位置")
    private Integer whOutputStockLocId;
	/**打包区库存位置*/
	@Excel(name = "打包区库存位置", width = 15)
    @ApiModelProperty(value = "打包区库存位置")
    private Integer whPackStockLocId;
	/**分拣作业类型*/
	@Excel(name = "分拣作业类型", width = 15)
    @ApiModelProperty(value = "分拣作业类型")
    private Integer pickTypeId;
	/**打包作业类型*/
	@Excel(name = "打包作业类型", width = 15)
    @ApiModelProperty(value = "打包作业类型")
    private Integer packTypeId;
	/**入库作业类型*/
	@Excel(name = "入库作业类型", width = 15)
    @ApiModelProperty(value = "入库作业类型")
    private Integer inTypeId;
	/**出库作业类型*/
	@Excel(name = "出库作业类型", width = 15)
    @ApiModelProperty(value = "出库作业类型")
    private Integer outTypeId;
	/**内部调拨作业类型*/
	@Excel(name = "内部调拨作业类型", width = 15)
    @ApiModelProperty(value = "内部调拨作业类型")
    private Integer intTypeId;
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

    /**地址*/
    @Excel(name = "地址", width = 255)
    @ApiModelProperty(value = "地址")
	private String address;
}
