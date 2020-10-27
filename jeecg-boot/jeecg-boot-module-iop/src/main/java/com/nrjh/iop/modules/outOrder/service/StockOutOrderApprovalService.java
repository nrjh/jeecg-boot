package com.nrjh.iop.modules.outOrder.service;

import com.nrjh.iop.modules.order.entity.StkSuggest;

public interface StockOutOrderApprovalService {
    /**
     * 审批完成
     * @param stkSuggest
     */
    void completeStockApproval(StkSuggest stkSuggest);
}
