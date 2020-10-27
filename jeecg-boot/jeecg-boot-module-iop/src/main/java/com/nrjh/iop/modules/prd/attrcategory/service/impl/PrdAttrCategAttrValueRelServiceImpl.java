package com.nrjh.iop.modules.prd.attrcategory.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nrjh.iop.modules.prd.attrcategory.entity.PrdAttrCategAttrValueRel;
import com.nrjh.iop.modules.prd.attrcategory.mapper.PrdAttrCategAttrValueRelMapper;
import com.nrjh.iop.modules.prd.attrcategory.service.IPrdAttrCategAttrValueRelService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 物品规格与产品属性值关系
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
@Service
@DS("iop")
public class PrdAttrCategAttrValueRelServiceImpl extends ServiceImpl<PrdAttrCategAttrValueRelMapper, PrdAttrCategAttrValueRel> implements IPrdAttrCategAttrValueRelService {

	@Autowired
	private PrdAttrCategAttrValueRelMapper prdAttrCategAttrValueRelMapper;

	@Override
	public List<PrdAttrCategAttrValueRel> selectByMainId(String mainId) {
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("attribute_category_id",mainId);
		return baseMapper.selectList(queryWrapper);
	}
}
