package com.nrjh.iop.modules.prd.product.service;

import com.nrjh.iop.modules.prd.product.entity.PrdProductAttrValueRel;
import com.nrjh.iop.modules.prd.product.entity.PrdProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 物品
 * @Author: jeecg-boot
 * @Date:   2020-05-06
 * @Version: V1.0
 */
public interface IPrdProductService extends IService<PrdProduct> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(PrdProduct prdProduct, List<PrdProductAttrValueRel> prdProductAttrValueRelList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PrdProduct prdProduct, List<PrdProductAttrValueRel> prdProductAttrValueRelList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
