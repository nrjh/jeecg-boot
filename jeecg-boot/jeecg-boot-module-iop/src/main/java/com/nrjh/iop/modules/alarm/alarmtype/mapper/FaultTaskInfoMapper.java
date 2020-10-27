package com.nrjh.iop.modules.alarm.alarmtype.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultTaskInfo;
import com.nrjh.iop.modules.alarm.alarmtype.vo.EquipIdModel;

import java.util.List;
import java.util.Map;

/**
 * @Description: 报警类型管理
 * @Author: jeecg-boot
 * @Date:   2020-08-25
 * @Version: V1.0
 */
public interface FaultTaskInfoMapper extends BaseMapper<FaultTaskInfo> {

    List<EquipIdModel> selectFristEquip();

    List<EquipIdModel> selectThreeEquip( String paramId);
    List<EquipIdModel> selectEquipType( String paramId);
    List<EquipIdModel> selectEquipByEquipTypeId( String paramId);
    List<EquipIdModel> selectEquipById( String equipId);

    List<Map> queryFaulNameByEquipType(String equipType);

}
