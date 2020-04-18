package com.nrjh.iop.modules.prd.attrcategory.mapper;

import java.util.List;
import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttrCategAttrValueRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 物品规格与产品属性值关系
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
public interface PrdAttrCategAttrValueRelMapper extends BaseMapper<PrdAttrCategAttrValueRel> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PrdAttrCategAttrValueRel> selectByMainId(@Param("mainId") String mainId);
}
