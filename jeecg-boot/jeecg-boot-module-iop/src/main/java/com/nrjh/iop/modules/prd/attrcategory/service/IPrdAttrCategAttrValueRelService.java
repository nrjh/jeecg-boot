package com.nrjh.iop.modules.prd.attrcategory.service;

import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttrCategAttrValueRel;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 物品规格与产品属性值关系
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
public interface IPrdAttrCategAttrValueRelService extends IService<PrdAttrCategAttrValueRel> {

	public List<PrdAttrCategAttrValueRel> selectByMainId(String mainId);
}
