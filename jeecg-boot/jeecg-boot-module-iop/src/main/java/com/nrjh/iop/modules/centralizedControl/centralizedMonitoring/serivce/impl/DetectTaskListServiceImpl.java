package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.serivce.impl;

import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto.CentralizedControlMonitorDTO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.mapper.DetectTaskListMapper;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.serivce.DetectTaskListService;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.DetectTaskListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/29 15:12
 */
@Service
public class DetectTaskListServiceImpl implements DetectTaskListService {
    @Autowired
    private DetectTaskListMapper detectTaskListMapper;
    @Override
    public List<DetectTaskListVO> getDetectTaskList(CentralizedControlMonitorDTO dto) {
        List<DetectTaskListVO> detectTaskList = detectTaskListMapper.getDetectTaskList(dto);
        detectTaskList.forEach(d -> {
            d.setIsNormalCheck(d.getIsNormalCheck().equals("0") ? "常规检定" : "非常规检定");
        });
        if(detectTaskList.size() > 0){
            return detectTaskList;
        }
        return null;
    }
}
