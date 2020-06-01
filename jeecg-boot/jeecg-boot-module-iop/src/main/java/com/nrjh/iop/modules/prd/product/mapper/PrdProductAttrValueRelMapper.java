package com.nrjh.iop.modules.prd.product.mapper;

import java.util.List;
import com.nrjh.iop.modules.prd.product.entity.PrdProductAttrValueRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 物品属性值关系
 * @Author: jeecg-boot
 * @Date:   2020-05-06
 * @Version: V1.0
 */
public interface PrdProductAttrValueRelMapper extends BaseMapper<PrdProductAttrValueRel> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PrdProductAttrValueRel> selectByMainId(@Param("mainId") String mainId);
}
