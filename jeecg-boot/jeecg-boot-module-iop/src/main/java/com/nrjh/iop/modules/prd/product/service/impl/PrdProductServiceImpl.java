package com.nrjh.iop.modules.prd.product.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.prd.product.entity.PrdProduct;
import com.nrjh.iop.modules.prd.product.entity.PrdProductAttrValueRel;
import com.nrjh.iop.modules.prd.product.mapper.PrdProductAttrValueRelMapper;
import com.nrjh.iop.modules.prd.product.mapper.PrdProductMapper;
import com.nrjh.iop.modules.prd.product.service.IPrdProductService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 物品
 * @Author: jeecg-boot
 * @Date:   2020-05-06
 * @Version: V1.0
 */
@Service
@DS("iop")
public class PrdProductServiceImpl extends ServiceImpl<PrdProductMapper, PrdProduct> implements IPrdProductService {

	@Autowired
	private PrdProductMapper prdProductMapper;
	@Autowired
	private PrdProductAttrValueRelMapper prdProductAttrValueRelMapper;
	
	@Override
	@Transactional
	public void saveMain(PrdProduct prdProduct, List<PrdProductAttrValueRel> prdProductAttrValueRelList) {
		prdProductMapper.insert(prdProduct);
		if(prdProductAttrValueRelList!=null && prdProductAttrValueRelList.size()>0) {
			for(PrdProductAttrValueRel entity:prdProductAttrValueRelList) {
				//外键设置
				entity.setProductId(prdProduct.getId());
				prdProductAttrValueRelMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(PrdProduct prdProduct,List<PrdProductAttrValueRel> prdProductAttrValueRelList) {
		prdProductMapper.updateById(prdProduct);
		
		//1.先删除子表数据
		prdProductAttrValueRelMapper.deleteByMainId(prdProduct.getId().toString());
		
		//2.子表数据重新插入
		if(prdProductAttrValueRelList!=null && prdProductAttrValueRelList.size()>0) {
			for(PrdProductAttrValueRel entity:prdProductAttrValueRelList) {
				//外键设置
				entity.setProductId(prdProduct.getId());
				prdProductAttrValueRelMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		prdProductAttrValueRelMapper.deleteByMainId(id);
		prdProductMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			prdProductAttrValueRelMapper.deleteByMainId(id.toString());
			prdProductMapper.deleteById(id);
		}
	}
	
}
