package com.nrjh.iop.modules.prd.attribute.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.prd.attribute.entity.PrdAttributeValue;
import com.nrjh.iop.modules.prd.attribute.mapper.PrdAttributeValueMapper;
import com.nrjh.iop.modules.prd.attribute.service.IPrdAttributeValueService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 属性值
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
@Service
@DS("iop")
public class PrdAttributeValueServiceImpl extends ServiceImpl<PrdAttributeValueMapper, PrdAttributeValue> implements IPrdAttributeValueService {
	
	@Autowired
	private PrdAttributeValueMapper prdAttributeValueMapper;
	
	@Override
	public List<PrdAttributeValue> selectByMainId(String mainId) {
		return prdAttributeValueMapper.selectByMainId(mainId);
	}
}
