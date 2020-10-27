package com.nrjh.iop.modules.workOrder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 工单转派表
 * @Author: jeecg-boot
 * @Date:   2020-09-03
 * @Version: V1.0
 */
@Data
@TableName("ORD_REDEPLOY_INFO")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ord_redeploy对象", description="工单转派信息")
public class OrdRedeployInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**工单单号*/
	@Excel(name = "工单单号", width = 15)
    @ApiModelProperty(value = "工单单号")
    private String workOrderId;

	/**转派人员ID*/
	@Excel(name = "转派人员ID", width = 15)
    @ApiModelProperty(value = "转派人员ID")
    private String redepUserId;

    /**
     * 转派人员姓名
     */
    @Excel(name = "转派人员姓名", width = 15)
    @ApiModelProperty(value = "转派人员姓名")
    private String redepUser;

	/**转派时间*/
	@Excel(name = "转派时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "转派时间")
    private Date redepDate;

    /**
     * 转派说明
     */
    @Excel(name = "转派说明", width = 15)
    @ApiModelProperty(value = "转派说明")
    private String redepDesc;

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
