package com.nrjh.iop.modules.order.vo;

import io.swagger.annotations.ApiModelProperty;
import com.nrjh.iop.modules.stk.in.entity.OrderStockInList;
import lombok.Data;
import org.jeecg.common.aspect.annotation.DictIop;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by shiyiteng on 2020/8/13.
 */
@Data
public class ProdAndStockVo {



    private String name;//物料名称
    private String rpoductName;//物料名称
    private String productNo;//物料编号 对应物料表内部货号字段
    private Integer attributeCategoryId;//规格ID attributeCategoryID
    @DictIop(dictTable = "prd_brand",dicCode = "id", dicText = "manufactor_name",dataSource = "iop")
    private Integer partnerId;//供应商ID  VENDOR_ID
    private String partnerName;//供应商名称
    private String status;//状态
    private Integer categoryId;//品类，类别
    private Integer locationId;//库房

    private String proLocation;//货位
    private Integer productUomId;//单位
    private Integer productQty;//库存件数
    private Integer virtualQty;//虚拟库存

    private Integer scrapQty;//报废件数
    private Integer productId;//物料id
    private BigDecimal productUomQty;//申请件数手动输入
    private BigDecimal price;//金额
    private BigDecimal priceUnit;//采购单价
    private String categoryType; //物品品类

    private String remark;//备注

    private String id;//明细id
    private int orderId; //主表id

    private String cateName;
    private String acName;
    private String uuName;

    private Integer productPlyQty;//计划需求数量
    private Integer productActualQty;//实际需求数量
    // 退回的数量
    private Integer returnCount;

    //入货位时间
    private Date completeTime;

    //入库货位信息
    private java.util.List<OrderStockInList> orderStockInLists;

}
