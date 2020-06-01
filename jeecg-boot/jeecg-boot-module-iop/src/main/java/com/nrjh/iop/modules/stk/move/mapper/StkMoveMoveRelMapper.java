package com.nrjh.iop.modules.stk.move.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.stk.move.entity.StkMoveLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 库存单与移库单关系
 * @Author: zzy
 * @Date:   2020-05-13
 * @Version: V1.0
 */
public interface StkMoveMoveRelMapper extends BaseMapper<StkMoveLine> {


}
