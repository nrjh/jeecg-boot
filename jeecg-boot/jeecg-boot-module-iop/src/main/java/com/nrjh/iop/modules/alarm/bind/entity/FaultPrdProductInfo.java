package com.nrjh.iop.modules.alarm.bind.entity;

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
 * @Description: fault_prd_product_info
 * @Author: jeecg-boot
 * @Date: 2020-08-26
 * @Version: V1.0
 */
@Data
@TableName("fault_prd_product_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "fault_prd_product_info对象", description = "fault_prd_product_info")
public class FaultPrdProductInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记录唯一主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "记录唯一主键")
    private java.lang.String id;
    /**
     * 故障编号
     */
    @Excel(name = "故障编号", width = 15)
    @ApiModelProperty(value = "故障编号")
    private java.lang.String faultTaskId;
    /**
     * 检定线编号
     */
    @Excel(name = "检定线编号", width = 15)
    @ApiModelProperty(value = "检定线编号")
    private java.lang.String lineNo;
    /**
     * 物品
     */
    @Excel(name = "物品", width = 15)
    @ApiModelProperty(value = "物品")
    private java.lang.String productId;
    /**
     * 等级
     */
    @Excel(name = "等级", width = 15)
    @ApiModelProperty(value = "等级")
    private java.lang.String faultGrade;
    /**
     * 绑定状态
     */
    @Excel(name = "绑定状态", width = 15)
    @ApiModelProperty(value = "绑定状态")
    private java.lang.String status;
    /**
     * 设备故障标识码
     */
    @Excel(name = "设备故障标识码", width = 15)
    @ApiModelProperty(value = "设备故障标识码")
    private java.lang.String productFaultCode;
    /**
     * 创建人ID
     */
    @Excel(name = "创建人ID", width = 15)
    @ApiModelProperty(value = "创建人ID")
    private java.lang.String createId;
    /**
     * 创建人姓名
     */
    @Excel(name = "创建人姓名", width = 15)
    @ApiModelProperty(value = "创建人姓名")
    private java.lang.String createName;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 修改人
     */
    @Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
    /**
     * 修改人名称
     */
    @Excel(name = "修改人名称", width = 15)
    @ApiModelProperty(value = "修改人名称")
    private java.lang.String updateName;
    /**
     * 修改时间
     */
    @Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;

    @ApiModelProperty("设备故障名称")
    private String faultName;

    @TableLogic
    private Integer isDel;
}
