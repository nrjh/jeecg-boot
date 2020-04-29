package com.nrjh.iop.modules.uom.category.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.uom.category.entity.UomUom;
import com.nrjh.iop.modules.uom.category.mapper.UomUomMapper;
import com.nrjh.iop.modules.uom.category.service.IUomUomService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 计量单位
 * @Author: jeecg-boot
 * @Date:   2020-04-29
 * @Version: V1.0
 */
@Service
@DS("iop")
public class UomUomServiceImpl extends ServiceImpl<UomUomMapper, UomUom> implements IUomUomService {
	
	@Autowired
	private UomUomMapper uomUomMapper;
	
	@Override
	public List<UomUom> selectByMainId(String mainId) {
		return uomUomMapper.selectByMainId(mainId);
	}
}
