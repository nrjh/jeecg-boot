package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.UDistTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: U_DIST_TASK
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
public interface UDistTaskMapper extends BaseMapper<UDistTask> {
    List<UDistTask> getAll(@Param("params") Map<String,Object> params);
}
