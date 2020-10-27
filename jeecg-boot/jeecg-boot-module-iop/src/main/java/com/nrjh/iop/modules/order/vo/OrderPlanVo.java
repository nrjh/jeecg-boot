package com.nrjh.iop.modules.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by shiyiteng on 2020/8/11.
 */
@Data
public class OrderPlanVo {

    private String pickingNo;
    private String createBy;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date scheduledDate;
    private String groupNo;
    private String categoryType;
    private String orderType;
    private String note;
    private Integer id;
    private String lineId;
    private String lineName;

    List<ProdAndStockVo> prdProduct;

}
