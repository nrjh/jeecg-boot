package com.nrjh.iop.modules.workOrder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nrjh.iop.modules.alarm.alarmtype.vo.EquipIdModel;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.workOrder.entity.SysUser;
import com.nrjh.iop.modules.workOrder.vo.OrdWorkOrderEnclosureInfoVo;
import com.nrjh.iop.modules.workOrder.vo.OrdWorkOrderMenuVO;

import java.util.List;
import java.util.Map;

/**
 * @Description: 故障运维工单表
 * @Author: jeecg-boot
 * @Date:   2020-09-01
 * @Version: V1.0
 */
public interface IOrdWorkOrderInfoService extends IService<OrdWorkOrderInfo> {

    /**
     * 新增工单信息
     * @param ordWorkOrderEnclosureInfoVo
     */
    void saveWorkOrderInfo(OrdWorkOrderEnclosureInfoVo ordWorkOrderEnclosureInfoVo);

    /**
     * 派工信息保存
     * @param ordWorkOrderInfo
     */
    void saveDispatchingInfo(OrdWorkOrderInfo ordWorkOrderInfo);

    /**
     * 根据设备id查询设备编号
     * @param id
     * @return
     */
    String getEquipNo(String id);

    /**
     * 根据工单id查询报警信息
     */
    OrdWorkOrderMenuVO getAlarmInfo(Integer ordWorkId);

    /**
     * 工单详情信息
     */
    OrdWorkOrderMenuVO getOrdWorkOrderDetail(String ordWorkId);



    /**
     * 转派工单信息
     */
    void saveTransferInfo(OrdWorkOrderInfo ordWorkOrderInfo, SysUser sysUser);

    /**
     * 多条件组合查询
     * @param page
     * @param dataMap
     * @return
     */
    IPage<OrdWorkOrderInfo> page(IPage<OrdWorkOrderInfo> page,Map<String,String> dataMap);

    /**
     * 删除表数据
     * @param id
     */
    void deleteById(String id);
}
