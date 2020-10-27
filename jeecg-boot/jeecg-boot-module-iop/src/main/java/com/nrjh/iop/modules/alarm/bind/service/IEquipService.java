package com.nrjh.iop.modules.alarm.bind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.bind.vo.TreeModel;

import java.util.List;

public interface IEquipService extends IService<Equip> {

    public List<TreeModel> getTreeList();

    List<String> quwerEquipNo(String equipType);
}
