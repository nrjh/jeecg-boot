package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper;

import java.util.List;

import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.dto.DevMonitorVo;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.DevMonitorInfo;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 监控设备管理
 * @Author: jeecg-boot
 * @Date:   2020-09-11
 * @Version: V1.0
 */
public interface DevMonitorInfoMapper extends BaseMapper<DevMonitorInfo> {
    List<DevMonitorVo>  queryDevList(String lineNo);

}
