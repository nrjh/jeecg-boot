package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.mapper;

import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto.CentralizedControlMonitorDTO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.CheckPassRateVO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.PartOperationStatusVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/27 17:14
 */
@Repository
public interface CheckPassRateMapper {
    /**
     * 按小时获取
     * @param dto
     * @return
     */
    List<CheckPassRateVO> getByHours(@Param("dto") CentralizedControlMonitorDTO dto);

    /**
     * 按天数获取
     * @param dto
     * @return
     */
    List<CheckPassRateVO> getByDays(@Param("dto") CentralizedControlMonitorDTO dto);

    /**
     * 按月份获取
     * @param dto
     * @return
     */
    List<CheckPassRateVO> getByMonths(@Param("dto") CentralizedControlMonitorDTO dto);

    /**
     * 以小时搜索时若有设备规格
     * @param dto
     * @return
     */
    List<PartOperationStatusVO> getTaskCountEquipSpecCodeWithHours(@Param("dto") CentralizedControlMonitorDTO dto);

    /**
     * 以天数搜索时若有设备规格
     * @param dto
     * @return
     */
    List<PartOperationStatusVO> getTaskCountEquipSpecCodeWithDays(@Param("dto") CentralizedControlMonitorDTO dto);
    /**
     * 以月份搜索时若有设备规格
     * @param dto
     * @return
     */
    List<PartOperationStatusVO> getTaskCountEquipSpecCodeWithMonths(@Param("dto") CentralizedControlMonitorDTO dto);


}
