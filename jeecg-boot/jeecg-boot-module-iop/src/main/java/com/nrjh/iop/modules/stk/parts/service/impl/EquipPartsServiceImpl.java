package com.nrjh.iop.modules.stk.parts.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.alarm.bind.vo.TreeModel;
import com.nrjh.iop.modules.stk.parts.entity.EquipParts;
import com.nrjh.iop.modules.stk.parts.mapper.EquipPartsMapper;
import com.nrjh.iop.modules.stk.parts.service.IEquipPartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: O_EQUIP_PARTS
 * @Author: jeecg-boot
 * @Date:   2020-09-30
 * @Version: V1.0
 */
@Service
@DS("ora")
public class EquipPartsServiceImpl extends ServiceImpl<EquipPartsMapper, EquipParts> implements IEquipPartsService {

    @Autowired
    private EquipPartsMapper equipPartsMapper;

    @Override
    public List<TreeModel> selectTreeList() {
        return equipPartsMapper.selectTreeList();
    }

    @Override
    public Map<String, Object> selectEquipCategById(String id) {
        return equipPartsMapper.selectEquipCategById(id);
    }

    @Override
    public Map<String, Object> selectAreaById(String id) {
        return equipPartsMapper.selectAreaById(id);
    }

    @Override
    public Map<String, Object> selectEquipById(String id) {
        return equipPartsMapper.selectEquipById(id);
    }

    @Override
    public List<Map<String, Object>> selectStkPart(String id) {
        return equipPartsMapper.selectStkPart(id);
    }

    @Override
    public int insertStkPart(List<Map<String, Object>> list) {
        return equipPartsMapper.insertStkPart(list);
    }

    @Override
    public int deleteStkPartByPartId(String id) {
        return equipPartsMapper.deleteStkPartByPartId(id);
    }

}
