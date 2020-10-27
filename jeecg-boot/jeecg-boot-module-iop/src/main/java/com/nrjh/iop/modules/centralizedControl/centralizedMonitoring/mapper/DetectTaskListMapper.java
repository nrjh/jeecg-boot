package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.mapper;

import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto.CentralizedControlMonitorDTO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.DetectTaskListVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/29 14:53
 */
@Repository
public interface DetectTaskListMapper {
    /**
     * 根据条件进行列表筛选（包含、小时、天数、月份）
     * @param dto
     * @return
     */
    List<DetectTaskListVO> getDetectTaskList(@Param("dto") CentralizedControlMonitorDTO dto);
}
