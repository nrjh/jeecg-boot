package com.nrjh.iop.modules.stk.move.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.nrjh.iop.modules.stk.move.entity.StkMoveLine;
import com.nrjh.iop.modules.stk.move.mapper.StkMoveLineMapper;
import com.nrjh.iop.modules.stk.move.mapper.StkMoveMapper;
import com.nrjh.iop.modules.stk.move.service.IStkMoveService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 移库单
 * @Author: jeecg-boot
 * @Date:   2020-05-13
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkMoveServiceImpl extends ServiceImpl<StkMoveMapper, StkMove> implements IStkMoveService {

	@Autowired
	private StkMoveMapper stkMoveMapper;
	@Autowired
	private StkMoveLineMapper stkMoveLineMapper;
	
	@Override
	@Transactional
	public void saveMain(StkMove stkMove, List<StkMoveLine> stkMoveLineList) {
		stkMoveMapper.insert(stkMove);
		if(stkMoveLineList!=null && stkMoveLineList.size()>0) {
			for(StkMoveLine entity:stkMoveLineList) {
				//外键设置
				entity.setMoveId(stkMove.getId());
				stkMoveLineMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(StkMove stkMove,List<StkMoveLine> stkMoveLineList) {
		stkMoveMapper.updateById(stkMove);
		
		//1.先删除子表数据
		stkMoveLineMapper.deleteByMainId(stkMove.getId().toString());
		
		//2.子表数据重新插入
		if(stkMoveLineList!=null && stkMoveLineList.size()>0) {
			for(StkMoveLine entity:stkMoveLineList) {
				//外键设置
				entity.setMoveId(stkMove.getId());
				stkMoveLineMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		stkMoveLineMapper.deleteByMainId(id);
		stkMoveMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			stkMoveLineMapper.deleteByMainId(id.toString());
			stkMoveMapper.deleteById(id);
		}
	}

	@Override
	public List<StkMove> selectByPickingId(String mainId) {
		return stkMoveMapper.selectByPickingId(mainId);
	}

	/**
	 * 移库单生成移库明细
	 * @param stkMove
	 */
	@Override
	public void createMoveLineByMove(StkMove stkMove) {
		StkMoveLine stkMoveLine = new StkMoveLine();
		stkMoveLine.setPickingId(stkMove.getPickingId());
		stkMoveLine.setMoveId(stkMove.getId());
		stkMoveLine.setProductId(stkMove.getProductId());
		stkMoveLine.setProductUomId(stkMove.getProductUomId());
		stkMoveLine.setProductQty(stkMove.getProductQty());
		stkMoveLine.setProductUomQty(stkMove.getProductUomQty());
		stkMoveLine.setProductDoneQty(stkMove.getProductUomQty());  //实际完成数量4
		stkMoveLine.setProductLotId(stkMove.getProductLotId());
		stkMoveLine.setProductLotName(null);
		stkMoveLine.setDateDone(stkMove.getDateDone());
		stkMoveLine.setState(stkMove.getState());
		stkMoveLine.setReference(stkMove.getReference());
		stkMoveLine.setLocationSrcId(stkMove.getLocationSrcId());
		stkMoveLine.setLocationDestId(stkMove.getLocationDestId());
		stkMoveLine.setCompanyId(stkMove.getCompanyId());
		stkMoveLine.setCreateBy(stkMove.getCreateBy());
		stkMoveLineMapper.insert(stkMoveLine);
	}


	/**
	 * 移库明细生成库存数量复式帐
	 */


}
