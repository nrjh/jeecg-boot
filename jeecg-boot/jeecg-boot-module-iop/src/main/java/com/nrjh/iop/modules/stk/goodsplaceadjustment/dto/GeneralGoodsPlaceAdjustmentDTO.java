package com.nrjh.iop.modules.stk.goodsplaceadjustment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nrjh.iop.modules.stk.goodsplaceadjustment.entity.StockMoveOrderLine;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 新增、查看、编辑DTO
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/10/9 17:09
 */
@Data
@ApiModel("通用货位调整DTO，用于新增、查看、编辑DTO")
public class GeneralGoodsPlaceAdjustmentDTO  implements Serializable {
    private static final long serialVersionUID = -1834641109262210032L;
    @ApiModelProperty("货位调整单号")
    private String orderMoveId;
    @ApiModelProperty("调整人")
    private String createOutName;
    @ApiModelProperty("调整日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createOutTime;
    @ApiModelProperty("备注")
    private String note;
    @ApiModelProperty("状态")
    private String state;
    @ApiModelProperty("添加物料列表")
    private List<StockMoveOrderLine> stockMoveOrderLineList;
}
