package com.nrjh.iop.modules.workOrder.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @Description: 工单挂单表
 * @Author: lei-li
 * @Date:   2020-09-03
 * @Version: V1.0
 */
@Data
@TableName("ord_suspend")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ord_suspend对象", description="工单挂单表")
public class OrdSuspend implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**工单单号*/
	@Excel(name = "工单单号", width = 15)
    @ApiModelProperty(value = "工单单号")
    private String workOrderId;
	/**挂起状态*/
	@Excel(name = "挂起状态", width = 15)
    @ApiModelProperty(value = "挂起状态")
    private String suspendStauts;
	/**挂起开始时间*/
	@Excel(name = "挂起开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "挂起开始时间")
    private Date suspendBeginTime;
	/**挂起结束时间*/
	@Excel(name = "挂起结束时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "挂起结束时间")
    private Date suspendEndTime;
	/**挂起说明*/
	@Excel(name = "挂起说明", width = 15)
    @ApiModelProperty(value = "挂起说明")
    private String suspendReason;
	/**解除说明*/
	@Excel(name = "解除说明", width = 15)
    @ApiModelProperty(value = "解除说明")
    private String resumeReason;
	/**解除挂起单号*/
	@Excel(name = "解除挂起单号", width = 15)
    @ApiModelProperty(value = "解除挂起单号")
    private String suspendOperId;
	/**创建人ID*/
	@Excel(name = "创建人ID", width = 15)
    @ApiModelProperty(value = "创建人ID")
    private String createId;
	/**创建人姓名*/
	@Excel(name = "创建人姓名", width = 15)
    @ApiModelProperty(value = "创建人姓名")
    private String createName;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改人名称*/
	@Excel(name = "修改人名称", width = 15)
    @ApiModelProperty(value = "修改人名称")
    private String updateName;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
