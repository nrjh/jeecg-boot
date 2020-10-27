package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.dto.CountAndTaskDTO;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.DIoTask;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.MdIoTask;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/10 19:12
 */
public interface DIoTaskMapper  extends BaseMapper<DIoTask> {
    List<DIoTask> getAllDIotaskInfo(@Param("params") Map<String,Object> params);
    List<MdIoTask> getListByRelaNo(@Param("relaNo")String relaNo);
//    @MapKey("ioTaskNo")
    @MapKey("IO_TASK_NO")
    List<CountAndTaskDTO> getCountAndTaskNo();
}
