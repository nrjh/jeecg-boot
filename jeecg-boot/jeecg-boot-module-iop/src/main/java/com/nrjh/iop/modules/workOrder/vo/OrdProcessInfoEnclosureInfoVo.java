package com.nrjh.iop.modules.workOrder.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-09-09 19:21
 */
@Data
public class OrdProcessInfoEnclosureInfoVo {
    /**主键*/
    private String id;
    /**工单单号*/
    private String workOrderId;
    /**修复类型*/
    private String execMode;
    /**修复方法名称*/
    private String repairName;
    /**修复方法id*/
    private String repairInfoId;
    /**报警级别*/
    private String alarmLevel;
    /**领料单号*/
    private String orderOutId;
    /**备注*/
    private String reason;
    /**创建人ID*/
    private String createId;
    /**创建人姓名*/
    private String createName;
    /**工单处理时*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**处理结果*/
    private String result;

    /**工单状态*/
    private String processStatus;
    /**附件名称*/
    private String enclosureName;
    /**附件地址*/
    private String enclosureUrl;
    /**故障原因**/
    private String errorReasonName;
    /**故障原因id**/
    private String errorReasonId;
    /**数据来源**/
    private String dataFrom;
}
