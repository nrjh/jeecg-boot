package com.nrjh.iop.modules.backorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.backorder.entity.StockBackOrderLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 退回入库明细
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
public interface StockBackOrderLineMapper extends BaseMapper<StockBackOrderLine> {

	public boolean deleteByMainId(@Param("mainId") Integer mainId);
    
	public List<StockBackOrderLine> selectByMainId(@Param("mainId") int mainId);
}
