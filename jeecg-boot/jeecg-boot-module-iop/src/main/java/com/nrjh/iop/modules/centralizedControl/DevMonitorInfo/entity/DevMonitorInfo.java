package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity;

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
import org.jeecg.common.aspect.annotation.DictIop;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 监控设备管理
 * @Author: jeecg-boot
 * @Date:   2020-09-11
 * @Version: V1.0
 */
@Data
@TableName("dev_monitor_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="dev_monitor_info对象", description="监控设备管理")
public class DevMonitorInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**线体编号*/
	@Excel(name = "线体编号", width = 15)
    @ApiModelProperty(value = "线体编号")
    @DictIop(dictTable = "o_area", dicText = "area_name", dicCode = "area_id",dataSource = "ora")
    private String lineNo;
	/**设备类型*/
	@Excel(name = "设备类型", width = 15)
    @DictIop(dictTable = "P_COMM_CODE", dicText = "param_name", dicCode = "param_id",dataSource = "ora")
    @ApiModelProperty(value = "设备类型")
    private String equipType;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private String equipNo;
	/**设备名称*/
    @DictIop(dictTable = "o_equip", dicText = "name", dicCode = "equip_no",dataSource = "ora")
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private String equipName;
	/**设备别名*/
	@Excel(name = "设备别名", width = 15)
    @ApiModelProperty(value = "设备别名")
    private String equipLabe;
	/**设备IP*/
	@Excel(name = "设备IP", width = 15)
    @ApiModelProperty(value = "设备IP")
    private String equipIp;
	/**用户名*/
	@Excel(name = "用户名", width = 15)
    @ApiModelProperty(value = "用户名")
    private String loginName;
	/**密码*/
	@Excel(name = "密码", width = 15)
    @ApiModelProperty(value = "密码")
    private String loginPasswd;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String reason;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
//    @TableLogic
//    private Integer isDel;
}
