package com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-9-30 14:19
 */
@Data
public class FaultMonitoringTableResultVo implements Serializable {

    /** 报警类型 */
    private String faultLabe;

    /** 检定线名称 */
    private String lineName;
    /** 设备名称 */
    private String equipName;
    /** 故障频率 */
    private Integer frequency;
    /** 故障总时长*/
    private Double totalTime;

    /** 备品备件名称*/
    private String productName;
    /** 消耗量*/
    private Integer totalNum;
    /** 库存*/
    private Integer productQty;







}
