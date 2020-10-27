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
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: D_IO_DET
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Data
@TableName("midhd_mds.d_io_det")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="d_io_det对象", description="D_IO_DET")
public class DIoDet implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ioDetId*/
	@Excel(name = "ioDetId", width = 15)
    @ApiModelProperty(value = "ioDetId")
    private Integer ioDetId;
	/**ioWhId*/
	@Excel(name = "ioWhId", width = 15)
    @ApiModelProperty(value = "ioWhId")
    private Integer ioWhId;
	/**detectDevId*/
	@Excel(name = "detectDevId", width = 15)
    @ApiModelProperty(value = "detectDevId")
    private Integer detectDevId;
	/**equipCateg*/
	@Excel(name = "equipCateg", width = 15)
    @ApiModelProperty(value = "equipCateg")
    private String equipCateg;
	/**arriveBatchNo*/
	@Excel(name = "arriveBatchNo", width = 15)
    @ApiModelProperty(value = "arriveBatchNo")
    private String arriveBatchNo;
	/**equipId*/
	@Excel(name = "equipId", width = 15)
    @ApiModelProperty(value = "equipId")
    private Integer equipId;
	/**equipCode*/
	@Excel(name = "equipCode", width = 15)
    @ApiModelProperty(value = "equipCode")
    private String equipCode;
	/**barCode*/
	@Excel(name = "barCode", width = 15)
    @ApiModelProperty(value = "barCode")
    private String barCode;
	/**boxBarCode*/
	@Excel(name = "boxBarCode", width = 15)
    @ApiModelProperty(value = "boxBarCode")
    private String boxBarCode;
	/**pileNo*/
	@Excel(name = "pileNo", width = 15)
    @ApiModelProperty(value = "pileNo")
    private String pileNo;
	/**dueRetDate*/
	@Excel(name = "dueRetDate", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "dueRetDate")
    private Date dueRetDate;
	/**palletBarCode*/
	@Excel(name = "palletBarCode", width = 15)
    @ApiModelProperty(value = "palletBarCode")
    private String palletBarCode;
	/**manufacturer*/
	@Excel(name = "manufacturer", width = 15)
    @ApiModelProperty(value = "manufacturer")
    private String manufacturer;
	/**isDetect*/
	@Excel(name = "isDetect", width = 15)
    @ApiModelProperty(value = "isDetect")
    private String isDetect;
	/**主体设备条形码*/
	@Excel(name = "主体设备条形码", width = 15)
    @ApiModelProperty(value = "主体设备条形码")
    private String mainEquipBarCode;
	/**主体设备ID*/
	@Excel(name = "主体设备ID", width = 15)
    @ApiModelProperty(value = "主体设备ID")
    private Integer mainEquipId;
	/**主体设备设备类别：01：电能表，09：采集终端*/
	@Excel(name = "主体设备设备类别：01：电能表，09：采集终端", width = 15)
    @ApiModelProperty(value = "主体设备设备类别：01：电能表，09：采集终端")
    private String mainEquipCateg;
	/**组箱码 引用F_EQUIP_GROUP_CODE表EQUIP_GROUP_CODE*/
	@Excel(name = "组箱码 引用F_EQUIP_GROUP_CODE表EQUIP_GROUP_CODE", width = 15)
    @ApiModelProperty(value = "组箱码 引用F_EQUIP_GROUP_CODE表EQUIP_GROUP_CODE")
    private String equipGroupCode;
    @TableLogic
    private Integer isDel;
}
