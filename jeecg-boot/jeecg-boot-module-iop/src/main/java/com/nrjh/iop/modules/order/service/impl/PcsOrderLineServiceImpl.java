package com.nrjh.iop.modules.order.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.order.entity.PcsOrderLine;
import com.nrjh.iop.modules.order.mapper.PcsOrderLineMapper;
import com.nrjh.iop.modules.order.service.IPcsOrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 采购明细
 * @Author: jeecg-boot
 * @Date:   2020-08-15
 * @Version: V1.0
 */
@Service
@DS("iop")
public class PcsOrderLineServiceImpl extends ServiceImpl<PcsOrderLineMapper, PcsOrderLine> implements IPcsOrderLineService {
	
	@Autowired
	private PcsOrderLineMapper pcsOrderLineMapper;
	
	@Override
	public List<PcsOrderLine> selectByMainId(String mainId) {
		return pcsOrderLineMapper.selectByMainId(mainId);
	}
}
