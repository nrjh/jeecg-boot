package com.nrjh.iop.modules.uom.category.mapper;

import java.util.List;
import com.nrjh.iop.modules.uom.category.entity.UomUom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 计量单位
 * @Author: jeecg-boot
 * @Date:   2020-04-29
 * @Version: V1.0
 */
public interface UomUomMapper extends BaseMapper<UomUom> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<UomUom> selectByMainId(@Param("mainId") String mainId);
}
