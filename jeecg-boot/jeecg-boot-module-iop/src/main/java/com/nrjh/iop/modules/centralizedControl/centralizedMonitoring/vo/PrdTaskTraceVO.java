package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/25 10:20
 */
@Data
@ApiModel(value="生产任务跟踪VO", description="生产任务跟踪VO")
public class PrdTaskTraceVO implements Serializable {
    @ApiModelProperty(value = "计划数量")
    private Integer planNum;

    @ApiModelProperty(value = "计划时间")
//    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd 24hh")
//    @DateTimeFormat(pattern="yyyy-MM-dd 24hh")
    private String planTime;
    @ApiModelProperty(value = "实际数量")
    private Integer actNum;
    @ApiModelProperty(value = "实际时间")
    private String actTime;
}
