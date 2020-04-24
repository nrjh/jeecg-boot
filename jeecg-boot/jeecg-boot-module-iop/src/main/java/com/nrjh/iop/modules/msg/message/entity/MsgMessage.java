package com.nrjh.iop.modules.msg.message.entity;

import java.io.Serializable;
import java.util.Date;
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

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@ApiModel(value="msg_message对象", description="消息")
@Data
@TableName("msg_message")
public class MsgMessage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "标识")
    private String id;
	/**主题*/
	@Excel(name = "主题", width = 15)
    @ApiModelProperty(value = "主题")
    private String subject;
	/**作者*/
	@Excel(name = "作者", width = 15)
    @ApiModelProperty(value = "作者")
    private String authorId;
	/**发布时间*/
	@Excel(name = "发布时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发布时间")
    private Date pubTime;
	/**正文*/
	@Excel(name = "正文", width = 15)
    @ApiModelProperty(value = "正文")
    private String body;
	/**关联数据表*/
	@Excel(name = "关联数据表", width = 15)
    @ApiModelProperty(value = "关联数据表")
    private String relaModel;
	/**父级节点*/
	@Excel(name = "父级节点", width = 15)
    @ApiModelProperty(value = "父级节点")
    private String pid;
	/**关联标识*/
	@Excel(name = "关联标识", width = 15)
    @ApiModelProperty(value = "关联标识")
    private String relaId;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否有子节点")
    private String hasChild;
	/**关联记录名称*/
	@Excel(name = "关联记录名称", width = 15)
    @ApiModelProperty(value = "关联记录名称")
    private String recordName;
	/**消息类型*/
	@Excel(name = "消息类型", width = 15)
    @ApiModelProperty(value = "消息类型")
    private String messageType;
	/**子类型*/
	@Excel(name = "子类型", width = 15)
    @ApiModelProperty(value = "子类型")
    private String subtypeId;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String writeBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private Date writeTime;
}
