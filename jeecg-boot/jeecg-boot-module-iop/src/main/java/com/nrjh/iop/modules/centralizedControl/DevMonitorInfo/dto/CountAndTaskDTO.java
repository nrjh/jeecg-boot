package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/22 15:56
 */
@Data
public class CountAndTaskDTO implements Serializable {
    private static final long serialVersionUID = -959064490894215103L;
    // 出入库任务单编号
    private String ioTaskNo;
    private Integer taskCount;
}
