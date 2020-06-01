package com.nrjh.iop.modules.stk.move.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.nrjh.iop.modules.stk.move.entity.StkMoveLine;
import com.nrjh.iop.modules.stk.move.mapper.StkMoveLineMapper;
import com.nrjh.iop.modules.stk.move.service.IStkMoveLineService;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 库存移动明细
 * @Author: jeecg-boot
 * @Date:   2020-05-13
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkMoveLineServiceImpl extends ServiceImpl<StkMoveLineMapper, StkMoveLine> implements IStkMoveLineService {
	
	@Autowired
	private StkMoveLineMapper stkMoveLineMapper;
	
	@Override
	public List<StkMoveLine> selectByMainId(String mainId) {
		return stkMoveLineMapper.selectByMainId(mainId);
	}

	@Override
	public void insertByMove(StkMove stkMove, String state) {
		StkMoveLine stkMoveLine = new StkMoveLine();
		stkMoveLine.setMoveId(stkMove.getId());
		stkMoveLine.setPickingId(stkMove.getPickingId());
		stkMoveLine.setCompanyId(stkMove.getCompanyId());
		stkMoveLine.setLocationDestId(stkMove.getLocationDestId());
		stkMoveLine.setLocationSrcId(stkMove.getLocationSrcId());
		stkMoveLine.setReference(stkMove.getReference());
		stkMoveLine.setProductLotId(stkMove.getProductLotId());
		//stkMoveLine.setProductLotName();   // 移库单无此属性
		stkMoveLine.setProductId(stkMove.getProductId());
		stkMoveLine.setProductQty(stkMove.getProductQty());
		stkMoveLine.setProductUomQty(stkMove.getProductUomQty());   // 保留数量=移库单-需求数量
		stkMoveLine.setProductDoneQty(stkMoveLine.getProductUomQty());  // 完成数量=保留数量
		stkMoveLine.setProductUomId(stkMove.getProductUomId());
		stkMoveLine.setDateDone(stkMove.getDateDone());
		if (StringUtil.isNullOrEmpty(state)){
			stkMoveLine.setState(stkMove.getState());
		}else{
			stkMoveLine.setState(state);
		}
		stkMoveLineMapper.insert(stkMoveLine);
	}


}
