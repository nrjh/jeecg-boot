package com.nrjh.iop.modules.centralizedControl.RightScreen.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 *
 * @author Kou Shenhai 2413176044@leimingtech.com
 * @version 1.0
 * @date 2020/10/22 0022 下午 8:50
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="出入库设备视图", description="出入库设备视图")
public class AccessEquipVO implements Serializable {

    @ApiModelProperty(value = "任务编号")
    private String taskId;

    @ApiModelProperty("批次号")
    private String batchId;

    @ApiModelProperty("设备类别")
    private String equipCateg;

    @ApiModelProperty("箱条码")
    private String boxBarCode;

    @ApiModelProperty("垛号")
    private String pileNo;

    @ApiModelProperty("托盘号")
    private String palletNo;

    @ApiModelProperty("系统编号")
    private String sysNo;

    @ApiModelProperty("写入时间")
    private Date writeDate;

    @ApiModelProperty("出入库分类编码")
    private String sortCode;

    @ApiModelProperty(value = "系统名称")
    private String sysName;

    @ApiModelProperty("检定线编号")
    private String areaId;

}
