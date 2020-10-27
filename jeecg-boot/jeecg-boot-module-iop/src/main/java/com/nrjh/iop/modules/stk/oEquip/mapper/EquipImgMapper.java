package com.nrjh.iop.modules.stk.oEquip.mapper;


import com.nrjh.iop.modules.stk.oEquip.entity.EquipImg;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 设备图片表
 * @Author: jeecg-boot
 * @Date:   2020-10-11
 * @Version: V1.0
 */
public interface EquipImgMapper extends BaseMapper<EquipImg> {

    public boolean deleteByMainId(@Param("mainId") String mainId);

    public EquipImg selectByMainId(@Param("mainId") String mainId);

}
