package com.nrjh.iop.modules.alarm.bind.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@TableName("O_EQUIP")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Equip {

    @TableId(type = IdType.ID_WORKER_STR)
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
    @TableLogic
    private Integer isDel;

    @Excel(name = "设备现场编号", width = 15)
    @ApiModelProperty(value = "设备类型")
    private String equipFno;
}
