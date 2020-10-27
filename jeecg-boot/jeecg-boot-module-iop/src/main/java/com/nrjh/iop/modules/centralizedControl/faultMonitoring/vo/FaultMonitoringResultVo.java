package com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-09-25 14:19
 */
@Data
public class FaultMonitoringResultVo implements Serializable {
    /** 日期 */
    private String date;


    /** 单相表检定线数量*/
    private String single;
    /** 三相表检定线数量*/
    private String triphase;
    /** 互感器检定线数量*/
    private String mutual;
    /** 采集终端检定线数量*/
    private String terminal;
    /** 智能化仓储数量*/
    private String storage;
    /** 故障总数*/
    private String count;
    /** 故障总时长*/
    private Double totalTime;
    /** 故障处理及时率*/
    private Double avgs;

    /** 饼图名字集合*/
    List<String> faultNameList;
    /** 饼图/线图数据集合*/
    List< Map<String, Object>> mapData;

    /**
     * 流水线故障共用字段
     */
    private String name;

    /**
     * 构造数据
     */
    private Map data;

    private String nowTotal;//本月

    private String lastTotal;//上月

    private String yearAddition;//同比增加

    /**
     * 同比描述
     */
    private String msg;
}
