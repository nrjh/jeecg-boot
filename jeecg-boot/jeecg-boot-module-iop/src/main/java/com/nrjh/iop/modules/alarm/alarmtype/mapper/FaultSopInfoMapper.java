package com.nrjh.iop.modules.alarm.alarmtype.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nrjh.iop.modules.alarm.alarmtype.entity.FaultSopInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 作业指导
 * @Author: jeecg-boot
 * @Date:   2020-08-27
 * @Version: V1.0
 */
public interface FaultSopInfoMapper extends BaseMapper<FaultSopInfo> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<FaultSopInfo> selectByMainId(@Param("mainId") String mainId);
}
