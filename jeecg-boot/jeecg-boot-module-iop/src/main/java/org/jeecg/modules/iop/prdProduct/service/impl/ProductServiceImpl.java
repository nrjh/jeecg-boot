package org.jeecg.modules.iop.prdProduct.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.iop.prdProduct.entity.Product;
import org.jeecg.modules.iop.prdProduct.entity.ProductAttrValueRel;
import org.jeecg.modules.iop.prdProduct.mapper.ProductAttrValueRelMapper;
import org.jeecg.modules.iop.prdProduct.mapper.ProductMapper;
import org.jeecg.modules.iop.prdProduct.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 备品备件
 * @Author: jeecg-boot
 * @Date:   2020-08-06
 * @Version: V1.0
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private ProductAttrValueRelMapper productAttrValueRelMapper;
	
	@Override
	@Transactional
	public void saveMain(Product product, List<ProductAttrValueRel> productAttrValueRelList) {
		productMapper.insert(product);
		if(productAttrValueRelList!=null && productAttrValueRelList.size()>0) {
			for(ProductAttrValueRel entity:productAttrValueRelList) {
				//外键设置
				entity.setProductId(product.getId());
				productAttrValueRelMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(Product product,List<ProductAttrValueRel> productAttrValueRelList) {
		productMapper.updateById(product);
		
		//1.先删除子表数据
		productAttrValueRelMapper.deleteByMainId(product.getId()+"");
		
		//2.子表数据重新插入
		if(productAttrValueRelList!=null && productAttrValueRelList.size()>0) {
			for(ProductAttrValueRel entity:productAttrValueRelList) {
				//外键设置
				entity.setProductId(product.getId());
				productAttrValueRelMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		productAttrValueRelMapper.deleteByMainId(id);
		productMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			productAttrValueRelMapper.deleteByMainId(id.toString());
			productMapper.deleteById(id);
		}
	}
	
}
