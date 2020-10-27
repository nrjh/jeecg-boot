package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.mapper;

import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto.CentralizedControlMonitorDTO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.PrdTaskTraceVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/25 10:22
 */
@Repository
public interface PrdTaskTraceMapper {
    /**
     * 按小时获取
     * @param dto
     * @return
     */
    List<PrdTaskTraceVO> getByHours(@Param("dto") CentralizedControlMonitorDTO dto);

    /**
     * 按天数获取
     * @param dto
     * @return
     */
    List<PrdTaskTraceVO> getByDays(@Param("dto") CentralizedControlMonitorDTO dto);

    /**
     * 按月份获取
     * @param dto
     * @return
     */
    List<PrdTaskTraceVO> getByMonths(@Param("dto") CentralizedControlMonitorDTO dto);
}
