package com.nrjh.iop.modules.stk.move.service;

import com.nrjh.iop.modules.stk.move.entity.StkMoveLine;
import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 移库单
 * @Author: jeecg-boot
 * @Date:   2020-05-13
 * @Version: V1.0
 */
public interface IStkMoveService extends IService<StkMove> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(StkMove stkMove, List<StkMoveLine> stkMoveLineList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(StkMove stkMove, List<StkMoveLine> stkMoveLineList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);



	public List<StkMove> selectByPickingId(String mainId);


	/**
	 * 通过移动单生成明细单
	 * @param stkMove
	 */
	public void createMoveLineByMove(StkMove stkMove);

}
