package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/10 18:10
 */
@Data
@TableName("midhd_mds.d_io_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="d_io_task对象", description="d_io_task")
public class DIoTask implements Serializable {
    private static final long serialVersionUID = -2634430270799055678L;
    @ApiModelProperty(value = "任务编号")
    private Integer taskId;
    @ApiModelProperty(value = "出入库任务编号")
    private String taskNo;
    @ApiModelProperty(value = "出入类别")
    private String ioFlag;
    @ApiModelProperty(value = "任务类别")
    private String taskPrio;
    @ApiModelProperty(value = "启动时间")
    private Date createDate;
    @ApiModelProperty(value = "任务量")
    private Integer qty;
    @ApiModelProperty(value = "已完成量")
    private Integer finishedIoQty;
    @TableLogic
    private Integer isDel;
}
