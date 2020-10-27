package com.nrjh.iop.modules.order.service;

import com.nrjh.iop.modules.order.entity.PcsOrderLine;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 采购明细
 * @Author: jeecg-boot
 * @Date:   2020-08-15
 * @Version: V1.0
 */
public interface IPcsOrderLineService extends IService<PcsOrderLine> {

	public List<PcsOrderLine> selectByMainId(String mainId);
}
