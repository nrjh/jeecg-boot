package com.nrjh.iop.modules.prd.attribute.mapper;

import java.util.List;
import com.nrjh.iop.modules.prd.attribute.entity.PrdAttributeValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 属性值
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
public interface PrdAttributeValueMapper extends BaseMapper<PrdAttributeValue> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PrdAttributeValue> selectByMainId(@Param("mainId") String mainId);
}
