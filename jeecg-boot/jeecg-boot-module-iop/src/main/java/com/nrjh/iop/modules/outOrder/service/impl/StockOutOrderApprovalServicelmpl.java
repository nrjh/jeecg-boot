package com.nrjh.iop.modules.outOrder.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nrjh.iop.modules.order.entity.StkSuggest;
import com.nrjh.iop.modules.order.service.IPcsPurchaseOrderService;
import com.nrjh.iop.modules.order.service.IStkSuggestService;
import com.nrjh.iop.modules.outOrder.entity.StockOutOrder;
import com.nrjh.iop.modules.outOrder.mapper.StockOutOrderApprovalMapper;
import com.nrjh.iop.modules.outOrder.mapper.StockOutOrderMapper;
import com.nrjh.iop.modules.outOrder.service.StockOutOrderApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-08-24 16:03
 */
@Service
@DS("iop")
public class StockOutOrderApprovalServicelmpl implements StockOutOrderApprovalService {
    @Autowired
    private StockOutOrderApprovalMapper stockOutOrderApprovalMapper;

    @Autowired
    private IStkSuggestService stkSuggestService;

    @Autowired
    private StockOutOrderMapper stockOutOrderMapper;
    @Autowired
    private IPcsPurchaseOrderService iPcsPurchaseOrderService;

    @Override
    @Transactional
    public void completeStockApproval(StkSuggest stkSuggest) {
        //查询审批表数据条数
//        QueryWrapper<StkSuggest> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("order_no", stkSuggest.getOrderNo());
//        queryWrapper.eq("result","pass");
//        List<StkSuggest> list = stkSuggestService.list(queryWrapper);
        QueryWrapper<StockOutOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_out_id",stkSuggest.getOrderNo());
        List<StockOutOrder> result= stockOutOrderMapper.selectList(queryWrapper);
        String orderType="";
        String nextRoleId="";
        int businessSequence=0;
        if(!CollectionUtil.isEmpty(result)){
            if(!StringUtils.isEmpty(result.get(0).getOrderType()) && result.get(0).getOrderType().equals("urgency")){
                orderType="urgency";
            }else {
                orderType="normal";
            }
             businessSequence=result.get(0).getBusinessSequence()+1;
             nextRoleId=iPcsPurchaseOrderService.getBusinessApproveNextRoleId(businessSequence,"1002",orderType);
        }
        //设置审核结果,插入数据
        stockOutOrderApprovalMapper.insert(stkSuggest);
        QueryWrapper<StockOutOrder> stockOutOrderQueryWrapper = new QueryWrapper<StockOutOrder>();
        StockOutOrder stockOutOrder = new StockOutOrder();
        //退回改状态
        if("reject".equals(stkSuggest.getResult())){
            stockOutOrderQueryWrapper.eq("order_out_id", stkSuggest.getOrderNo());
            stockOutOrder.setState("back");
            stockOutOrder.setNextRoleId("99");
            stockOutOrder.setBusinessSequence(businessSequence);
            stockOutOrderMapper.update(stockOutOrder,stockOutOrderQueryWrapper);
        }else {
            //审批没有下一个角色了 审批流程结束
            if(StringUtils.isEmpty(nextRoleId)){
                stockOutOrderQueryWrapper.eq("order_out_id", stkSuggest.getOrderNo());
                stockOutOrder.setState("approve");
                stockOutOrder.setNextRoleId("99");
                stockOutOrder.setBusinessSequence(businessSequence);
                stockOutOrderMapper.update(stockOutOrder,stockOutOrderQueryWrapper);
            }else {
                stockOutOrderQueryWrapper.eq("order_out_id", stkSuggest.getOrderNo());
                stockOutOrder.setNextRoleId(nextRoleId);
                stockOutOrder.setBusinessSequence(businessSequence);
                stockOutOrderMapper.update(stockOutOrder,stockOutOrderQueryWrapper);
            }

        }
        //判断是否二次审批，更改出库单状态
//        if(list.size()>0){
//            stockOutOrderQueryWrapper.eq("order_out_id", stkSuggest.getOrderNo());
//            stockOutOrder.setState("approve");
//            stockOutOrderMapper.update(stockOutOrder,stockOutOrderQueryWrapper);
//        }
    }
}
