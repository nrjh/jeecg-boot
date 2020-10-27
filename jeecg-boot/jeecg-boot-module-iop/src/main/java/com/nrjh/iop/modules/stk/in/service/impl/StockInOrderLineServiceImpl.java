package com.nrjh.iop.modules.stk.in.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.order.vo.ProdAndStockVo;
import com.nrjh.iop.modules.stk.in.entity.OrderStockInList;
import com.nrjh.iop.modules.stk.in.entity.StockInOrderLine;
import com.nrjh.iop.modules.stk.in.mapper.StockInOrderLineMapper;
import com.nrjh.iop.modules.stk.in.service.IOrderStockInListService;
import com.nrjh.iop.modules.stk.in.service.IStockInOrderLineService;
import com.nrjh.iop.modules.stk.in.vo.StockInOrderLineVo;
import com.nrjh.iop.modules.stk.stkscrap.entity.ScrapPro;
import com.nrjh.iop.modules.stk.stkscrap.service.IScrapProService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 入库明细
 * @Author: jeecg-boot
 * @Date: 2020-08-16
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StockInOrderLineServiceImpl extends ServiceImpl<StockInOrderLineMapper, StockInOrderLine> implements IStockInOrderLineService {

    @Autowired
    private StockInOrderLineMapper stockInOrderLineMapper;

    @Autowired
    private IOrderStockInListService orderStockInListService;

    @Autowired
    private IScrapProService scrapProService;

    @Override
    public List<StockInOrderLineVo> selectByMainId(String mainId) {
        List<StockInOrderLineVo> voList = new ArrayList<>();
        List<StockInOrderLine> list = stockInOrderLineMapper.selectByMainId(mainId);
        for (StockInOrderLine entity : list) {
            StockInOrderLineVo vo = new StockInOrderLineVo();
            BeanUtils.copyProperties(entity, vo);
            vo.setOrderStockInLists(orderStockInListService.selectByMainId(String.valueOf(entity.getId())));
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public List<ProdAndStockVo> selectStockByMainId(String mainId) {
        List<ProdAndStockVo> result=new ArrayList<>();
        List<StockInOrderLine> list = stockInOrderLineMapper.selectByMainId(mainId);
        for (StockInOrderLine entity : list) {
            System.out.println(entity.getPartnerId());
            ProdAndStockVo prodAndStockVo=new ProdAndStockVo();
            BeanUtils.copyProperties(entity, prodAndStockVo);
            ScrapPro scrapPro=new ScrapPro();
            //获取实际入库数量
            prodAndStockVo.setProductPlyQty(entity.getProductQty() == null ? 0 : entity.getProductQty().intValue());
            //获取应入库数量
            prodAndStockVo.setProductActualQty(entity.getProductActualQty() == null ? 0 : entity.getProductActualQty().intValue());
            scrapPro.setProductId(entity.getProductId());
            List<ScrapPro> records=scrapProService.queryProdList(scrapPro);
            if(!CollectionUtils.isEmpty(records)){
                prodAndStockVo.setAcName(records.get(0).getAcName());
                prodAndStockVo.setUuName(records.get(0).getUuName());
                prodAndStockVo.setCateName(records.get(0).getCateName());
                prodAndStockVo.setName(records.get(0).getRpoductName());
                prodAndStockVo.setRpoductName(records.get(0).getRpoductName());
                prodAndStockVo.setProductNo(records.get(0).getProductNo());
            }
            prodAndStockVo.setPartnerName(entity.getPartnerName());
            prodAndStockVo.setProductUomQty(entity.getProductActualQty());
            prodAndStockVo.setOrderStockInLists(orderStockInListService.selectByMainId(String.valueOf(entity.getId())));
            result.add(prodAndStockVo);
        }
        return result;
    }
}
