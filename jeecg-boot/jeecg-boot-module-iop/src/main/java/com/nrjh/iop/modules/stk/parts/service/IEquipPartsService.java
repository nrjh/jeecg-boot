package com.nrjh.iop.modules.stk.parts.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.alarm.bind.vo.TreeModel;
import com.nrjh.iop.modules.stk.parts.entity.EquipParts;

import java.util.List;
import java.util.Map;

/**
 * @Description: O_EQUIP_PARTS
 * @Author: jeecg-boot
 * @Date:   2020-09-30
 * @Version: V1.0
 */
public interface IEquipPartsService extends IService<EquipParts> {
    List<TreeModel> selectTreeList();
    Map<String,Object> selectEquipCategById(String id);

    Map<String,Object> selectAreaById(String id);

    Map<String,Object> selectEquipById(String id);

    List<Map<String,Object>> selectStkPart(String id);

    int insertStkPart(List<Map<String,Object>> list);

    int deleteStkPartByPartId( String id);
}
