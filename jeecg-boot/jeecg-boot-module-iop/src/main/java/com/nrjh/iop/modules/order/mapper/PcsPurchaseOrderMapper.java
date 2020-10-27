package com.nrjh.iop.modules.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nrjh.iop.modules.order.entity.PcsPurchaseOrder;

import java.util.Map;

/**
 * @Description: 采购单
 * @Author: jeecg-boot
 * @Date:   2020-08-15
 * @Version: V1.0
 */
public interface PcsPurchaseOrderMapper extends BaseMapper<PcsPurchaseOrder> {

    IPage<PcsPurchaseOrder> selectPage(com.baomidou.mybatisplus.extension.plugins.pagination.Page<PcsPurchaseOrder> page , PcsPurchaseOrder pcsPurchaseOrder);

    IPage<PcsPurchaseOrder> selectPageOrders(IPage iPage, Map<String,Object> dataMap);

}
