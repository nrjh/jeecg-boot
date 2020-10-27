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
 * @Description: 工单处理明细
 * @Author: jeecg-boot
 * @Date:   2020-09-08
 * @Version: V1.0
 */
@Data
@TableName("ord_process_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ord_process_info对象", description="工单处理明细")
public class OrdProcessInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**工单单号*/
	@Excel(name = "工单单号", width = 15)
    @ApiModelProperty(value = "工单单号")
    private String workOrderId;
	/**修复类型*/
	@Excel(name = "修复类型", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修复类型")
    private String execMode;
	/**修复方法名称*/
	@Excel(name = "修复方法名称", width = 15)
    @ApiModelProperty(value = "修复方法名称")
    private String repairName;
	/**报警级别*/
	@Excel(name = "报警级别", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "报警级别")
    private String alarmLevel;
	/**领料单号*/
	@Excel(name = "领料单号", width = 15)
    @ApiModelProperty(value = "领料单号")
    private String orderOutId;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String reason;
	/**创建人ID*/
	@Excel(name = "创建人ID", width = 15)
    @ApiModelProperty(value = "创建人ID")
    private String createId;
	/**创建人姓名*/
	@Excel(name = "创建人姓名", width = 15)
    @ApiModelProperty(value = "创建人姓名")
    private String createName;
	/**工单处理时*/
	@Excel(name = "工单处理时", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "工单处理时")
    private Date createTime;
	/**处理结果*/
	@Excel(name = "处理结果", width = 15)
    @ApiModelProperty(value = "处理结果")
    private String result;
    /**故障原因ID*/
    @Excel(name = "故障原因ID", width = 15)
    @ApiModelProperty(value = "故障原因ID")
    private String ReasonInfoId;
    /**故障原因名称*/
    @Excel(name = "故障原因名称", width = 15)
    @ApiModelProperty(value = "故障原因名称")
    private String ReasonName;
    /**修复方法ID*/
    @Excel(name = "修复方法ID", width = 15)
    @ApiModelProperty(value = "修复方法ID")
    private String repairInfoId;

    /**数据来源*/
    @Excel(name = "数据来源", width = 15)
    @ApiModelProperty(value = "数据来源")
    private String dataFrom;
}
