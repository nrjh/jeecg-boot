package com.nrjh.iop.modules.stk.stkscrap.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.stk.stkscrap.entity.ScrapPro;
import com.nrjh.iop.modules.stk.stkscrap.entity.StkScrap;
import org.jeecg.modules.online.config.exception.BusinessException;

import java.util.List;

/**
 * @Description: 查询物料
 * @Author: 陈超
 * @Date:   2020-08-07
 * @Version: V1.0
 */
public interface IScrapProService extends IService<ScrapPro> {

    //查询物料信息列表
    List<ScrapPro> queryProdList(ScrapPro scrapPro);

    //保存物料信息
    void saveProdList(ScrapPro scrapPro);

    //查看物料信息
    List<ScrapPro> lookScrapPro(String scrapInventoryOrder);

    //提交物料信息
    Boolean submitScrapPro(StkScrap stkScrap);

    //确认单据信息
    Boolean sureScrapPro(String id) throws BusinessException;

    //审核单据
    Boolean shenheScrapPro(String id,String info) throws BusinessException;

    //作废单据
    Boolean badScrapPro(String id) throws BusinessException;

    //修改库存数量
    Boolean updateStockQty(String prdNo,Integer realQty,Integer virtualQty)throws BusinessException;

}
