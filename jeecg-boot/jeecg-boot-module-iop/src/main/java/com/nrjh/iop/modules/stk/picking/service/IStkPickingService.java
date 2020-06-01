package com.nrjh.iop.modules.stk.picking.service;

import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.nrjh.iop.modules.stk.picking.entity.StkPicking;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 调拨单
 * @Author: jeecg-boot
 * @Date:   2020-05-18
 * @Version: V1.0
 */
public interface IStkPickingService extends IService<StkPicking> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(StkPicking stkPicking, List<StkMove> stkMoveList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(StkPicking stkPicking, List<StkMove> stkMoveList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);

}
