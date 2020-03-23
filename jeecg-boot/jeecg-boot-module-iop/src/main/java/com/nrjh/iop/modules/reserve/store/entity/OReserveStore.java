package com.nrjh.iop.modules.reserve.store.entity;

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

/**
 * @Description: O_RESERVE_STORE
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
@Data
@TableName("iop_o_reserve_store")
public class OReserveStore implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**节点标识*/
	@TableId(type = IdType.AUTO)
	@Excel(name = "节点标识", width = 15)
    private String storeId;
	/**节点名称*/
	@Excel(name = "节点名称", width = 15)
    private String storeName;
	/**节点编号*/
	@Excel(name = "节点编号", width = 15)
    private String storeNo;
	/**节点位置*/
	@Excel(name = "节点位置", width = 15)
    private String storeAddress;
	/**节点级别
1-库房 2-库区 3-货架 4-货位*/
	@Excel(name = "节点级别 1-库房 2-库区 3-货架 4-货位", width = 15)
    private java.lang.Integer storeLevel;
	/**父级节点*/
	@Excel(name = "父级节点", width = 15)
    private java.lang.String storeParent;
	/**节点状态
1-启用 0-禁用*/
	@Excel(name = "节点状态 1-启用 0-禁用", width = 15)
    private java.lang.String storeStatus;
	/**是否虚拟*/
	@Excel(name = "是否虚拟", width = 15)
    private java.lang.String storeIsvirtual;
	/**四线一库设备*/
	@Excel(name = "四线一库设备", width = 15)
    private java.lang.String storeEquipCateg;
	/**层*/
	@Excel(name = "层", width = 15)
    private java.lang.Integer storeLayer;
	/**列*/
	@Excel(name = "列", width = 15)
    private java.lang.Integer storeColumn;
	/**操作时间*/
	@Excel(name = "操作时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date optDate;
	/**操作人*/
	@Excel(name = "操作人", width = 15)
    private java.lang.String optCode;
}
