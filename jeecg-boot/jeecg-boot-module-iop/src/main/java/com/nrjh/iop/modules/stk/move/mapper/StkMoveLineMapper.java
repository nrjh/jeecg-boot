package com.nrjh.iop.modules.stk.move.mapper;

import java.util.List;

import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.nrjh.iop.modules.stk.move.entity.StkMoveLine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 库存移动明细
 * @Author: jeecg-boot
 * @Date:   2020-05-13
 * @Version: V1.0
 */
public interface StkMoveLineMapper extends BaseMapper<StkMoveLine> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<StkMoveLine> selectByMainId(@Param("mainId") String mainId);


}
