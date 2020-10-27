package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/11 18:26
 */
@Data
public class ExceptionCheckTaskVO implements Serializable {
    private static final long serialVersionUID = -4642666549396270887L;
    @ApiModelProperty(value = "任务号")
    private String detectTaskNo;
    @ApiModelProperty(value = "线体名称")
    private String lineNo;
    @ApiModelProperty(value = "任务类别")
    private String taskType;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "启动时间")
    private Date taskBgnDate;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "完成时间")
    private Date taskEndDate;
    @ApiModelProperty(value = "审核结果")
    private String auditRemark;
    @ApiModelProperty(value = "已检定量")
    private Integer hadCheckNum;
    @ApiModelProperty(value = "合格量")
    private Integer detectConcP;
    @ApiModelProperty(value = "不合格量")
    private Integer detectConcU;

}
