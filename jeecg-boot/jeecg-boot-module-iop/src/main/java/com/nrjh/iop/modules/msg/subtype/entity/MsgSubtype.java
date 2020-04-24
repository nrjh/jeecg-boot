package com.nrjh.iop.modules.msg.subtype.entity;

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
 * @Description: 消息子类型
 * @Author: jeecg-boot
 * @Date:   2020-04-23
 * @Version: V1.0
 */
@Data
@TableName("msg_subtype")
public class MsgSubtype implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
	private String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
	private String name;
	/**描述*/
	@Excel(name = "描述", width = 15)
	private String description;
	/**仅内部*/
	@Excel(name = "仅内部", width = 15,dicCode = "yn")
	@Dict(dicCode = "yn")
	private Integer internal;
	/**父级*/
	@Excel(name = "父级", width = 15)
	private String pid;
	/**关联字段*/
	@Excel(name = "关联字段", width = 15)
	private String relaField;
	/**关联数据表*/
	@Excel(name = "关联数据表", width = 15)
	private String relaModel;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
	private String hasChild;
	/**默认*/
	@Excel(name = "默认", width = 15,dicCode = "yn")
	@Dict(dicCode = "yn")
	private Integer izDefault;
	/**顺序*/
	@Excel(name = "顺序", width = 15)
	private Integer sequence;
	/**隐藏*/
	@Excel(name = "隐藏", width = 15,dicCode = "yn")
	@Dict(dicCode = "yn")
	private Integer hidden;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	private String writeBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date writeTime;
}
