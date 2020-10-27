package com.nrjh.iop.modules.centralizedControl.RightScreen.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 * @author Kou Shenhai 2413176044@leimingtech.com
 * @version 1.0
 * @date 2020/9/16 0016 上午 9:52
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="在检信息视图", description="在检信息视图")
public class DetectTaskInfoVO implements Serializable {

    @ApiModelProperty(value = "任务单号")
    private String taskId;

    @ApiModelProperty(value = "批次")
    private String batchId;

    @ApiModelProperty(value = "设备型号")
    private String deviceModel;

    @ApiModelProperty(value = "供应商")
    private String supplier;

    @ApiModelProperty(value = "任务类型")
    private String taskType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "任务总量")
    private Integer totalTask;

    @ApiModelProperty(value = "当天完成量")
    @TableField(exist = false)
    private Integer amountCompleted;

    @ApiModelProperty(value = "累计完成量")
    @TableField(exist = false)
    private Integer cumulativeCompletion;

    @ApiModelProperty(value = "任务状态")
    private String taskStatus;

    @ApiModelProperty(value = "区域编号")
    private String areaId;

    @ApiModelProperty(value = "区域名称")
    private String areaName;

}
