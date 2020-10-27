package com.nrjh.iop.modules.stk.stkscrap.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.stk.spareparts.entity.StkStock;
import com.nrjh.iop.modules.stk.stkscrap.entity.ScrapPro;
import org.jeecg.modules.online.config.exception.BusinessException;

import java.util.List;

/**
 * @Description: 报废管理---物料信息
 * @Author: chenchao
 * @Date:   2020-08-07
 * @Version: V1.0
 */
public interface ScrapProMapper extends BaseMapper<ScrapPro> {

    //查询物料信息列表
    List<ScrapPro> queryProdList(ScrapPro scrapPro);

    //保存物料信息
    void saveProdList(ScrapPro scrapPro);

    //修改库存数量
    Boolean updateStockQty(String prdNo,Integer realQty,Integer virtualQty);

    //通过物料编号查询库存信息
    List<StkStock> queryStockList(String prdNo);

    //查看物料信息
    List<ScrapPro> lookScrapPro(String scrapInventoryOrder);

    //提交物料信息
    List<ScrapPro> submitScrapPro(String scrapInventoryOrder);

    //确认
    List<ScrapPro> sureScrapPro(String scrapInventoryOrder);

    //审核
    List<ScrapPro> shenheScrapPro(String scrapInventoryOrder);

    //作废
    List<ScrapPro> badScrapPro(String scrapInventoryOrder);
}
