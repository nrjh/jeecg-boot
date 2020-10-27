package com.nrjh.iop.modules.alarm.alarmtype.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultRepairInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 修复方法
 * @Author: jeecg-boot
 * @Date:   2020-08-27
 * @Version: V1.0
 */
public interface FaultRepairInfoMapper extends BaseMapper<FaultRepairInfo> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<FaultRepairInfo> selectByMainId(@Param("mainId") String mainId);
}
