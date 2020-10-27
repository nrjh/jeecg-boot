package com.nrjh.iop.modules.stk.spareparts.mapper;

import java.util.List;

import com.nrjh.iop.modules.stk.spareparts.entity.StkStock;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 库存信息表
 * @Author: jeecg-boot
 * @Date:   2020-08-20
 * @Version: V1.0
 */
public interface StkStockMapper extends BaseMapper<StkStock> {

    //通过合同id查询合同号
    String queryProductName(int productLotId);

    //通过物料编号查询物料信息列表
    List<StkStock> queryStockList(String productNo);

}
