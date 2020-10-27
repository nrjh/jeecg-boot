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
 * @date 2020/9/17 0017 下午 4:16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="通知公告视图", description="通知公告视图")
public class AnnouncementVO implements Serializable {

    @ApiModelProperty(value = "公告名称")
    private String announcementName;

    @ApiModelProperty(value = "发布时间")
    private Date releaseTime;

    @ApiModelProperty(value = "管控人")
    private String controller;

}
