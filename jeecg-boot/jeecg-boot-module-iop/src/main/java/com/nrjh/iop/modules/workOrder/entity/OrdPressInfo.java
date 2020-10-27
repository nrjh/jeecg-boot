package com.nrjh.iop.modules.workOrder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/7 16:20
 */
@Data
@TableName("ORD_PRESS_INFO")
@ApiModel(value = "ORD_PRESS_INFO",description = "工单督办关系表")
public class OrdPressInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 督办工单表
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "督办工单表")
    private String id;
    /**
     * 工单单号
     */
    @ApiModelProperty(value = "工单单号")
    private String workOrderId;
    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID")
    private String createId;
    /**
     * 创建人姓名
     */
    @ApiModelProperty(value = "创建人姓名")
    private String createName;
    /**
     * 	创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
