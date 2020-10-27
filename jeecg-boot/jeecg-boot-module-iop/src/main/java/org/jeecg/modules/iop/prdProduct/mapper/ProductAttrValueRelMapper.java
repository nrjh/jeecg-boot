package org.jeecg.modules.iop.prdProduct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.iop.prdProduct.entity.ProductAttrValueRel;

import java.util.List;

/**
 * @Description: 备品备件属性值关系
 * @Author: jeecg-boot
 * @Date:   2020-08-06
 * @Version: V1.0
 */
public interface ProductAttrValueRelMapper extends BaseMapper<ProductAttrValueRel> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ProductAttrValueRel> selectByMainId(@Param("mainId") String mainId);
}
