package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.dto.DevMonitorVo;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.DevMonitorInfo;

import java.util.List;

/**
 * @Description: 监控设备管理
 * @Author: jeecg-boot
 * @Date:   2020-09-11
 * @Version: V1.0
 */
public interface IDevMonitorInfoService extends IService<DevMonitorInfo> {

  List<DevMonitorVo>  queryDevList(String lineNo);

}
