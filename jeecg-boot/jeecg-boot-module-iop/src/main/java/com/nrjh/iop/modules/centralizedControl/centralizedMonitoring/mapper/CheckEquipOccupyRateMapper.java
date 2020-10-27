package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.mapper;

import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto.CentralizedControlMonitorDTO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.CheckEquipOccupyRateVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/28 11:10
 */
@Repository
public interface CheckEquipOccupyRateMapper {
    /**
     * 按小时获取没有单向、三相
     * @param dto
     * @return
     */
    List<CheckEquipOccupyRateVO> getNotOne(@Param("dto") CentralizedControlMonitorDTO dto);

    /**
     * 根据设备类型查询，根据设备规格分组
     * @param dto
     * @return
     */
    List<CheckEquipOccupyRateVO> getByEquipType(@Param("dto") CentralizedControlMonitorDTO dto);

    /**
     * 根据设备类型 - 设备规格 查询
     * @param dto
     * @return
     */
    List<CheckEquipOccupyRateVO> getByEquipTypeAndCateg(@Param("dto") CentralizedControlMonitorDTO dto);

    /**
     * 按小时获取有单向、三相
     * @param dto
     * @return
     */
    List<CheckEquipOccupyRateVO> getHaveOne(@Param("dto") CentralizedControlMonitorDTO dto);

}
