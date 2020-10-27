package com.nrjh.iop.modules.intelligent.automation.entity;

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
 * @Description: FAILURES_COUNT
 * @Author: jeecg-boot
 * @Date:   2020-10-14
 * @Version: V1.0
 */
@Data
@TableName("failures_count")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="failures_count对象", description="FAILURES_COUNT")
public class FailuresCount implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "设备名称")
    private String equipName;
	/**时间范围*/
	@Excel(name = "时间范围", width = 15)
    @ApiModelProperty(value = "时间范围")
    private String timeRange;
	/**故障数量小于A类*/
	@Excel(name = "故障数量小于A类", width = 15)
    @ApiModelProperty(value = "故障数量小于A类")
    private Integer failureCountA;
	/**故障数量小于B类*/
	@Excel(name = "故障数量小于B类", width = 15)
    @ApiModelProperty(value = "故障数量小于B类")
    private Integer failureCountB;
	/**故障数量小于C类*/
	@Excel(name = "故障数量小于C类", width = 15)
    @ApiModelProperty(value = "故障数量小于C类")
    private Integer failureCountC;
	/**巡查频率*/
	@Excel(name = "巡查频率", width = 15)
    @ApiModelProperty(value = "巡查频率")
    private String patrolFrequency;
	/**状态（0未启用、1已启用）*/
	@Excel(name = "状态（0未启用、1已启用）", width = 15)
    @ApiModelProperty(value = "状态（0未启用、1已启用）")
    private Integer state;
	/**0未删除、1已删除*/
    @TableLogic
    private Integer isDel;
}
