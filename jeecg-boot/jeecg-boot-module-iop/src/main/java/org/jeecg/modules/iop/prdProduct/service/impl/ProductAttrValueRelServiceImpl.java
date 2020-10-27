package org.jeecg.modules.iop.prdProduct.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.iop.prdProduct.entity.ProductAttrValueRel;
import org.jeecg.modules.iop.prdProduct.mapper.ProductAttrValueRelMapper;
import org.jeecg.modules.iop.prdProduct.service.IProductAttrValueRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 备品备件属性值关系
 * @Author: jeecg-boot
 * @Date:   2020-08-06
 * @Version: V1.0
 */
@Service
public class ProductAttrValueRelServiceImpl extends ServiceImpl<ProductAttrValueRelMapper, ProductAttrValueRel> implements IProductAttrValueRelService {
	
	@Autowired
	private ProductAttrValueRelMapper productAttrValueRelMapper;
	
	@Override
	public List<ProductAttrValueRel> selectByMainId(String mainId) {
		return productAttrValueRelMapper.selectByMainId(mainId);
	}
}
