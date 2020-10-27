package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.MtDetectTaskInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: 检定任务信息(新平台)实体
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
public interface MtDetectTaskInfoMapper extends BaseMapper<MtDetectTaskInfo> {
    List<MtDetectTaskInfo> getAll(@Param("params") Map<String,Object> params);
    List<MtDetectTaskInfo> checkTaskMonitorList(@Param("params") Map<String, Object> params);
}
