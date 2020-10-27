package com.nrjh.iop.modules.alarm.alarmtype.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: 报警类型管理
 * @Author: jeecg-boot
 * @Date:   2020-08-25
 * @Version: V1.0
 */
@Data
@TableName("fault_task_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="fault_task_info对象", description="报警类型管理")
public class FaultTaskInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.UUID)
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
    @Excel(name = "转义代码", width = 15)
    @ApiModelProperty(value = "转义代码")
    private String 	faultCode;

    @ApiModelProperty(value = "系统名称")
    private String 	systemName;

    //设备编号
    private String 	equipNo;
    @TableLogic
    private Integer isDel;
}
