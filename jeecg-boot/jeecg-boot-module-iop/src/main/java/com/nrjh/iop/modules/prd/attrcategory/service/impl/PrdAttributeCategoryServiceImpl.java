package com.nrjh.iop.modules.prd.attrcategory.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttributeCategory;
import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttrCategAttrValueRel;
import com.nrjh.iop.modules.prd.attrcategory.mapper.PrdAttrCategAttrValueRelMapper;
import com.nrjh.iop.modules.prd.attrcategory.mapper.PrdAttributeCategoryMapper;
import com.nrjh.iop.modules.prd.attrcategory.service.IPrdAttributeCategoryService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 规格
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
@Service
@DS("iop")
public class PrdAttributeCategoryServiceImpl extends ServiceImpl<PrdAttributeCategoryMapper, PrdAttributeCategory> implements IPrdAttributeCategoryService {

	@Autowired
	private PrdAttributeCategoryMapper prdAttributeCategoryMapper;
	@Autowired
	private PrdAttrCategAttrValueRelMapper prdAttrCategAttrValueRelMapper;
	
	@Override
	@Transactional
	public void saveMain(PrdAttributeCategory prdAttributeCategory, List<PrdAttrCategAttrValueRel> prdAttrCategAttrValueRelList) {
		prdAttributeCategoryMapper.insert(prdAttributeCategory);
		if(prdAttrCategAttrValueRelList!=null && prdAttrCategAttrValueRelList.size()>0) {
			prdAttributeCategory.setAttributeQty(prdAttrCategAttrValueRelList.size());
			for(PrdAttrCategAttrValueRel entity:prdAttrCategAttrValueRelList) {
				//外键设置
				entity.setAttributeCategoryId(prdAttributeCategory.getId());
				prdAttrCategAttrValueRelMapper.insert(entity);
			}
			prdAttributeCategoryMapper.updateById(prdAttributeCategory);
		}
	}

	@Override
	@Transactional
	public void updateMain(PrdAttributeCategory prdAttributeCategory,List<PrdAttrCategAttrValueRel> prdAttrCategAttrValueRelList) {
		prdAttributeCategoryMapper.updateById(prdAttributeCategory);
		
		//1.先删除子表数据
		prdAttrCategAttrValueRelMapper.deleteByMainId(prdAttributeCategory.getId().toString());
		
		//2.子表数据重新插入
		if(prdAttrCategAttrValueRelList!=null && prdAttrCategAttrValueRelList.size()>0) {
			prdAttributeCategory.setAttributeQty(prdAttrCategAttrValueRelList.size());
			for(PrdAttrCategAttrValueRel entity:prdAttrCategAttrValueRelList) {
				//外键设置
				entity.setAttributeCategoryId(prdAttributeCategory.getId());
				prdAttrCategAttrValueRelMapper.insert(entity);
				prdAttributeCategoryMapper.updateById(prdAttributeCategory);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		prdAttrCategAttrValueRelMapper.deleteByMainId(id);
		prdAttributeCategoryMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			prdAttrCategAttrValueRelMapper.deleteByMainId(id.toString());
			prdAttributeCategoryMapper.deleteById(id);
		}
	}
	
}
