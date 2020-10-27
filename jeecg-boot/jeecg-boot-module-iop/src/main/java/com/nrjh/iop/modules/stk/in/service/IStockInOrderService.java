package com.nrjh.iop.modules.stk.in.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nrjh.iop.modules.order.entity.StkSuggest;
import com.nrjh.iop.modules.stk.in.entity.StockInOrderLine;
import com.nrjh.iop.modules.stk.in.entity.StockInOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderVo;
import org.jeecg.common.constant.CacheConstant;
import org.springframework.cache.annotation.Cacheable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 入库单
 * @Author: jeecg-boot
 * @Date: 2020-08-16
 * @Version: V1.0
 */
public interface IStockInOrderService extends IService<StockInOrder> {

    /**
     * 添加一对多
     */
    public void saveMain(StockInOrderVo stockInOrderVo);

    /**
     * 修改一对多
     */
    public void updateMain(StockInOrderVo stockInOrderVo);

    /**
     * 删除一对多
     */
    public void delMain(String id);

    /**
     * 批量删除一对多
     */
    public void delBatchMain(Collection<? extends Serializable> idList);


    /**
     * 多表查询
     */
    List<StockInOrder> pageByInOrderAndDetail(Map map);


    /**
     * 保存审批
     *
     * @param stkSuggest
     */
    public void saveSuggest(StkSuggest stkSuggest);

    public String queryDictTextByKey(String code, String key);
}
