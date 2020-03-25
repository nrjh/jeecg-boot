package com.nrjh.iop.modules.reserve.pallet.entity;

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
 * @Description: 托盘维护
 * @Author: jeecg-boot
 * @Date: 2020-03-16
 * @Version: V1.0
 */
@Data
@TableName("iop_o_reserve_pallet")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "iop_o_reserve_pallet对象", description = "托盘维护")
public class IopOReservePallet implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主建
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主建")
    private String id;
    /**
     * 标识
     */
    @Excel(name = "标识", width = 15)
    @ApiModelProperty(value = "标识")
    private String palletId;
    /**
     * 尺寸
     */
    @Excel(name = "尺寸", width = 15)
    @ApiModelProperty(value = "尺寸")
    private String palletSize;
    /**
     * 颜色
     */
    @Excel(name = "颜色", width = 15)
    @ApiModelProperty(value = "颜色")
    private String palletColor;
    /**
     * 制作材质
     */
    @Excel(name = "制作材质", width = 15)
    @ApiModelProperty(value = "制作材质")
    private String palletMaterial;
    /**
     * 编号
     */
    @Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
    private String palletNo;
    /**
     * 序号 用于排序
     */
    @Excel(name = "序号 用于排序", width = 15)
    @ApiModelProperty(value = "序号 用于排序")
    private Integer palletOrder;
    /**
     * 所属货位
     */
    @Excel(name = "所属货位", width = 15)
    @ApiModelProperty(value = "所属货位")
    @Dict(dicCode = "id",dictTable="iop_o_reserve_stroe",dicText="store_name")
    private String storeId;
    /**
     * 托盘状态
     * 1-启用 0-禁用
     */
    @Excel(name = "托盘状态 1-启用 0-禁用", width = 15)
    @ApiModelProperty(value = "托盘状态1-启用 0-禁用")
    private java.lang.String storeStatus;
    /**
     * 是否虚拟1-是 0-否
     */
    @Excel(name = "是否虚拟 1-是 0-否", width = 15)
    @ApiModelProperty(value = "是否虚拟 1-是 0-否")
    @Dict(dicCode = "IOP_TORF")
    private java.lang.String storeIsvirtual;
    /**
     * 操作时间
     */
    @Excel(name = "操作时间", width = 15, format = "yyyy-MM-dd h:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh24:mi:ss")
    @ApiModelProperty(value = "操作时间")
    private java.util.Date optDate;
    /**
     * 操作人
     */
    @Excel(name = "操作人", width = 15)
    @ApiModelProperty(value = "操作人")
    private java.lang.String optCode;
}
