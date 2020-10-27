package com.nrjh.iop.modules.stk.stkscrap.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 报废管理---添加物料实体类
 * @Author: chenchao
 * @Date:   2020-08-07
 * @Version: V1.0
 */
@Data
@ToString
public class ScrapPro implements Serializable {

    private String scrapInventoryOrder;//报废单据号，跟报废主表做关联

    private String rpoductName;//物料名称
    private String productNo;//物料编号 对应物料表内部货号字段
    private Integer attributeCategoryId;//规格ID attributeCategoryID
    private Integer vendorId;//供应商ID  VENDOR_ID
    private String status;//状态

    private Integer categoryId;//品类，类别
    private String categoryType;//物料类别
    private Integer locationID;//库房
    private String proLocation;//货位
    private Integer productUomId;//单位
    private Integer productQty;//实际库存件数
    private Integer virtualQty; //虚拟库存件数
    private Integer scrapQty;//报废件数
    private Integer productId;//物料id
    private Integer prodNums;//申请件数手动输入
    private BigDecimal price;//金额
    private String name;//合同编号
    private String cateName;
    private String acName;
    private String uuName;

    private String id;



}
