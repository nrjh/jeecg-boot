package com.nrjh.iop.modules.centralizedControl.RightScreen.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * TODO
 *
 * @author Kou Shenhai 2413176044@leimingtech.com
 * @version 1.0
 * @date 2020/9/18 0018 下午 3:06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="检定统计视图", description="检定统计视图")
public class DetectCountVO implements Serializable {

    @ApiModelProperty(value = "本月待检总数")
    private int pendMonthTotal;

    @ApiModelProperty(value = "本月完成检定数")
    private int completeVerificeMonthCount;

    @ApiModelProperty(value = "本月完成率")
    private int completeMonth;

    @ApiModelProperty(value = "今天完成率")
    private int completeDay;

    @ApiModelProperty(value = "今日待检总数")
    private int pendDayTotal;

    @ApiModelProperty(value = "今天完成检定数")
    private int completeVerificeDayCount;

    @ApiModelProperty(value = "单相计划数")
    private int planD;

    @ApiModelProperty(value = "单相完成数")
    private int completeD;

    @ApiModelProperty(value = "三相计划数")
    private int planS;

    @ApiModelProperty(value = "三相完成数")
    private int completeS;

    @ApiModelProperty(value = "采集终端计划数")
    private int planC;

    @ApiModelProperty(value = "采集终端完成数")
    private int completeC;

    @ApiModelProperty(value = "互感器计划数")
    private int planH;

    @ApiModelProperty(value = "互感器完成数")
    private int completeH;

    @ApiModelProperty(value = "单相完成率")
    private int completeRateD;

    @ApiModelProperty(value = "三相完成率")
    private int completeRateS;

    @ApiModelProperty(value = "互感器完成率")
    private int completeRateH;

    @ApiModelProperty(value = "采集终端完成率")
    private int completeRateC;

}
