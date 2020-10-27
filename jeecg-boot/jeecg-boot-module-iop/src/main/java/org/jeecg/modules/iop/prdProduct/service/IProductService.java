package org.jeecg.modules.iop.prdProduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.iop.prdProduct.entity.Product;
import org.jeecg.modules.iop.prdProduct.entity.ProductAttrValueRel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 备品备件
 * @Author: jeecg-boot
 * @Date:   2020-08-06
 * @Version: V1.0
 */
public interface IProductService extends IService<Product> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(Product product, List<ProductAttrValueRel> productAttrValueRelList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(Product product, List<ProductAttrValueRel> productAttrValueRelList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
