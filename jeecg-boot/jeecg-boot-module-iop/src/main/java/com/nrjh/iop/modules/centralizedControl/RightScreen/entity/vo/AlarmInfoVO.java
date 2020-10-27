package com.nrjh.iop.modules.centralizedControl.RightScreen.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.DictIop;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 *
 * @author Kou Shenhai 2413176044@leimingtech.com
 * @version 1.0
 * @date 2020/9/17 0017 下午 4:54
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="报警信息视图", description="报警信息视图")
public class AlarmInfoVO implements Serializable {

    @ApiModelProperty(value = "报警时间")
    private Date alarmTime;

    @ApiModelProperty(value = "报警名称")
    private String alarmName;

    @ApiModelProperty(value = "检定线")
    @DictIop(dicCode = "o_area,area_name,area_id")
    private String areaId;

    @ApiModelProperty(value = "检定区域名称")
    private String areaName;

    @ApiModelProperty(value = "设备名称")
    private String equipName;

    @ApiModelProperty(value = "故障等级")
    private String failureLevel;

    @ApiModelProperty(value = "故障状态")
    private String faultStatus;

    @ApiModelProperty(value = "处理时长")
    @TableField(exist = false)
    private Integer handleTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

}
