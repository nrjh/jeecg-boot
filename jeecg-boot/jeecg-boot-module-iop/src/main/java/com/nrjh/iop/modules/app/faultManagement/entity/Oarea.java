package com.nrjh.iop.modules.app.faultManagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-10-16 11:14
 */
@Data
@TableName("O_AREA")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="O_AREA对象", description="检定线")
public class Oarea {
    /**id*/
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String areaId;

    @Excel(name = "区域名称", width = 15)
    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @Excel(name = "区域编号", width = 15)
    @ApiModelProperty(value = "区域编号")
    private String areaNum;

    @Excel(name = "设备类别", width = 15)
    @ApiModelProperty(value = "设备类别")
    private String equipCateg;

    @Excel(name = "生产分区", width = 15)
    @ApiModelProperty(value = "生产分区")
    private String prodArea;

    @Excel(name = "是否可用", width = 15)
    @ApiModelProperty(value = "是否可用")
    private String newFlag;

    @Excel(name = "操作时间", width = 15)
    @ApiModelProperty(value = "操作时间")
    private Date optDate;

    @Excel(name = "操作人", width = 15)
    @ApiModelProperty(value = "操作人")
    private String optCode;


}
