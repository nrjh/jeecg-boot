package com.nrjh.iop.modules.stk.in.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: order_stock_in_list
 * @Author: jeecg-boot
 * @Date:   2020-08-18
 * @Version: V1.0
 */
@Data
@TableName("order_stock_in_list")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="order_stock_in_list对象", description="order_stock_in_list")
public class OrderStockInList implements Serializable {
    private static final long serialVersionUID = 1L;

	/**出库表ID*/
	@Excel(name = "出库表ID", width = 15)
    @ApiModelProperty(value = "出库表ID")
    private Integer stockOutOrderId;
	/**入库表ID*/
	@Excel(name = "入库表ID", width = 15)
    @ApiModelProperty(value = "入库表ID")
    private Integer stockInOrderId;
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**明细表ID*/
	@Excel(name = "明细表ID", width = 15)
    @ApiModelProperty(value = "明细表ID")
    private String stockOrderId;
	/**操作类型*/
	@Excel(name = "操作类型", width = 15)
    @ApiModelProperty(value = "操作类型")
    private String operType;
	/**位置id*/
	@Excel(name = "位置id", width = 15)
    @ApiModelProperty(value = "位置id")
    private String whInputStockLotId;
	/**位置名*/
	@Excel(name = "位置名", width = 15)
    @ApiModelProperty(value = "位置名")
    private String whInputStockLotName;
	/**需求数量*/
	@Excel(name = "需求数量", width = 15)
    @ApiModelProperty(value = "需求数量")
    private BigDecimal productActualQty;
	/**状态
     */
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.String status;
	@TableLogic
    private Integer isDel;
}
