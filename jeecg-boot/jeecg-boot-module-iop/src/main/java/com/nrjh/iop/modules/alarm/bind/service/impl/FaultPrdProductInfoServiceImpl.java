package com.nrjh.iop.modules.alarm.bind.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nrjh.iop.modules.alarm.bind.entity.FaultPrdProductInfo;
import com.nrjh.iop.modules.alarm.bind.mapper.FaultPrdProductInfoMapper;
import com.nrjh.iop.modules.alarm.bind.service.IFaultPrdProductInfoService;
import com.nrjh.iop.modules.alarm.bind.vo.EquipAlarmRefVO;
import com.nrjh.iop.modules.alarm.bind.vo.FaultPrdProductInfoVO;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: fault_prd_product_info
 * @Author: jeecg-boot
 * @Date: 2020-08-26
 * @Version: V1.0
 */
@Service
@DS("ora")
public class FaultPrdProductInfoServiceImpl extends ServiceImpl<FaultPrdProductInfoMapper, FaultPrdProductInfo> implements IFaultPrdProductInfoService {
    @Autowired
    FaultPrdProductInfoMapper faultPrdProductInfoMapper;

    @Override
    public List<FaultPrdProductInfoVO> pageAlarmByFaultTypeAndId(FaultPrdProductInfoVO faultPrdProductInfoVO) {
        return faultPrdProductInfoMapper.pageAlarmByFaultTypeAndId(faultPrdProductInfoVO);
    }

    @Override
    public List<FaultPrdProductInfoVO> pageAlarmByLineNoAndId(FaultPrdProductInfoVO faultPrdProductInfoVO) {
        return faultPrdProductInfoMapper.pageAlarmByLineNoAndId(faultPrdProductInfoVO);
    }

    @Override
    public List<FaultPrdProductInfoVO> queryFaultPrdProduct(FaultPrdProductInfoVO faultPrdProductInfoVO) {
        return faultPrdProductInfoMapper.queryFaultPrdProduct(faultPrdProductInfoVO);
    }

    @Override
    public void save(EquipAlarmRefVO equipAlarmRefVo) {
        List<FaultPrdProductInfoVO> list = equipAlarmRefVo.getFaultPrdProductInfos();
        for (FaultPrdProductInfoVO vo : list) {
            FaultPrdProductInfo entity = new FaultPrdProductInfo();
            BeanUtils.copyProperties(vo, entity);
            entity.setProductId(equipAlarmRefVo.getEquipId());
            entity.setLineNo(equipAlarmRefVo.getAreaId());
            entity.setFaultTaskId(String.valueOf(vo.getTaskId()));
            if (entity.getId() != null) {
                faultPrdProductInfoMapper.updateById(entity);
            } else {
//                todo 如果相同的检定线下的设备下的故障名称已经绑定了等级则不再重新绑定
                FaultPrdProductInfoVO checkVo=new FaultPrdProductInfoVO();
                checkVo.setProductId(equipAlarmRefVo.getEquipId());
                checkVo.setLineNo(equipAlarmRefVo.getAreaId());
                checkVo.setFaultName(vo.getFaultName());
                List<FaultPrdProductInfoVO> result=faultPrdProductInfoMapper.pageAlarmByLineNoAndId(checkVo);
                if(CollectionUtils.isEmpty(result)){
                    entity.setStatus("Y");
                    faultPrdProductInfoMapper.insert(entity);
                }else {
                    log.error(equipAlarmRefVo.getAreaId()+"该检定线下设备id"+equipAlarmRefVo.getEquipId()+"已经绑定了故障等级");
                }
            }
        }
    }

    @Override
    public String queryExplanInfo(String lineNo) {
        return faultPrdProductInfoMapper.queryExplanInfo(lineNo);
    }
}
