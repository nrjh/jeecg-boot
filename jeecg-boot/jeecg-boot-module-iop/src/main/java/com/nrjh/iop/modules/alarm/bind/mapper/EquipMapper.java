package com.nrjh.iop.modules.alarm.bind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.bind.entity.FaultPrdProductInfo;
import com.nrjh.iop.modules.alarm.bind.vo.TreeModel;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: o_equip
 * @Author: jeecg-boot
 * @Date: 2020-08-26
 * @Version: V1.0
 */
public interface EquipMapper extends BaseMapper<Equip> {

    @Select("SELECT param_id id ,param_name title, 0 as pid,0 as is_equip FROM P_COMM_CODE T WHERE SORT_ID = 'EQUIP_CATEG' AND STATUS = '1'")
    public List<TreeModel> getAllEquipCategory();

    @Select("SELECT area_id id,area_name title,equip_categ pid ,0 as is_equip FROM o_area")
    public List<TreeModel> getAllArea();

    @Select("SELECT equip_id id,name title, area_id pid ,1 as is_equip FROM o_equip where is_del='0' and new_flag='1'")
    public List<TreeModel> getAllEquip();

    List<String> queryEquipNo(String equipType);

}
