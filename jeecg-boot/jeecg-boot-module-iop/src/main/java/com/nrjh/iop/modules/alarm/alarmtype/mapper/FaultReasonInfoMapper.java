package com.nrjh.iop.modules.alarm.alarmtype.mapper;


import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultReasonInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultSopInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 故障原因
 * @Author: jeecg-boot
 * @Date:   2020-09-18
 * @Version: V1.0
 */
public interface FaultReasonInfoMapper extends BaseMapper<FaultReasonInfo> {
    public boolean deleteByMainId(@Param("mainId") String mainId);

    public List<FaultReasonInfo> selectByMainId(@Param("mainId") String mainId);

    List<FaultReasonInfo> errorReasonNameAndId(String lineNO,String productId);
}
