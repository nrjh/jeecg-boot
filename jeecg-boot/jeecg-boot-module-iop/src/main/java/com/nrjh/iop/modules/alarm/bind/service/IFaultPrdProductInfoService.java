package com.nrjh.iop.modules.alarm.bind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.alarm.bind.entity.FaultPrdProductInfo;
import com.nrjh.iop.modules.alarm.bind.vo.EquipAlarmRefVO;
import com.nrjh.iop.modules.alarm.bind.vo.FaultPrdProductInfoVO;

import java.util.List;

/**
 * @Description: fault_prd_product_info
 * @Author: jeecg-boot
 * @Date: 2020-08-26
 * @Version: V1.0
 */
public interface IFaultPrdProductInfoService extends IService<FaultPrdProductInfo> {
    public List<FaultPrdProductInfoVO> pageAlarmByFaultTypeAndId(FaultPrdProductInfoVO faultPrdProductInfoVO);
    public List<FaultPrdProductInfoVO> pageAlarmByLineNoAndId(FaultPrdProductInfoVO faultPrdProductInfoVO);
    public List<FaultPrdProductInfoVO> queryFaultPrdProduct(FaultPrdProductInfoVO faultPrdProductInfoVO);

    public void save(EquipAlarmRefVO equipAlarmRefVo);
    public String queryExplanInfo(String lineNo);
}
