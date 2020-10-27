package com.nrjh.iop.modules.alarm.alarmtype.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 修复方法
 * @Author: jeecg-boot
 * @Date:   2020-08-27
 * @Version: V1.0
 */
@ApiModel(value="fault_task_info对象", description="报警类型管理")
@Data
@TableName("fault_repair_info")
public class FaultRepairInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.UUID)
	@ApiModelProperty(value = "主键")
	private String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	@ApiModelProperty(value = "更新人")
	private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
	private Date updateTime;
	/**故障编号*/
	@ApiModelProperty(value = "故障编号")
	private String faultTaskId;
	/**显示顺序8*/
	@Excel(name = "显示顺序8", width = 15)
	@ApiModelProperty(value = "显示顺序")
	private Integer sequence;
	/**修复方法*/
	@Excel(name = "修复方法", width = 15)
	@ApiModelProperty(value = "修复方法")
	private String repairName;
	/**设备更换标识*/
	@Excel(name = "设备更换标识", width = 15)
	@ApiModelProperty(value = "设备更换标识")
	private String isReplace;
	/**说明*/
	@Excel(name = "说明", width = 15)
	@ApiModelProperty(value = "说明")
	private String remark;
	/**修复类型*/
	@Excel(name = "修复类型", width = 15)
	@ApiModelProperty(value = "修复类型")
	private String execMode;
	@TableLogic
	private Integer isDel;
}
