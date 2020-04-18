package com.nrjh.iop.modules.message.message.entity;

import java.io.Serializable;
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

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Data
@TableName("message_message")
public class MessageMessage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.ID_WORKER_STR)
	private java.lang.String id;
	/**主题*/
	@Excel(name = "主题", width = 15)
	private java.lang.String subject;
	/**作者*/
	@Excel(name = "作者", width = 15)
	private java.lang.String authorId;
	/**发布时间*/
	@Excel(name = "发布时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date pubTime;
	/**正文*/
	@Excel(name = "正文", width = 15)
	private java.lang.String body;
	/**上级*/
	@Excel(name = "上级", width = 15)
	private java.lang.String pid;
	/**关联数据表*/
	@Excel(name = "关联数据表", width = 15)
	private java.lang.String relaModel;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
	private java.lang.String hasChild;
	/**关联标识*/
	@Excel(name = "关联标识", width = 15)
	private java.lang.String relaId;
	/**关联记录名称*/
	@Excel(name = "关联记录名称", width = 15)
	private java.lang.String recordName;
	/**消息类型 email:邮件；comment:备注；notification:通知*/
	@Excel(name = "消息类型 email:邮件；comment:备注；notification:通知", width = 15, dicCode = "IOP_MESSAGE_TYPE")
	private java.lang.String messageType;
	/**子类型*/
	@Excel(name = "子类型", width = 15)
	private java.lang.String subtypeId;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
}
