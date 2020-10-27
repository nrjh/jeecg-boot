package com.nrjh.iop.modules.stk.parts.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.DictIop;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author Kou Shenhai 2413176044@leimingtech.com
 * @version 1.0t
 * @date 2020/10/9 0009 下午 6:47
 */
@ApiModel(value="部件视图", description="部件视图")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EquipPartsVO implements Serializable {

    @ApiModelProperty(value = "设备类别名称")
    private String equipCategName;

    @ApiModelProperty(value = "设备类别编号")
    private String equipCategId;

    @ApiModelProperty(value = "所属区域名称")
    private String areaName;

    @ApiModelProperty(value = "所属区域编号")
    private String areaId;

    @ApiModelProperty(value = "设备名称")
    private String equipName;

    @ApiModelProperty(value = "设备编号")
    private String equipId;

    @ApiModelProperty(value = "部件名称")
    private String partsName;

    @ApiModelProperty(value = "部件编号")
    private String partsNo;

    @ApiModelProperty(value = "部件标识")
    private String partsId;

    @ApiModelProperty(value = "部件类别 01 物理部件 02 虚拟部件")
    @DictIop(dicCode = "Parts_Sort")
    private String partsSort;

/*    @ApiModelProperty(value = "部件类型")
    @DictIop(dicCode = "Parts_Type")
    private String partsType;*/

    @ApiModelProperty(value = "部件类型 ")
    private List<StkSparePartsVO> StkSparePartsList;

}
