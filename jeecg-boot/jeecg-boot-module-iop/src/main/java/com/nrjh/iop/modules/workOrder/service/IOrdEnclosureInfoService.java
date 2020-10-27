package com.nrjh.iop.modules.workOrder.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.workOrder.entity.OrdEnclosureInfo;

/**
 * @Description: 工单附件表
 * @Author: lei-li
 * @Date:   2020-09-02
 * @Version: V1.0
 */
public interface IOrdEnclosureInfoService extends IService<OrdEnclosureInfo> {

    /**
     * 通过工单号查询附件信息
     * @param workOrderId
     * @return
     */
    OrdEnclosureInfo queryEnclosureInfo(String workOrderId);
}
