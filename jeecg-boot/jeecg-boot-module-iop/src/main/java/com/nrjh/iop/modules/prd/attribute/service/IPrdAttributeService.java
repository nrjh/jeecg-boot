package com.nrjh.iop.modules.prd.attribute.service;

import com.nrjh.iop.modules.prd.attribute.entity.PrdAttributeValue;
import com.nrjh.iop.modules.prd.attribute.entity.PrdAttribute;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 属性
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
public interface IPrdAttributeService extends IService<PrdAttribute> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(PrdAttribute prdAttribute, List<PrdAttributeValue> prdAttributeValueList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PrdAttribute prdAttribute, List<PrdAttributeValue> prdAttributeValueList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
