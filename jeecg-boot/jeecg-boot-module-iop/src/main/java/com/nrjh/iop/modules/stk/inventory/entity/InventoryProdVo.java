package com.nrjh.iop.modules.stk.inventory.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.DictIop;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * Created by shiyiteng on 2020/8/6.
 */
@Data
public class InventoryProdVo {

    @Excel(name = "盘点单", width = 15)
    private int inventoryId;// 盘点
    @Excel(name = "库位", width = 15)
    private int locationId;// 库位
    @Excel(name = "供应商", width = 15)
    private int vendorId;//供应商
    @Excel(name = "物品id", width = 15)
    private int productId;
    @Excel(name = "品牌", width = 15)
    private int brandId;
    /**品类*/
    @Excel(name = "品类", width = 15)
    @ApiModelProperty(value = "品类")
    @DictIop(dictTable = "PRD_CATEGORY" ,dicCode = "id",dicText = "name",dataSource = "iop")
    private Integer categoryId;

    @Excel(name = "物品名", width = 15)
    private String rpoductName;
    @Excel(name = "物品编号", width = 15)
    private String productNo;
    @Excel(name = "价格", width = 15)
    private Double price;
    @Excel(name = "数量", width = 15)
    private BigDecimal productQty;
    @Excel(name = "状态", width = 15)
    private String status;


    private Double priceTotal;
    private BigDecimal productTotalQty;
    private String houseName;




}
