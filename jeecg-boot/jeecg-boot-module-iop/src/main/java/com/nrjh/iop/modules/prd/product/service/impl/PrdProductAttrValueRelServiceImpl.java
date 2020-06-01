package com.nrjh.iop.modules.prd.product.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.prd.product.entity.PrdProductAttrValueRel;
import com.nrjh.iop.modules.prd.product.mapper.PrdProductAttrValueRelMapper;
import com.nrjh.iop.modules.prd.product.service.IPrdProductAttrValueRelService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 物品属性值关系
 * @Author: jeecg-boot
 * @Date:   2020-05-06
 * @Version: V1.0
 */
@Service
@DS("iop")
public class PrdProductAttrValueRelServiceImpl extends ServiceImpl<PrdProductAttrValueRelMapper, PrdProductAttrValueRel> implements IPrdProductAttrValueRelService {
	
	@Autowired
	private PrdProductAttrValueRelMapper prdProductAttrValueRelMapper;
	
	@Override
	public List<PrdProductAttrValueRel> selectByMainId(String mainId) {
		return prdProductAttrValueRelMapper.selectByMainId(mainId);
	}
}
