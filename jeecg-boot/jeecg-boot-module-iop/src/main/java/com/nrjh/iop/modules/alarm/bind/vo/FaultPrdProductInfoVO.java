package com.nrjh.iop.modules.alarm.bind.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @Description: fault_prd_product_info
 * @Author: jeecg-boot
 * @Date: 2020-08-26
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "fault_prd_product_info对象", description = "fault_prd_product_info")
public class FaultPrdProductInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记录唯一主键
     */
    @ApiModelProperty(value = "记录唯一主键")
    private String id;
    /**
     * 故障编号
     */
    @Excel(name = "故障编号", width = 15)
    @ApiModelProperty(value = "故障编号")
    private String faultTaskId;
    /**
     * 检定线编号
     */
    @Excel(name = "检定线编号", width = 15)
    @ApiModelProperty(value = "检定线编号")
    private String lineNo;
    /**
     * 物品
     */
    @Excel(name = "物品", width = 15)
    @ApiModelProperty(value = "物品")
    private String productId;
    /**
     * 等级
     */
    @Excel(name = "等级", width = 15)
    @ApiModelProperty(value = "等级")
    private String faultGrade;
    /**
     * 绑定状态
     */
    @Excel(name = "绑定状态", width = 15)
    @ApiModelProperty(value = "绑定状态")
    private String status;
    /**
     * 设备故障标识码
     */
    @Excel(name = "设备故障标识码", width = 15)
    @ApiModelProperty(value = "设备故障标识码")
    private String productFaultCode;
    /**
     * 创建人ID
     */
    @Excel(name = "创建人ID", width = 15)
    @ApiModelProperty(value = "创建人ID")
    private String createId;
    /**
     * 创建人姓名
     */
    @Excel(name = "创建人姓名", width = 15)
    @ApiModelProperty(value = "创建人姓名")
    private String createName;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 修改人
     */
    @Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    /**
     * 修改人名称
     */
    @Excel(name = "修改人名称", width = 15)
    @ApiModelProperty(value = "修改人名称")
    private String updateName;
    /**
     * 修改时间
     */
    @Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @Excel(name = "报警ID", width = 15)
    @ApiModelProperty(value = "报警ID")
    private String taskId;
    @Excel(name = "异常设备分类", width = 15)
    @ApiModelProperty(value = "异常设备分类")
    private String faultType;
    @Excel(name = "报警名称", width = 15)
    @ApiModelProperty(value = "报警名称")
    private String faultName;
    @Excel(name = "故障运维班组", width = 15)
    @ApiModelProperty(value = "故障运维班组")
    private String lineTeam;
    @Excel(name = "故障类型", width = 15)
    @ApiModelProperty(value = "故障类型")
    private String faultLabe;
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String isStatus;
    @ApiModelProperty(value = "系统名称")
    private String 	systemName;
}
