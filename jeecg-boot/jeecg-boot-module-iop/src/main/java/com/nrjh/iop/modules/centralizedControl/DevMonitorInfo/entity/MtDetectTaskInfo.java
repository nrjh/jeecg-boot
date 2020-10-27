package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity;

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
 * @Description: 检定任务信息(新平台)实体
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Data
@TableName("MIDHD.mt_detect_task_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="mt_detect_task_info对象", description="检定任务信息(新平台)实体")
public class MtDetectTaskInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键,IOP在中间库创建对应序列*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键,IOP在中间库创建对应序列")
    private Integer id;
	/**检定任务编号*/
	@Excel(name = "检定任务编号", width = 15)
    @ApiModelProperty(value = "检定任务编号")
    private String detectTaskNo;
	/**任务优先级*/
	@Excel(name = "任务优先级", width = 15)
    @ApiModelProperty(value = "任务优先级")
    private Integer taskPrio;
	/**检定方式：01 自动  02  人工*/
	@Excel(name = "检定方式：01 自动  02  人工", width = 15)
    @ApiModelProperty(value = "检定方式：01 自动  02  人工")
    private String detectMode;
	/**线体编号*/
	@Excel(name = "线体编号", width = 15)
    @ApiModelProperty(value = "线体编号")
    private String lineNo;
	/**到货的批次号*/
	@Excel(name = "到货的批次号", width = 15)
    @ApiModelProperty(value = "到货的批次号")
    private String arriveBatchNo;
	/**关联代码分类的设备类别实体记录，单相电能表、三相电能表、互感器、采集终端*/
	@Excel(name = "关联代码分类的设备类别实体记录，单相电能表、三相电能表、互感器、采集终端", width = 15)
    @ApiModelProperty(value = "关联代码分类的设备类别实体记录，单相电能表、三相电能表、互感器、采集终端")
    private String equipCateg;
	/**型号*/
	@Excel(name = "型号", width = 15)
    @ApiModelProperty(value = "型号")
    private String modelCode;
	/**复检数量*/
	@Excel(name = "复检数量", width = 15)
    @ApiModelProperty(value = "复检数量")
    private Integer redetectQty;
	/**设备数量*/
	@Excel(name = "设备数量", width = 15)
    @ApiModelProperty(value = "设备数量")
    private Integer equipQty;
	/**总垛数*/
	@Excel(name = "总垛数", width = 15)
    @ApiModelProperty(value = "总垛数")
    private Integer pileQty;
	/**任务状态：0-未执行；1-执行中；2-执行完毕；3-暂停；4-中止*/
	@Excel(name = "任务状态：0-未执行；1-执行中；2-执行完毕；3-暂停；4-中止", width = 15)
    @ApiModelProperty(value = "任务状态：0-未执行；1-执行中；2-执行完毕；3-暂停；4-中止")
    private String taskStatus;
	/**处理时间*/
	@Excel(name = "处理时间", width = 15)
    @ApiModelProperty(value = "处理时间")
    private String handleDate;
	/**处理标记:0-未处理（默认）；1-处理中；2-已处理*/
	@Excel(name = "处理标记:0-未处理（默认）；1-处理中；2-已处理", width = 15)
    @ApiModelProperty(value = "处理标记:0-未处理（默认）；1-处理中；2-已处理")
    private String handleFlag;
	/**平台写入时间*/
	@Excel(name = "平台写入时间", width = 15)
    @ApiModelProperty(value = "平台写入时间")
    private String writeDate;
	/**任务类型*/
	@Excel(name = "任务类型", width = 15)
    @ApiModelProperty(value = "任务类型")
    private String taskType;
	/**本次检定活动开始时间*/
	@Excel(name = "本次检定活动开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "本次检定活动开始时间")
    private Date detectStartDate;
	/**本次检定活动结束（完成）时间*/
	@Excel(name = "本次检定活动结束（完成）时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "本次检定活动结束（完成）时间")
    private Date detectEndDate;
	/**操作标志（0:未读取，1：已读取）*/
	@Excel(name = "操作标志（0:未读取，1：已读取）", width = 15)
    @ApiModelProperty(value = "操作标志（0:未读取，1：已读取）")
    private String opFlag;
	/**数据写入的时间，精确到秒*/
	@Excel(name = "数据写入的时间，精确到秒", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "数据写入的时间，精确到秒")
    private Date writeTime;
    @TableLogic
    private Integer isDel;
}
