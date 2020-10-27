package com.nrjh.iop.modules.stk.spareparts.entity;

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
 * @Description: 备品备件
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
@Data
@TableName("stk_spare_parts")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="stk_spare_parts对象", description="备品备件")
public class StkSpareParts implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;
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
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**物品表ID*/
    @Excel(name = "物品表ID", width = 15)
    @ApiModelProperty(value = "物品表ID")
    private Integer proId;
    /**库存表ID*/
    @Excel(name = "库存表ID", width = 15)
    @ApiModelProperty(value = "库存表ID")
    private Integer stockId;
	/**物料编号*/
	@Excel(name = "物料编号", width = 15)
    @ApiModelProperty(value = "物料编号")
    private String productNo;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称")
    private String productName;
	/**物料名称缩写*/
	@Excel(name = "物料名称缩写", width = 15)
    @ApiModelProperty(value = "物料名称缩写")
    private String productNameAbb;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")

    private Integer vendorId;
    /**品牌*/
    @Excel(name = "品牌", width = 15)
    @ApiModelProperty(value = "品牌")
    @DictIop(dictTable = "prd_brand", dicText = "name", dicCode = "id",dataSource = "iop")
    private Integer brandId;
	/**物料类别*/
	@Excel(name = "物料类别", width = 15)
    @ApiModelProperty(value = "物料类别")
    @DictIop(dictTable = "PRD_CATEGORY", dicText = "name", dicCode = "id",dataSource = "iop")
    private Integer categoryId;
	/**入库日期*/
	@Excel(name = "入库日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "入库日期")
    private Date enterDate;
	/**出入库类型*/
	@Excel(name = "出入库类型", width = 15)
    @ApiModelProperty(value = "出入库类型")
    private String inOutType;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String statusList;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
    @DictIop(dictTable = "prd_attribute_category", dicText = "name", dicCode = "id",dataSource = "iop")
    private Integer attributeCategoryId;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @DictIop(dictTable = "uom_uom", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "单位")
    private Integer uomId;
	/**物料状态*/
	@Excel(name = "物料状态", width = 15)
    @ApiModelProperty(value = "物料状态")
    @DictIop(dicCode ="IOP_STK_SPARE_PARTS_STATUS_PRO")
    private String statusPro;
	/**货位*/
	@Excel(name = "货位", width = 15)
    @DictIop(dictTable = "stk_location", dicText = "name", dicCode = "id",dataSource = "iop")
    @ApiModelProperty(value = "货位")
    private Integer locationId;
	/**库存实际数量*/
	@Excel(name = "库存实际数量", width = 15)
    @ApiModelProperty(value = "库存实际数量")
    private Integer productQty;
	/**最低库存*/
	@Excel(name = "最低库存", width = 15)
    @ApiModelProperty(value = "最低库存")
    private Integer stockMin;
	/**最高库存*/
	@Excel(name = "最高库存", width = 15)
    @ApiModelProperty(value = "最高库存")
    private Integer stockMax;
	/**库存年限*/
	@Excel(name = "库存年限", width = 15)
    @ApiModelProperty(value = "库存年限")
    private Integer stockYear;
	/**关联设备*/
	@Excel(name = "关联设备", width = 15)
    @ApiModelProperty(value = "关联设备")
//    @DictIop(dictTable = "o_equip", dicText = "name", dicCode = "equip_id",dataSource = "iop")
    @DictIop(dictTable = "P_COMM_CODE", dicText = "param_name", dicCode = "param_id",dataSource = "ora")
    private String joinParts;
	/**初始数量*/
	@Excel(name = "初始数量", width = 15)
    @ApiModelProperty(value = "初始数量")
    private Integer initQty;
	/**初始总金额*/
	@Excel(name = "初始总金额", width = 15)
    @ApiModelProperty(value = "初始总金额")
    private Double initAmount;
	/**警戒库存*/
	@Excel(name = "警戒库存", width = 15)
    @ApiModelProperty(value = "警戒库存")
    private Integer stockWarn;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**图片*/
	@Excel(name = "图片", width = 15)
    private transient String photoString;

    private byte[] photo;

    public byte[] getPhoto(){
        if(photoString==null){
            return null;
        }
        try {
            return photoString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPhotoString(){
        if(photo==null || photo.length==0){
            return "";
        }
        try {
            return new String(photo,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
	/**当前均价*/
	@Excel(name = "当前均价", width = 15)
    @ApiModelProperty(value = "当前均价")
    private BigDecimal priceAvg;
    @ApiModelProperty(value = "办公、备品标识")
    private Integer flag;
}
