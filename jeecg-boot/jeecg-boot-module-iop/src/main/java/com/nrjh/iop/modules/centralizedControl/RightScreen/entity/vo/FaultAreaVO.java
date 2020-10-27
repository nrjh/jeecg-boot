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
 * @date 2020/9/22 0022 下午 6:27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="故障区域占比视图", description="故障区域占比视图")
public class FaultAreaVO implements Serializable {

    @ApiModelProperty(value = "区域")
    private String equipType;
    @ApiModelProperty(value = "次数")
    private Integer num;
}