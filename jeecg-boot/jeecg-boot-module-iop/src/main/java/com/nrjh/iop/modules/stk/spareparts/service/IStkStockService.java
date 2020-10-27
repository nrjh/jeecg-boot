package com.nrjh.iop.modules.stk.spareparts.service;

import com.nrjh.iop.modules.stk.spareparts.entity.StkStock;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 库存信息表
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
public interface IStkStockService extends IService<StkStock> {

    //通过合同id查询合同号
    String queryProductName(int productLotId);

    //通过物料编号查询物料信息列表
    List<StkStock> queryStockList(String productNo);

    // 根据物料id获取库存
    Integer productStockQty(Integer productId);
}
