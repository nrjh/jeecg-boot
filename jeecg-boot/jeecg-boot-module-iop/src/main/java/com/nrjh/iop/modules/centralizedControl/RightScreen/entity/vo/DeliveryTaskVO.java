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
 *
 * @author Kou Shenhai 2413176044@leimingtech.com
 * @version 1.0
 * @date 2020/9/17 0017 下午 2:31
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="配送任务信息视图", description="配送任务信息视图")
public class DeliveryTaskVO implements Serializable {

    @ApiModelProperty(value = "车牌号")
    @TableField(exist = false)
    private Integer plateNum;

    @ApiModelProperty(value = "接收单位")
    private String acceptUnit;

    @ApiModelProperty(value = "设备数量")
    private Integer equipQty;

    @ApiModelProperty(value = "出发时间")
    private Date departureTime;

    @ApiModelProperty(value = "完成时间")
    private Date completeTime;

}
