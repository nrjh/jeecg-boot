package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.serivce;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto.CentralizedControlMonitorDTO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.DetectTaskListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/29 15:11
 */
@DS("ora")
public interface DetectTaskListService {
    /**
     * 根据条件进行列表筛选（包含、小时、天数、月份）
     * @param dto
     * @return
     */
    List<DetectTaskListVO> getDetectTaskList(@Param("dto") CentralizedControlMonitorDTO dto);
}
