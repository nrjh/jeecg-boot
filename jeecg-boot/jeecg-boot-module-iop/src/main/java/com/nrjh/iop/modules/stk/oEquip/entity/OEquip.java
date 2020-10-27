package com.nrjh.iop.modules.stk.oEquip.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: o_equip
 * @Author: jeecg-boot
 * @Date:   2020-10-10
 * @Version: V1.0
 */
@Data
@TableName("o_equip")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="o_equip对象", description="o_equip")
public class OEquip implements Serializable {
    private static final long serialVersionUID = 1L;

	/**设备标识*/
	@Excel(name = "设备标识", width = 15)
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "设备标识")
    private String equipId;
	/**区域标识*/
	@Excel(name = "区域标识", width = 15)
    @ApiModelProperty(value = "区域标识")
    @DictIop(dictTable = "o_area", dicText = "area_name", dicCode = "area_id",dataSource = "ora")
    private String areaId;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String equipNo;
	/**设备类型*/
	@Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
//    @DictIop(dicCode = "P_COMM_CODE,param_name,param_id,SORT_ID = 'EQUIP_TYPE' AND STATUS = '1'")
    @DictIop(dictTable = "P_COMM_CODE", dicText = "param_name", dicCode = "param_id",dataSource = "ora")
    private java.lang.String equipType;
	@Excel(name = "硬件对应标识", width = 15)
    @ApiModelProperty(value = "硬件对应标识")
    private java.lang.String hwEquipId;
	@Excel(name = "硬件对应标识说", width = 15)
    @ApiModelProperty(value = "硬件对应标识说明")
    private java.lang.String hwEquipDesc;
	/**厂家*/
	@Excel(name = "厂家", width = 15)
    @ApiModelProperty(value = "厂家")
    private java.lang.String manufactoryId;
	/**排序
记录该区域在所属设备中的次序（用于点检顺序等业务）*/
	@Excel(name = "排序")
    private java.math.BigDecimal sortNo;
	/**购置日期*/
	@Excel(name = "购置日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "购置日期")
    private java.util.Date purchaseDate;
	/**是否可用*/
	@Excel(name = "是否可用", width = 15)
    @ApiModelProperty(value = "是否可用")
    private java.lang.String newFlag;
	/**操作时间*/
	@Excel(name = "操作时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "操作时间")
    private java.util.Date optDate;
	/**操作人*/
	@Excel(name = "操作人", width = 15)
    @ApiModelProperty(value = "操作人")
    private java.lang.String optCode;
	/**设备别名  用于标示设备通俗叫法，默认与设备名称一致，可单独修改*/
	@Excel(name = "设备别名  用于标示设备通俗叫法，默认与设备名称一致，可单独修改", width = 15)
    @ApiModelProperty(value = "设备别名  用于标示设备通俗叫法，默认与设备名称一致，可单独修改")
    private java.lang.String alias;
	/**设备现场编号 按照生产现场（或厂家）定义编号设置*/
	@Excel(name = "设备现场编号 按照生产现场（或厂家）定义编号设置", width = 15)
    @ApiModelProperty(value = "设备现场编号 按照生产现场（或厂家）定义编号设置")
    private java.lang.String equipFno;
	/**0是为未删除、1是已删除*/
	@Excel(name = "0是为未删除、1是已删除", width = 15)
    @ApiModelProperty(value = "0是为未删除、1是已删除")
    private java.math.BigDecimal isDel;

	@TableField(exist = false)
	private String QRStr;

	private String brandId;//品牌

    @TableField(exist = false)
    private String equipImgs;
    @TableField(exist = false)
    private String equipImgsId;
}
