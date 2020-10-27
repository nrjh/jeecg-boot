package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity;

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
 * @Description: EQUIP_DETECT_TASK_INFO
 * @Author: jeecg-boot
 * @Date:   2020-09-12
 * @Version: V1.0
 */
@Data
@TableName("equip_detect_task_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="equip_detect_task_info对象", description="EQUIP_DETECT_TASK_INFO")
public class EquipDetectTaskInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**检定任务编号*/
	@Excel(name = "检定任务编号", width = 15)
    @ApiModelProperty(value = "检定任务编号")
    private String detectTaskNo;
	/**0-未执行1-执行中2-执行完毕3-暂停4-中止*/
	@Excel(name = "0-未执行 1-执行中 2-执行完毕 3-暂停 4-中止", width = 15)
    @ApiModelProperty(value = "0-未执行 1-执行中 2-执行完毕 3-暂停 4-中止")
    private java.lang.String taskStatus;
	/**异常原因*/
	@Excel(name = "异常原因", width = 15)
    @ApiModelProperty(value = "异常原因")
    private java.lang.String reasonName;
	/**处理说明*/
	@Excel(name = "处理说明", width = 15)
    @ApiModelProperty(value = "处理说明")
    private java.lang.String remark;
	/**处理时间*/
	@Excel(name = "处理时间", width = 15)
    @ApiModelProperty(value = "处理时间")
    private java.lang.String handleDate;
	/**创建人ID*/
	@Excel(name = "创建人ID", width = 15)
    @ApiModelProperty(value = "创建人ID")
    private java.lang.String createId;
	/**创建人姓名*/
	@Excel(name = "创建人姓名", width = 15)
    @ApiModelProperty(value = "创建人姓名")
    private java.lang.String createName;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date 	createTime;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15)
    @ApiModelProperty(value = "创建时间")
    private java.lang.String updateBy;
	/**修改人名称*/
	@Excel(name = "修改人名称", width = 15)
    @ApiModelProperty(value = "修改人名称")
    private java.lang.String updateName;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    @TableLogic
    private Integer isDel;
}
