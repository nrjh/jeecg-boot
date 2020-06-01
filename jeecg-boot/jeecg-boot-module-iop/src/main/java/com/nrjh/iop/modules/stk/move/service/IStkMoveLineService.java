package com.nrjh.iop.modules.stk.move.service;

import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.nrjh.iop.modules.stk.move.entity.StkMoveLine;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 库存移动明细
 * @Author: jeecg-boot
 * @Date:   2020-05-13
 * @Version: V1.0
 */
public interface IStkMoveLineService extends IService<StkMoveLine> {

	public List<StkMoveLine> selectByMainId(String mainId);

	public void insertByMove(StkMove stkMove, String state);
}
