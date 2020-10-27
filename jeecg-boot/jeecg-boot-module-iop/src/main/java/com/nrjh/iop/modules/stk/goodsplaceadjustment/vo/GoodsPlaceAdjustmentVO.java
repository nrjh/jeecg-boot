package com.nrjh.iop.modules.stk.goodsplaceadjustment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/10/9 14:55
 */
@Data
@ApiModel("货位调整VO")
public class GoodsPlaceAdjustmentVO implements Serializable {
    private static final long serialVersionUID = -7022268445736514413L;
    @ApiModelProperty("货位调整单号")
    private String orderMoveId;
    @ApiModelProperty("调整人员")
    private String createOutName;
    @ApiModelProperty(value = "调整日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createOutTime;
    @ApiModelProperty("调整物资列表")
    private String adjustmentProductListName;
    @ApiModelProperty("状态")
    private String state;
    @ApiModelProperty("备注")
    private String note;
}
