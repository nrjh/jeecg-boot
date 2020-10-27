package com.nrjh.iop.modules.app.faultManagement.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-10-16 09:54
 */
@Data
public class EquipAndAreaVo {


    @ApiModelProperty(value = "设备名称")
    private String equipName;

    @ApiModelProperty(value = "设备编号")
    private String equipNo;

    @ApiModelProperty(value = "设备id")
    private String equipId;

    @ApiModelProperty(value = "名称")
    private String lineName;

    @ApiModelProperty(value = "检定线Id")
    private String lineId;

    @ApiModelProperty(value = "设备类别")
    private String equipCateg;


}
