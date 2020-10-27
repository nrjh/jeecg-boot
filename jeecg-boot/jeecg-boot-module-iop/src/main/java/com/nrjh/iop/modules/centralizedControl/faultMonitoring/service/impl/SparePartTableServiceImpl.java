package com.nrjh.iop.modules.centralizedControl.faultMonitoring.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.mapper.SparePartTableMapper;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.service.SparePartTableService;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringTableResultVo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-10-09 22:56
 */
@Service
@DS("iop")
public class SparePartTableServiceImpl implements SparePartTableService {

    private final static String FAULTMONITORING_CLASSIFY_DAY = "day";
    private final static String FAULTMONITORING_CLASSIFY_MONTH = "month";

    @Autowired
    private SparePartTableMapper sparePartTableMapper;
    @Override
    public List<FaultMonitoringTableResultVo> getSparePartTableList(FaultMonitoringVo faultMonitoringVo) {

        Date startDate = faultMonitoringVo.getStartDate();
        Date endDate = faultMonitoringVo.getEndDate();
        List<FaultMonitoringTableResultVo> faultMonitoringTableResultVos = new ArrayList<>();
        if(FAULTMONITORING_CLASSIFY_DAY.equals(faultMonitoringVo.getClassify())){
            SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
            String startTime = sft.format(startDate);
            String endTime = sft.format(endDate);
            //按天获取选中天内备品备件表记录
            faultMonitoringTableResultVos= sparePartTableMapper.getSparePartTableListByDay(startTime,endTime,faultMonitoringVo);

        }else if(FAULTMONITORING_CLASSIFY_MONTH.equals(faultMonitoringVo.getClassify())){
            SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM");
            String startTime = sft.format(startDate);
            String endTime = sft.format(endDate);
            //按月获取选中天内备品备件表记录
            faultMonitoringTableResultVos= sparePartTableMapper.getSparePartTableListByMonth(startTime,endTime,faultMonitoringVo);
        }
        return faultMonitoringTableResultVos;
    }
}
