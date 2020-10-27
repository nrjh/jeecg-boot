package com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecg.common.aspect.annotation.DictIop;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: DEV_PRODUCE_DPLAN
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Data
@TableName("dev_produce_dplan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="dev_produce_dplan对象", description="DEV_PRODUCE_DPLAN")
public class DevProduceDplan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**实际检测总数*/
	@Excel(name = "实际检测总数", width = 15)
    @ApiModelProperty(value = "实际检测总数")
    private Integer realSumNumber;
	/**状态01：试算02：生效00：删除*/
	@Excel(name = "状态 01：试算 02：生效 00：删除", width = 15)
    @ApiModelProperty(value = "状态 01：试算 02：生效 00：删除")
    private java.lang.String planStruts;
	/**完成总计划占比*/
	@Excel(name = "完成总计划占比", width = 15)
    @ApiModelProperty(value = "完成总计划占比")
    private java.math.BigDecimal realSumRatio;
	/**实际检测数量检测日类型为如下值：G：工作日Z：自然日	*/
	@Excel(name = "实际检测数量 检测日类型为如下值： G：工作日 Z：自然日	", width = 15)
    @ApiModelProperty(value = "实际检测数量 检测日类型为如下值：G：工作日 Z：自然日	")
    private java.lang.String realDayNumber;
	/**终端电压/通信方式*/
	@Excel(name = "终端电压/通信方式", width = 15)
    @ApiModelProperty(value = "终端电压/通信方式")
    private java.lang.String equipSignalC;
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**生产周期ID*/
	@Excel(name = "生产周期ID", width = 15)
    @ApiModelProperty(value = "生产周期ID")
    private java.lang.String produceCycleId;
	/**生产计划ID*/
	@Excel(name = "生产计划ID", width = 15)
    @ApiModelProperty(value = "生产计划ID")
    private java.lang.String producePlanId;
	/**年份*/
	@Excel(name = "年份", width = 15)
    @ApiModelProperty(value = "年份")
    private java.lang.String cycleYear;
	/**月份*/
	@Excel(name = "月份", width = 15)
    @ApiModelProperty(value = "月份")
    private java.lang.String cycleMonth;
	/**计划日*/
	@Excel(name = "计划日", width = 15)
    @ApiModelProperty(value = "计划日")
    private java.lang.String cycleDay;
	/**设备类别*/
	@Excel(name = "设备类别", width = 15)
    @ApiModelProperty(value = "设备类别")
    private java.lang.String equipCateg;
	/**单相频率*/
	@Excel(name = "单相频率", width = 15)
    @ApiModelProperty(value = "单相频率")
    @DictIop(dicCode = "D_EQUIP_RATE")
    private java.lang.String equipRateD;
	/**变比*/
	@Excel(name = "变比", width = 15)
    @ApiModelProperty(value = "变比")
    private java.lang.String equipScale;
	/**三相物料描述*/
	@Excel(name = "三相物料描述", width = 15)
    @ApiModelProperty(value = "三相物料描述")
    private java.lang.String equipDescribeS;
	/**三相频率*/
	@Excel(name = "三相频率 ", width = 15)
    @ApiModelProperty(value = "三相频率")
    private java.lang.String equipRateS;
	/**三相电流*/
	@Excel(name = "三相电流", width = 15)
    @ApiModelProperty(value = "三相电流")
    private java.lang.String equipI;
	/**终端物料描述*/
	@Excel(name = "终端物料描述", width = 15)
    @ApiModelProperty(value = "终端物料描述")
    private java.lang.String equipDescribeC;
	/**终端载波频率*/
	@Excel(name = "终端载波频率", width = 15)
    @ApiModelProperty(value = "终端载波频率")
    private java.lang.String equipRateC;
	/**拟检测数*/
	@Excel(name = "拟检测数", width = 15)
    @ApiModelProperty(value = "拟检测数")
    private java.lang.Integer planDayNumber;

	@ApiModelProperty("创建时间")
    @Excel(name = "创建时间", width = 15)
    private Date createTime;
}
