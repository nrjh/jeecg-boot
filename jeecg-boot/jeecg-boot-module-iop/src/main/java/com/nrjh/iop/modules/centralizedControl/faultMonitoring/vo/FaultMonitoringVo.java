package com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-09-25 14:19
 */
@Data
public class FaultMonitoringVo implements Serializable {
    /** 设备类型 */
    private String equipCateg;

    /** 检定线 */
    private String areaId;

    /** 统计维度 */
    private String classify;

    /** 开始时间 */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    /** 结束时间 */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    /** 线图标识*/
    private String lineCode;

    private String faultGrade;

    private String equipType;

    /**
     * 设备编号
     */
    private String equipNO;

    /**
     * 故障名称
     */
    private String faultName;
}
