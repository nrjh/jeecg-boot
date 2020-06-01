package com.nrjh.iop.modules.prd.product.service;

import com.nrjh.iop.modules.prd.product.entity.PrdProductAttrValueRel;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 物品属性值关系
 * @Author: jeecg-boot
 * @Date:   2020-05-06
 * @Version: V1.0
 */
public interface IPrdProductAttrValueRelService extends IService<PrdProductAttrValueRel> {

	public List<PrdProductAttrValueRel> selectByMainId(String mainId);
}
