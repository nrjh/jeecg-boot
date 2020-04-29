package com.nrjh.iop.modules.uom.category.service;

import com.nrjh.iop.modules.uom.category.entity.UomUom;
import com.nrjh.iop.modules.uom.category.entity.UomCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 计量单位类别
 * @Author: jeecg-boot
 * @Date:   2020-04-29
 * @Version: V1.0
 */
public interface IUomCategoryService extends IService<UomCategory> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(UomCategory uomCategory, List<UomUom> uomUomList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(UomCategory uomCategory, List<UomUom> uomUomList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
