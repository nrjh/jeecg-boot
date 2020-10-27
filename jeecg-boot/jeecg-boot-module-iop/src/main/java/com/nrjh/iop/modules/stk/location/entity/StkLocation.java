package com.nrjh.iop.modules.stk.location.entity;

import java.io.Serializable;
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

/**
 * @Description: 位置
 * @Author: jeecg-boot
 * @Date:   2020-04-26
 * @Version: V1.0
 */
@Data
@TableName("stk_location")
public class StkLocation implements Serializable {
    private static final long serialVersionUID = 1L;

	/**标识*/
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**启用*/
	@Excel(name = "状态", width = 15,dicCode = "IOP_PUB_ACTION")
	@Dict(dicCode="IOP_PUB_ACTION")
	private Integer active;
	/**父位置*/
	@Excel(name = "父位置", width = 15,dictTable = "stk_location",dicCode = "id",dicText = "name",dataSource = "iop")
	private String pid;
	/**父路径*/
	@Excel(name = "父路径", width = 15)
	private String parentPath;
	/**名称*/
	@Excel(name = "名称", width = 15)
	private String name;
	/**位置全名*/
	@Excel(name = "位置全名", width = 15)
	private String completeName;
	/**条码*/
	@Excel(name = "条码", width = 15)
	private String barcode;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
	private String hasChild;
	/**位置类型*/
	@Excel(name = "位置类型", width = 15,dicCode = "IOP_STK_USAGE_TYPE")
	@Dict(dicCode = "IOP_STK_USAGE_TYPE")
	private String usageType;
	/**说明*/
	@Excel(name = "说明", width = 15)
	private String comment;
	/**X位置*/
	@Excel(name = "X位置", width = 15)
	private Integer posx;
	/**Y位置*/
	@Excel(name = "Y位置", width = 15)
	private Integer posy;
	/**Z位置*/
	@Excel(name = "Z位置", width = 15)
	private Integer posz;
	/**作为报废位置 如果该位置是报废位置，则该位置可存放已报废/已损坏的物资。*/
	@Excel(name = "作为报废位置 如果该位置是报废位置，则该位置可存放已报废/已损坏的物资。", width = 15)
	private Integer izScrapLocation;
	/**作为退库位置 允许使用该位置作为退回物资的存放位置。*/
	@Excel(name = "作为退库位置 允许使用该位置作为退回物资的存放位置。", width = 15)
	private Integer izReturnLocation;
	/**下架策略*/
	@Excel(name = "下架策略", width = 15)
	private Integer remaovalStrategyId;
	/**单位*/
	@Excel(name = "单位", width = 15)
	private Integer companyId;
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
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	@TableLogic
	private Integer isDel;
}
