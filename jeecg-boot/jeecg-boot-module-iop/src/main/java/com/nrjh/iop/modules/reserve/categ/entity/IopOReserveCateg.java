package com.nrjh.iop.modules.reserve.categ.entity;

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
 * @Description: 类别信息
 * @Author: jeecg-boot
 * @Date:   2020-03-25
 * @Version: V1.0
 */
@Data
@TableName("iop_o_reserve_categ")
public class IopOReserveCateg implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.UUID)
	private String id;
	/**父主键*/
	@Excel(name = "所属类别", width = 15)
	private String pid;
	/**创建人*/
	private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**修改人*/
	private String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	/**删除标识0-正常，1-已删除*/
	//@Excel(name = "删除标识0-正常，1-已删除", width = 15)
	private String delFlag;
	/**类别标识*/
	@Excel(name = "类别标识", width = 15)
	private String categId;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
	private String hasChild;
	/**类别名称*/
	@Excel(name = "类别名称", width = 15)
	private String categName;
	/**类别编码*/
	@Excel(name = "类别编码", width = 15)
	private String categNo;
	/**所属类别*/
	@Excel(name = "所属类别", width = 15)
	private String categParent;
	/**序号*/
	@Excel(name = "序号", width = 15)
	private Integer categOrder;
	/**计量单位*/
	@Excel(name = "计量单位", width = 15,dicCode = "IOP_RESERVE_UNIT")
	@Dict(dicCode = "IOP_RESERVE_UNIT")
	private String categUnit;
	/**安全库存*/
	@Excel(name = "安全库存", width = 15)
	private Integer categSafetyNum;
	/**预警库存*/
	@Excel(name = "预警库存", width = 15)
	private Integer categWarnNum;
	/**类别状态 1-启用 0-禁用*/
	@Excel(name = "类别状态", width = 15,dicCode = "IOP_RESERVE_STORE_STATUS")
	@Dict(dicCode = "IOP_RESERVE_STORE_STATUS")
	private java.lang.String categStatus;
	/**操作时间*/
	@Excel(name = "操作时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date optDate;
	/**操作人*/
	@Excel(name = "操作人", width = 15)
	private String optCode;
	/**isSingle*/
	private String isSingle;
}
