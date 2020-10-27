package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.po;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于获取设备类型、检定线的数据库映射对象PO
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/10/13 9:42
 */
@Data
public class KeyIdAndValuePO implements Serializable {

    private static final long serialVersionUID = -3287648527613761395L;

    /**
     * id
     */
    private String keyId;
    /**
     * 具体的值
     */
    private String value;
}
