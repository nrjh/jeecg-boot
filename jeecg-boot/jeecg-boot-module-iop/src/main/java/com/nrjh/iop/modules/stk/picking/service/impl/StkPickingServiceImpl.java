package com.nrjh.iop.modules.stk.picking.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.stk.location.entity.StkLocation;
import com.nrjh.iop.modules.stk.location.mapper.StkLocationMapper;
import com.nrjh.iop.modules.stk.picking.entity.StkPicking;
import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.nrjh.iop.modules.stk.move.mapper.StkMoveMapper;
import com.nrjh.iop.modules.stk.picking.mapper.StkPickingMapper;
import com.nrjh.iop.modules.stk.picking.service.IStkPickingService;
import com.nrjh.iop.modules.stk.warehouse.entity.StkWarehouse;
import com.nrjh.iop.modules.stk.warehouse.mapper.StkWarehouseMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 调拨单
 * @Author: jeecg-boot
 * @Date:   2020-05-18
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkPickingServiceImpl extends ServiceImpl<StkPickingMapper, StkPicking> implements IStkPickingService {

	@Autowired
	private StkPickingMapper stkPickingMapper;
	@Autowired
	private StkMoveMapper stkMoveMapper;
	@Autowired
	private StkLocationMapper stkLocationMapper;
	@Autowired
	private StkWarehouseMapper stkWarehouseMapper;

	@Override
	@Transactional
	public void saveMain(StkPicking stkPicking, List<StkMove> stkMoveList) {
		stkPickingMapper.insert(stkPicking);
		// 调拨单生成是  创建移库单
		if(stkMoveList!=null && stkMoveList.size()>0) {
            Integer warehouseId = getWareHouseByDestId(stkPicking.getLocationDestId());
			for(StkMove entity:stkMoveList) {
				//外键设置
				entity.setPickingId(stkPicking.getId());
				// 属性继承
				entity.setPickingTypeId(stkPicking.getPickingTypeId());
				entity.setState(stkPicking.getState());
				entity.setCompanyId(stkPicking.getCompanyId());
				entity.setWarehouseId(warehouseId);
				entity.setLocationSrcId(stkPicking.getLocationSrcId());
				entity.setLocationDestId(stkPicking.getLocationDestId());
				entity.setDateExpected(stkPicking.getScheduledDate());
				entity.setDateDone(stkPicking.getDateDone());
				entity.setOrigin(stkPicking.getOrign());
				entity.setNote(stkPicking.getNote());
				stkMoveMapper.insert(entity);
			}
		}
	}



	@Override
	@Transactional
	public void updateMain(StkPicking stkPicking,List<StkMove> stkMoveList) {
		stkPickingMapper.updateById(stkPicking);
		
		//1.先删除子表数据
		stkMoveMapper.deleteByMainId(stkPicking.getId().toString());
		
		//2.子表数据重新插入
		if(stkMoveList!=null && stkMoveList.size()>0) {
			Integer warehouseId = getWareHouseByDestId(stkPicking.getLocationDestId());
			for(StkMove entity:stkMoveList) {
				//外键设置
				entity.setPickingId(stkPicking.getId());
				// 属性继承
				entity.setPickingTypeId(stkPicking.getPickingTypeId());
				entity.setState(stkPicking.getState());
				entity.setCompanyId(stkPicking.getCompanyId());
				entity.setWarehouseId(warehouseId);
				entity.setLocationSrcId(stkPicking.getLocationSrcId());
				entity.setLocationDestId(stkPicking.getLocationDestId());
				entity.setDateExpected(stkPicking.getScheduledDate());
				entity.setDateDone(stkPicking.getDateDone());
				entity.setOrigin(stkPicking.getOrign());
				entity.setNote(stkPicking.getNote());
				stkMoveMapper.insert(entity);
			}
		}
	}

	private Integer getWareHouseByDestId(Integer destId ){
		StkLocation stkLocation = stkLocationMapper.selectById(destId);
		String[] destArr = stkLocation.getParentPath().split("/");
		if(destArr!=null&&destArr.length>1){
			Integer viewLocationId = Integer.parseInt(destArr[1]);
			StkWarehouse stkWarehouse = stkWarehouseMapper.selectByViewLocationId(viewLocationId);
			return stkWarehouse.getId();
		}else{
			return null;
		}
	}


	@Override
	@Transactional
	public void delMain(String id) {
		stkMoveMapper.deleteByMainId(id);
		stkPickingMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			stkMoveMapper.deleteByMainId(id.toString());
			stkPickingMapper.deleteById(id);
		}
	}

}
