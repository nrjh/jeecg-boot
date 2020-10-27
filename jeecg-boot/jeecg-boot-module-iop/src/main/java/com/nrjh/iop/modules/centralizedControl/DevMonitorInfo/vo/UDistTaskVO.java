package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: U_DIST_TASK
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Data
@ApiModel(value="配送任务监控列表VO", description="配送任务监控列表VO")
public class UDistTaskVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**派车信息标识*/
    @ApiModelProperty(value = "车牌号")
    private Integer autoInfo;

    /**接收单位，接收库房所属单位*/
    @ApiModelProperty(value = "接收单位，接收库房所属单位")
    private String orgNo;

    /**配送任务数量*/
    @ApiModelProperty(value = "配送任务数量")
    private Integer distTaskNum;

    @ApiModelProperty(value = "出发时间")
    private Date madeTime;

    @ApiModelProperty(value = "配送签收时间")
    private Date signDate;

}
