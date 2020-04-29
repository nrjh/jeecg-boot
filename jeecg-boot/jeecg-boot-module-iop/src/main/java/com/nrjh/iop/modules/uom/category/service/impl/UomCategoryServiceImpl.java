package com.nrjh.iop.modules.uom.category.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.uom.category.entity.UomCategory;
import com.nrjh.iop.modules.uom.category.entity.UomUom;
import com.nrjh.iop.modules.uom.category.mapper.UomUomMapper;
import com.nrjh.iop.modules.uom.category.mapper.UomCategoryMapper;
import com.nrjh.iop.modules.uom.category.service.IUomCategoryService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 计量单位类别
 * @Author: jeecg-boot
 * @Date:   2020-04-29
 * @Version: V1.0
 */
@Service
@DS("iop")
public class UomCategoryServiceImpl extends ServiceImpl<UomCategoryMapper, UomCategory> implements IUomCategoryService {

	@Autowired
	private UomCategoryMapper uomCategoryMapper;
	@Autowired
	private UomUomMapper uomUomMapper;
	
	@Override
	@Transactional
	public void saveMain(UomCategory uomCategory, List<UomUom> uomUomList) {
		uomCategoryMapper.insert(uomCategory);
		if(uomUomList!=null && uomUomList.size()>0) {
			for(UomUom entity:uomUomList) {
				//外键设置
				entity.setCategoryId(uomCategory.getId());
				// 计量类型 继承主表
				entity.setMeasureType(uomCategory.getMeasureType());
				uomUomMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(UomCategory uomCategory,List<UomUom> uomUomList) {
		uomCategoryMapper.updateById(uomCategory);
		
		//1.先删除子表数据
		uomUomMapper.deleteByMainId(uomCategory.getId().toString());
		
		//2.子表数据重新插入
		if(uomUomList!=null && uomUomList.size()>0) {
			for(UomUom entity:uomUomList) {
				//外键设置
				entity.setCategoryId(uomCategory.getId());
				uomUomMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		uomUomMapper.deleteByMainId(id);
		uomCategoryMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			uomUomMapper.deleteByMainId(id.toString());
			uomCategoryMapper.deleteById(id);
		}
	}
	
}
