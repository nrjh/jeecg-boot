package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.serivce;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.po.KeyIdAndValuePO;

import java.util.List;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/10/13 9:48
 */
@DS("ora")
public interface EquipCategAndAreaService {
    /**
     * 获取所有设备类型
     * @return
     */
    List<KeyIdAndValuePO> getAllEquipCateg();

    /**
     * 获取所有检定线
     * @return
     */
    List<KeyIdAndValuePO> getAllCheckLine();
}
