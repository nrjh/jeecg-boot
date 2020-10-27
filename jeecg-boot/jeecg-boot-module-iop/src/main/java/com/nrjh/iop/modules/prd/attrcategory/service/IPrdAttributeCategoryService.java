package com.nrjh.iop.modules.prd.attrcategory.service;

import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttrCategAttrValueRel;
import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttributeCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 规格
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
public interface IPrdAttributeCategoryService extends IService<PrdAttributeCategory> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(PrdAttributeCategory prdAttributeCategory, List<PrdAttrCategAttrValueRel> prdAttrCategAttrValueRelList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PrdAttributeCategory prdAttributeCategory, List<PrdAttrCategAttrValueRel> prdAttrCategAttrValueRelList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);

	public List<PrdAttributeCategory> selectAttrCategoryListByCategoryId( String categoryId);

	/**
	 *
	 */
	PrdAttributeCategory selectAttributeByProductId(String productId);
}
