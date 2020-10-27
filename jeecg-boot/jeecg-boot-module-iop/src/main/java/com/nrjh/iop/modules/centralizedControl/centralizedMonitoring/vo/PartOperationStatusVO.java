package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/27 15:06
 */
@Data
public class PartOperationStatusVO implements Serializable {
    private static final long serialVersionUID = 6002643192139576625L;
    @ApiModelProperty("x轴时间")
    private String xTime;
    @ApiModelProperty("任务量")
    private Integer taskCount;
}
