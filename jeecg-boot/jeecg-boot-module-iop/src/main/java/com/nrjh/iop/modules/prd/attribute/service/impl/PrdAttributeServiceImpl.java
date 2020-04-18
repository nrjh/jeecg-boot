package com.nrjh.iop.modules.prd.attribute.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.prd.attribute.entity.PrdAttribute;
import com.nrjh.iop.modules.prd.attribute.entity.PrdAttributeValue;
import com.nrjh.iop.modules.prd.attribute.mapper.PrdAttributeValueMapper;
import com.nrjh.iop.modules.prd.attribute.mapper.PrdAttributeMapper;
import com.nrjh.iop.modules.prd.attribute.service.IPrdAttributeService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 属性
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
@Service
@DS("iop")
public class PrdAttributeServiceImpl extends ServiceImpl<PrdAttributeMapper, PrdAttribute> implements IPrdAttributeService {

	@Autowired
	private PrdAttributeMapper prdAttributeMapper;
	@Autowired
	private PrdAttributeValueMapper prdAttributeValueMapper;
	
	@Override
	@Transactional
	public void saveMain(PrdAttribute prdAttribute, List<PrdAttributeValue> prdAttributeValueList) {
		prdAttributeMapper.insert(prdAttribute);
		if(prdAttributeValueList!=null && prdAttributeValueList.size()>0) {
			for(PrdAttributeValue entity:prdAttributeValueList) {
				//外键设置
				entity.setAttributeId(prdAttribute.getId());
				prdAttributeValueMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PrdAttribute prdAttribute,List<PrdAttributeValue> prdAttributeValueList) {
		prdAttributeMapper.updateById(prdAttribute);
		
		//1.先删除子表数据
		prdAttributeValueMapper.deleteByMainId(prdAttribute.getId().toString());
		
		//2.子表数据重新插入
		if(prdAttributeValueList!=null && prdAttributeValueList.size()>0) {
			for(PrdAttributeValue entity:prdAttributeValueList) {
				//外键设置
				entity.setAttributeId(prdAttribute.getId());
				prdAttributeValueMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		prdAttributeValueMapper.deleteByMainId(id);
		prdAttributeMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			prdAttributeValueMapper.deleteByMainId(id.toString());
			prdAttributeMapper.deleteById(id);
		}
	}
	
}
