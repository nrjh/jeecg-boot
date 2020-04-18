package com.nrjh.iop.modules.message.trackingvalue.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @Description: 变更记录
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("message_tracking_value")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="message_tracking_value对象", description="变更记录")
public class MessageTrackingValue implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "标识")
    private String id;
	/**字段代码*/
	@Excel(name = "字段代码", width = 15)
    @ApiModelProperty(value = "字段代码")
    private String field;
	/**字段描述*/
	@Excel(name = "字段描述", width = 15)
    @ApiModelProperty(value = "字段描述")
    private String fieldDesc;
	/**字段类型*/
	@Excel(name = "字段类型", width = 15)
    @ApiModelProperty(value = "字段类型")
    private String fieldType;
	/**旧整数值*/
	@Excel(name = "旧整数值", width = 15)
    @ApiModelProperty(value = "旧整数值")
    private Integer oldValueInteger;
	/**旧浮点值*/
	@Excel(name = "旧浮点值", width = 15)
    @ApiModelProperty(value = "旧浮点值")
    private Double oldValueFloat;
	/**旧货币值*/
	@Excel(name = "旧货币值", width = 15)
    @ApiModelProperty(value = "旧货币值")
    private Double oldValueMonotary;
	/**旧字符值*/
	@Excel(name = "旧字符值", width = 15)
    @ApiModelProperty(value = "旧字符值")
    private String oldValueChar;
	/**旧文本值*/
	@Excel(name = "旧文本值", width = 15)
    @ApiModelProperty(value = "旧文本值")
    private String oldValueText;
	/**旧日期时间值*/
	@Excel(name = "旧日期时间值", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "旧日期时间值")
    private Date oldValueDatetime;
	/**新整数值*/
	@Excel(name = "新整数值", width = 15)
    @ApiModelProperty(value = "新整数值")
    private Integer newValueInteger;
	/**新浮点值*/
	@Excel(name = "新浮点值", width = 15)
    @ApiModelProperty(value = "新浮点值")
    private Double newValueFloat;
	/**新货币值*/
	@Excel(name = "新货币值", width = 15)
    @ApiModelProperty(value = "新货币值")
    private Double newValueMonotary;
	/**新字符值*/
	@Excel(name = "新字符值", width = 15)
    @ApiModelProperty(value = "新字符值")
    private String newValueChar;
	/**新文本值*/
	@Excel(name = "新文本值", width = 15)
    @ApiModelProperty(value = "新文本值")
    private String newValueText;
	/**新日期时间值*/
	@Excel(name = "新日期时间值", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "新日期时间值")
    private Date newValueDatetime;
	/**消息*/
	@Excel(name = "消息", width = 15)
    @ApiModelProperty(value = "消息")
    private String messageId;
	/**跟踪序号*/
	@Excel(name = "跟踪序号", width = 15)
    @ApiModelProperty(value = "跟踪序号")
    private Integer trackingSequence;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
