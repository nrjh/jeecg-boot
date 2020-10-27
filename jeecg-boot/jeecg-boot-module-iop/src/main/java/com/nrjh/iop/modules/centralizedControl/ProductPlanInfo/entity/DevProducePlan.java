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
 * @Description: DEV_PRODUCE_PLAN
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Data
@TableName("dev_produce_plan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="dev_produce_plan对象", description="DEV_PRODUCE_PLAN")
public class DevProducePlan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**0正常 1删除*/
	@Excel(name = "0正常 1删除", width = 15)
    @ApiModelProperty(value = "0正常 1删除")
    private java.lang.String isDel;
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**生产周期ID*/
	@Excel(name = "生产周期ID", width = 15)
    @ApiModelProperty(value = "生产周期ID")
    private java.lang.String produceCycleId;
	/**年份*/
	@Excel(name = "年份", width = 15)
    @ApiModelProperty(value = "年份")
    private java.lang.String cycleYear;
	/**月份*/
	@Excel(name = "月份", width = 15)
    @ApiModelProperty(value = "月份")
    private java.lang.String cycleMonth;
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
    @DictIop(dicCode = "H_EQUIP_SCALE")
    private java.lang.String equipScale;
	/**三相物料描述*/
	@Excel(name = "三相物料描述", width = 15)
    @ApiModelProperty(value = "三相物料描述")
    @DictIop(dicCode = "EQUIP_DESCRIBE_S")
    private java.lang.String equipDescribeS;
	/**三相频率*/
	@Excel(name = "三相频率", width = 15)
    @ApiModelProperty(value = "三相频率")
    @DictIop(dicCode = "D_EQUIP_RATE_S")
    private java.lang.String equipRateS;
	/**三相电流*/
	@Excel(name = "三相电流 ", width = 15)
    @ApiModelProperty(value = "三相电流 ")
    @DictIop(dicCode = "D_EQUIP_I_S")
    private java.lang.String equipI;
	/**终端物料描述*/
	@Excel(name = "终端物料描述 ", width = 15)
    @ApiModelProperty(value = "终端物料描述 ")
    @DictIop(dicCode = "EQUIP_DESCRIBE_C")
    private java.lang.String equipDescribeC;
	/**终端载波频率*/
	@Excel(name = "终端载波频率", width = 15)
    @ApiModelProperty(value = "终端载波频率")
    @DictIop(dicCode = "C_EQUIP_RATE")
    private java.lang.String equipRateC;
	/**终端电压/通信方式*/
	@Excel(name = "终端电压/通信方式", width = 15)
    @ApiModelProperty(value = "终端电压/通信方式")
    @DictIop(dicCode = "C_EQUIP_SIGNAL")
    private java.lang.String equipSignalC;
	/**拟检测数*/
	@Excel(name = "拟检测数", width = 15)
    @ApiModelProperty(value = "拟检测数")
    private java.lang.Integer equipNumber;
	/**检测日类型*/
	@Excel(name = "检测日类型", width = 15)
    @ApiModelProperty(value = "检测日类型")
    @DictIop(dicCode = "DATE_TYPE")
    private java.lang.String dateType;
	/**计划检测天数	*/
	@Excel(name = "计划检测天数	", width = 15)
    @ApiModelProperty(value = "计划检测天数")
    private java.lang.Integer planDate;

	@Excel(name = "创建时间",width = 15)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
