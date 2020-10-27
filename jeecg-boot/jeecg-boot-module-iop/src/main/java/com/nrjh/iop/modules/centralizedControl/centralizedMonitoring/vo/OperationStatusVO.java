package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/26 19:27
 */
@ApiModel("运行情况统计VO")
@Data
public class OperationStatusVO implements Serializable {
    private static final long serialVersionUID = -3028249706246578052L;
    @ApiModelProperty("运行时长")
    private String actionTime;
    @ApiModelProperty("x轴时间")
    private String xTime;
    @ApiModelProperty("检定量")
    private Integer checkCount;
    @ApiModelProperty("任务量")
    private Integer taskCount;
}
