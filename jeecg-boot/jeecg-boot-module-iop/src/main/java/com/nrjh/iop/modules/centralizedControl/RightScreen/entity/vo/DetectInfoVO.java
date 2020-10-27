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
@ApiModel(value="在检状态视图", description="在检状态视图")
public class DetectInfoVO implements Serializable {

    @ApiModelProperty(value = "任务状态")
    private String taskStatus;

    @ApiModelProperty(value = "状态")
    private Integer status;

}
