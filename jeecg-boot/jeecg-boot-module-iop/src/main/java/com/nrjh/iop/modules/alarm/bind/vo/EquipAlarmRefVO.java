package com.nrjh.iop.modules.alarm.bind.vo;

import com.nrjh.iop.modules.alarm.bind.entity.FaultPrdProductInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EquipAlarmRefVO {

    @ApiModelProperty(value = "设备标识")
    private String equipId;

    @Excel(name = "区域标识", width = 15)
    @ApiModelProperty(value = "区域标识")
    private String areaId;

    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;

    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String equipNo;

    @Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
    private String equipType;

    @ApiModelProperty(value = "设备绑定告警信息")
    List<FaultPrdProductInfoVO> faultPrdProductInfos;
}
