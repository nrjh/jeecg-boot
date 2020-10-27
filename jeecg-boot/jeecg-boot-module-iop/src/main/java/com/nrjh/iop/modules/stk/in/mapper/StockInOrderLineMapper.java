package com.nrjh.iop.modules.stk.in.mapper;

import java.util.List;
import com.nrjh.iop.modules.stk.in.entity.StockInOrderLine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 入库明细
 * @Author: jeecg-boot
 * @Date:   2020-08-16
 * @Version: V1.0
 */
public interface StockInOrderLineMapper extends BaseMapper<StockInOrderLine> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<StockInOrderLine> selectByMainId(@Param("mainId") String mainId);

}
