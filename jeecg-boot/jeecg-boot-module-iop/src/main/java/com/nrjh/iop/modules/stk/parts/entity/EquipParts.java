package com.nrjh.iop.modules.stk.parts.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: O_EQUIP_PARTS
 * @Author: jeecg-boot
 * @Date:   2020-09-30
 * @Version: V1.0
 */
@Data
@TableName("o_equip_parts")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="o_equip_parts对象", description="O_EQUIP_PARTS")
public class EquipParts implements Serializable {
    private static final long serialVersionUID = 1L;

	/**部件标识*/
    @TableId(type = IdType.UUID)
	@Excel(name = "部件标识", width = 15)
    @ApiModelProperty(value = "部件标识")
    private java.lang.String partsId;
	/**设备标识*/
	@Excel(name = "设备标识", width = 15)
    @ApiModelProperty(value = "设备标识")
    private java.lang.String equipId;
	/**父部件标识*/
	@Excel(name = "父部件标识", width = 15)
    @ApiModelProperty(value = "父部件标识")
    private java.lang.String partsPid;
	/**部件名称*/
	@Excel(name = "部件名称", width = 15)
    @ApiModelProperty(value = "部件名称")
    private java.lang.String partsName;
	/**部件编号*/
	@Excel(name = "部件编号", width = 15)
    @ApiModelProperty(value = "部件编号")
    private java.lang.String partsNo;
	/**部件类别01 物理部件02 虚拟部件*/
	@Excel(name = "部件类别 01 物理部件 02 虚拟部件", width = 15)
    @ApiModelProperty(value = "部件类别 01 物理部件 02 虚拟部件")
    private java.lang.String partsSort;
	/**部件类型*/
	@Excel(name = "部件类型", width = 15)
    @ApiModelProperty(value = "部件类型")
    private java.lang.String partsType;
	/**排序记录该区域在所属设备中的次序（用于点检顺序等业务）*/
	@Excel(name = "排序 记录该区域在所属设备中的次序（用于点检顺序等业务）", width = 15)
    @ApiModelProperty(value = "排序 记录该区域在所属设备中的次序（用于点检顺序等业务）")
    private java.lang.Integer sortNo;
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
	/**部件别名  用于标示部件通俗叫法，默认与部件名称一致，可单独修改*/
	@Excel(name = "部件别名  用于标示部件通俗叫法，默认与部件名称一致，可单独修改", width = 15)
    @ApiModelProperty(value = "部件别名  用于标示部件通俗叫法，默认与部件名称一致，可单独修改")
    private java.lang.String alias;
	/**部件现场编号  按照生产现场（或厂家）定义编号设置*/
	@Excel(name = "部件现场编号  按照生产现场（或厂家）定义编号设置", width = 15)
    @ApiModelProperty(value = "部件现场编号  按照生产现场（或厂家）定义编号设置")
    private java.lang.String partsFno;

}
