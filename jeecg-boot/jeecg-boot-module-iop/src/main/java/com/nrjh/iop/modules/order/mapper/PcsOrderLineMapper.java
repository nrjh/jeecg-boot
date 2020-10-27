package com.nrjh.iop.modules.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.order.entity.PcsOrderLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 采购明细
 * @Author: jeecg-boot
 * @Date:   2020-08-15
 * @Version: V1.0
 */
public interface PcsOrderLineMapper extends BaseMapper<PcsOrderLine> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<PcsOrderLine> selectByMainId(@Param("mainId") String mainId);
}
