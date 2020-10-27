package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.serivce.impl;

import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.mapper.EquipCategAndAreaMapper;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.po.KeyIdAndValuePO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.serivce.EquipCategAndAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/10/13 9:48
 */
@Service
public class EquipCategAndAreaServiceImpl implements EquipCategAndAreaService {
    @Autowired
    private EquipCategAndAreaMapper mapper;
    @Override
    public List<KeyIdAndValuePO> getAllEquipCateg() {
        return mapper.getAllEquipCateg();
    }

    @Override
    public List<KeyIdAndValuePO> getAllCheckLine() {
        return mapper.getAllCheckLine();
    }
}
