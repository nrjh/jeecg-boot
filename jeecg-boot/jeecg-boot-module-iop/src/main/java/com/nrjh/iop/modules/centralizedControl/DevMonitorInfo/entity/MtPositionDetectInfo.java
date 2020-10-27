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
 * @Description: MT_POSITION_DETECT_INFO
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Data
@TableName("midhd.mt_position_detect_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="mt_position_detect_info对象", description="MT_POSITION_DETECT_INFO")
public class MtPositionDetectInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键,对应序列*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键,对应序列")
    private Integer id;
	/**检定任务号*/
	@Excel(name = "检定任务号", width = 15)
    @ApiModelProperty(value = "检定任务号")
    private String taskNo;
	/**检定线编号  用于区分不同检定线（检定设备类别：单相表：D、三相表：S、采集终端：C、互感器：H；编号规则：1号线=001、2号线=002、3号线=003）如单相表1号线：D001*/
	@Excel(name = "检定线编号  用于区分不同检定线（检定设备类别：单相表：D、三相表：S、采集终端：C、互感器：H；编号规则：1号线=001、2号线=002、3号线=003）如单相表1号线：D001", width = 15)
    @ApiModelProperty(value = "检定线编号  用于区分不同检定线（检定设备类别：单相表：D、三相表：S、采集终端：C、互感器：H；编号规则：1号线=001、2号线=002、3号线=003）如单相表1号线：D001")
    private String lineNo;
	/**检定单元编号 用于区分不同检定单元（检定单元类别：外观识别单元：W，多功能检定单元：D，耐压单元：N，插卡单元：C，红外测试单元：H，信道测试单元：X;编号规则：1号单元=001、2号单元=002、3号单元=003）如外观识别单元1号单元：W001*/
	@Excel(name = "检定单元编号 用于区分不同检定单元（检定单元类别：外观识别单元：W，多功能检定单元：D，耐压单元：N，插卡单元：C，红外测试单元：H，信道测试单元：X;编号规则：1号单元=001、2号单元=002、3号单元=003）如外观识别单元1号单元：W001", width = 15)
    @ApiModelProperty(value = "检定单元编号 用于区分不同检定单元（检定单元类别：外观识别单元：W，多功能检定单元：D，耐压单元：N，插卡单元：C，红外测试单元：H，信道测试单元：X;编号规则：1号单元=001、2号单元=002、3号单元=003）如外观识别单元1号单元：W001")
    private String unitNo;
	/**表位编号*/
	@Excel(name = "表位编号", width = 15)
    @ApiModelProperty(value = "表位编号")
    private String positionNo;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String equipNo;
	/**本任务检定次数 在支持不合格回流重检的检定系统中，表示该设备在本任务中第n次检定，检定次数为正整数，默认从1开始，可用于统计一次检定合格率*/
	@Excel(name = "本任务检定次数 在支持不合格回流重检的检定系统中，表示该设备在本任务中第n次检定，检定次数为正整数，默认从1开始，可用于统计一次检定合格率", width = 15)
    @ApiModelProperty(value = "本任务检定次数 在支持不合格回流重检的检定系统中，表示该设备在本任务中第n次检定，检定次数为正整数，默认从1开始，可用于统计一次检定合格率")
    private Integer detSortNum;
	/**设备类别 设备类别（01：电能表；03：电流互感器；09：采集终端）*/
	@Excel(name = "设备类别 设备类别（01：电能表；03：电流互感器；09：采集终端）", width = 15)
    @ApiModelProperty(value = "设备类别 设备类别（01：电能表；03：电流互感器；09：采集终端）")
    private String equipCateg;
	/**接线方式（1：单相；2：三相三线；3：三相四线）*/
	@Excel(name = "接线方式（1：单相；2：三相三线；3：三相四线）", width = 15)
    @ApiModelProperty(value = "接线方式（1：单相；2：三相三线；3：三相四线）")
    private String wiringMode;
	/**接入方式（01：直接接入；02：经互感器接入）*/
	@Excel(name = "接入方式（01：直接接入；02：经互感器接入）", width = 15)
    @ApiModelProperty(value = "接入方式（01：直接接入；02：经互感器接入）")
    private String conMode;
	/**设备规格*/
	@Excel(name = "设备规格", width = 15)
    @ApiModelProperty(value = "设备规格")
    private String equipSpecCode;
	/**检定结论（01：合格；02：不合格；03：未检）*/
	@Excel(name = "检定结论（01：合格；02：不合格；03：未检）", width = 15)
    @ApiModelProperty(value = "检定结论（01：合格；02：不合格；03：未检）")
    private String detConcCode;
	/**不合格原因即导致检定结论不合格的检定项目,包括压接检查及其他检定项目.*/
	@Excel(name = "不合格原因即导致检定结论不合格的检定项目,包括压接检查及其他检定项目.", width = 15)
    @ApiModelProperty(value = "不合格原因即导致检定结论不合格的检定项目,包括压接检查及其他检定项目.")
    private String reasonCode;
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
