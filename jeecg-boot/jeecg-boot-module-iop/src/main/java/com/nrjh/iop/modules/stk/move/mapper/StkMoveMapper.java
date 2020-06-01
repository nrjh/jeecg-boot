package com.nrjh.iop.modules.stk.move.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 移库单
 * @Author: jeecg-boot
 * @Date:   2020-05-13
 * @Version: V1.0
 */
public interface StkMoveMapper extends BaseMapper<StkMove> {

    public boolean deleteByMainId(@Param("mainId") String mainId);

    public List<StkMove> selectByPickingId(@Param("mainId") String mainId);



}
