package com.nrjh.iop.modules.centralizedControl.RightScreen.entity.model;

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
 * @date 2020/9/23 0023 下午 2:34
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="柱状图模型", description="柱状图模型")
public class BarModel implements Serializable {

    @ApiModelProperty(value = "名字")
    private String name;
    @ApiModelProperty(value = "类型")
    private String type = "bar";
    @ApiModelProperty(value = "数据")
    private Integer[] data;

}
