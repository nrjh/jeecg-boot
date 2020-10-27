package com.nrjh.iop.modules.alarm.alarmtype.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultReasonInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultRepairInfo;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultSopInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 报警类型管理
 * @Author: jeecg-boot
 * @Date:   2020-08-27
 * @Version: V1.0
 */
@Data
@ApiModel(value="fault_task_infoPage对象", description="报警类型管理")
public class FaultTaskInfoPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
	private String id;
	/**创建人id*/
	@Excel(name = "创建人id", width = 15)
	@ApiModelProperty(value = "创建人id")
	private String createId;
	/**创建人名字*/
	@Excel(name = "创建人名字", width = 15)
	@ApiModelProperty(value = "创建人名字")
	private String createName;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private Date createTime;
	/**更新人id*/
	@Excel(name = "更新人id", width = 15)
	@ApiModelProperty(value = "更新人id")
	private String updateBy;
	/**更新人名字*/
	@Excel(name = "更新人名字", width = 15)
	@ApiModelProperty(value = "更新人名字")
	private String updateName;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
	private Date updateTime;
	/**故障编号*/
	@Excel(name = "故障编号", width = 15)
	@ApiModelProperty(value = "故障编号")
	private String faultTaskNo;
	/**报警名称*/
	@Excel(name = "报警名称", width = 15)
	@ApiModelProperty(value = "报警名称")
	private String faultName;
	/**异常设备分类*/
	@Excel(name = "异常设备分类", width = 15)
	@ApiModelProperty(value = "异常设备分类")
	private String faultType;
	/**故障运维班组*/
	@Excel(name = "故障运维班组", width = 15)
	@ApiModelProperty(value = "故障运维班组")
	private String lineTeam;
	/**故障类型*/
	@Excel(name = "故障类型", width = 15)
	@ApiModelProperty(value = "故障类型")
	private String faultLabe;
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
	private String isStatus;
	@Excel(name = "设备id", width = 15)
	@ApiModelProperty(value = "设备id")
	private String equipId;

	@ApiModelProperty(value = "系统名称")
	private String 	systemName;

	private String faultCode;

	private String equipNo;

	@ExcelCollection(name="修复方法")
	@ApiModelProperty(value = "修复方法")
	private List<FaultRepairInfo> faultRepairInfoList;
	@ExcelCollection(name="作业指导")
	@ApiModelProperty(value = "作业指导")
	private List<FaultSopInfo> faultSopInfoList;

	@ExcelCollection(name="故障原因")
	@ApiModelProperty(value = "故障原因")
	private List<FaultReasonInfo> faultReasonInfoList;

}
