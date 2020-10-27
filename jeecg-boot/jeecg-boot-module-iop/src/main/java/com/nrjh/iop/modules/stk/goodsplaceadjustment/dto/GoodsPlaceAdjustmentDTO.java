package com.nrjh.iop.modules.stk.goodsplaceadjustment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/10/9 14:59
 */
@Data
@ApiModel("货位调整DTO")
public class GoodsPlaceAdjustmentDTO implements Serializable {
    private static final long serialVersionUID = -7030845252163699206L;
    @ApiModelProperty("货位调整单号")
    private String orderMoveId;
    @ApiModelProperty("调整人")
    private String createOutName;
    @ApiModelProperty("物料名称")
    private String 	productName;
    @ApiModelProperty("物料编号")
    private Integer productId;
    @ApiModelProperty("调整开始时间")
    @DateTimeFormat(pattern="YYYY-MM-dd")
    private Date startAdjustTime;
    @ApiModelProperty("调整结束时间")
    @DateTimeFormat(pattern="YYYY-MM-dd")
    private Date endAdjustTime;
    @ApiModelProperty("状态")
    private java.lang.String state;
}
