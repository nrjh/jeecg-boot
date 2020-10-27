package com.nrjh.iop.modules.workOrder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.workOrder.entity.OrdSuspend;

/**
 * @Description: 工单挂单表
 * @Author: lei-li
 * @Date:   2020-09-03
 * @Version: V1.0
 */
public interface IOrdSuspendService extends IService<OrdSuspend> {

    void Suspend(OrdSuspend ordSuspend);
}
