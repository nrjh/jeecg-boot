package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/22 16:03
 */
@Data
public class MdIoTask implements Serializable {
    private static final long serialVersionUID = 4511280909165910918L;
    private Integer qty;
    private String ioTaskNo;
    private String relaNo;
}
