package com.nrjh.iop.modules.stk.parts.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.alarm.bind.vo.TreeModel;
import com.nrjh.iop.modules.stk.parts.entity.EquipParts;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: O_EQUIP_PARTS
 * @Author: jeecg-boot
 * @Date:   2020-09-30
 * @Version: V1.0
 */
@Repository
public interface EquipPartsMapper extends BaseMapper<EquipParts> {

    List<TreeModel> selectTreeList();

    Map<String,Object> selectEquipCategById(@Param("id")String id);

    Map<String,Object> selectAreaById(@Param("id")String id);

    Map<String,Object> selectEquipById(@Param("id")String id);

    List<Map<String,Object>> selectStkPart(@Param("id")String id);

    int insertStkPart(@Param("list")List<Map<String,Object>> list);

    int deleteStkPartByPartId(@Param("id") String id);
}
