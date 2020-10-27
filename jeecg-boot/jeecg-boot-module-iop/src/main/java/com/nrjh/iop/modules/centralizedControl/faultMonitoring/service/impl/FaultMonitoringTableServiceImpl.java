package com.nrjh.iop.modules.centralizedControl.faultMonitoring.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.mapper.FaultMonitoringTableMapper;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.service.FaultMonitoringService;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.service.FaultMonitoringTableService;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringTableResultVo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-10-08 18:31
 */
@Service
@DS("ora")
public class FaultMonitoringTableServiceImpl implements FaultMonitoringTableService{

    private final static String FAULTMONITORING_CLASSIFY_DAY = "day";
    private final static String FAULTMONITORING_CLASSIFY_MONTH = "month";

    @Autowired
    private FaultMonitoringTableMapper faultMonitoringTableMapper;

    @Autowired
    private FaultMonitoringServiceImpl faultMonitoringService;

    @Override
    public List<FaultMonitoringTableResultVo> getFaultTypeTableList(FaultMonitoringVo faultMonitoringVo) {

        Date startDate = faultMonitoringVo.getStartDate();
        Date endDate = faultMonitoringVo.getEndDate();
        //按天或月获取一段时间内数据
        if(FAULTMONITORING_CLASSIFY_DAY.equals(faultMonitoringVo.getClassify())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startTime = sdf.format(startDate);
            String endTime = sdf.format(endDate);
            List<FaultMonitoringTableResultVo> voList = faultMonitoringTableMapper.getFaultTypeTableDataByDay(startTime,endTime,faultMonitoringVo);
            for(int i = 0;i<voList.size();i=i+3){
                String lineName1 = voList.get(i).getLineName();
                String lineName2 = voList.get(i+1).getLineName();
                String lineName3 = voList.get(i+2).getLineName();
                String faultLabe1 = voList.get(i).getFaultLabe();
                String faultLabe2 = voList.get(i+1).getFaultLabe();
                //判断检定线有几种报警类型，补全另外一种，并去掉其他故障类型数据
                if(!(lineName1.equals(lineName2))){
                    FaultMonitoringTableResultVo fmtVo = new FaultMonitoringTableResultVo();
                    if("其他故障".equals(faultLabe1)){
                        fmtVo.setFaultLabe("机械故障");
                        fmtVo.setLineName(lineName1);
                        fmtVo.setFrequency(0);
                        voList.add(i,fmtVo);
                        fmtVo.setFaultLabe("电测故障");
                        voList.add(i,fmtVo);
                        //删除其他故障所在行数据
                        voList.remove(i+2);
                    }else{
                        fmtVo.setFaultLabe(("机械故障".equals(faultLabe1) ? "电测故障" : "机械故障"));
                        fmtVo.setLineName(lineName1);
                        fmtVo.setFrequency(0);
                    }
                }else if(isEquals(lineName1,lineName2) && !isEquals(lineName1,lineName2,lineName3)){
                    if("其他故障".equals(faultLabe1) || "其他故障".equals(faultLabe2)){
                        FaultMonitoringTableResultVo fmtVo = new FaultMonitoringTableResultVo();
                        fmtVo.setFaultLabe(("机械故障".equals(faultLabe1) || "机械故障".equals(faultLabe2)) ? "电测故障" : "机械故障");
                        fmtVo.setLineName(lineName1);
                        fmtVo.setFrequency(0);
                        voList.add(i,fmtVo);
                        //删除其他故障所在行数据
                        if("其他故障".equals(faultLabe1)){
                            voList.remove(i+1);
                        }else {
                            voList.remove(i+2);
                        }
                    }
                }else{
                    //有三种故障类型删除其他故障所在行数据
                    if("其他故障".equals(faultLabe1)){
                        voList.remove(i);
                    }else if("其他故障".equals(faultLabe2)){
                        voList.remove(i+1);
                    }else{
                        voList.remove(i+2);
                    }
                }
                //根据检定线名称获取在此时间段内的总故障频率，并插入一行数据
                FaultMonitoringTableResultVo faultMonitoringTableResultVo = new FaultMonitoringTableResultVo();
                faultMonitoringTableResultVo.setLineName(lineName1);
                Integer totalFrequency = faultMonitoringTableMapper.getTotalFrequency(lineName1,startTime,endTime);
                faultMonitoringTableResultVo.setFrequency(totalFrequency);
                voList.add(i,faultMonitoringTableResultVo);
            }
            return voList;
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String startTime = sdf.format(startDate);
            String endTime = sdf.format(endDate);
            List<FaultMonitoringTableResultVo> voList = faultMonitoringTableMapper.getFaultTypeTableDataByMonth(startTime,endTime,faultMonitoringVo);
            for(int i = 0;i<voList.size();i=i+3){
                String lineName1 = voList.get(i).getLineName();
                String lineName2 = voList.get(i+1).getLineName();
                String lineName3 = voList.get(i+2).getLineName();
                String faultLabe1 = voList.get(i).getFaultLabe();
                String faultLabe2 = voList.get(i+1).getFaultLabe();
                String faultLabe3 = voList.get(i+2).getFaultLabe();
                //判断检定线有几种报警类型，补全另外一种，并去掉其他故障类型数据
                if(!(lineName1.equals(lineName2))){
                    FaultMonitoringTableResultVo fmtVo = new FaultMonitoringTableResultVo();
                    if("其他故障".equals(faultLabe1)){
                        fmtVo.setFaultLabe("机械故障");
                        fmtVo.setLineName(lineName1);
                        fmtVo.setFrequency(0);
                        voList.add(i,fmtVo);
                        fmtVo.setFaultLabe("电测故障");
                        voList.add(i,fmtVo);
                        //删除其他故障所在行数据
                        voList.remove(i+2);
                    }else{
                        fmtVo.setFaultLabe(("机械故障".equals(faultLabe1) ? "电测故障" : "机械故障"));
                        fmtVo.setLineName(lineName1);
                        fmtVo.setFrequency(0);
                    }
                }else if(isEquals(lineName1,lineName2) && !isEquals(lineName1,lineName2,lineName3)){
                    if("其他故障".equals(faultLabe1) || "其他故障".equals(faultLabe2)){
                        FaultMonitoringTableResultVo fmtVo = new FaultMonitoringTableResultVo();
                        fmtVo.setFaultLabe(("机械故障".equals(faultLabe1) || "机械故障".equals(faultLabe2)) ? "电测故障" : "机械故障");
                        fmtVo.setLineName(lineName1);
                        fmtVo.setFrequency(0);
                        voList.add(i,fmtVo);
                        //删除其他故障所在行数据
                        if("其他故障".equals(faultLabe1)){
                            voList.remove(i+1);
                        }else {
                            voList.remove(i+2);
                        }
                    }
                }else{
                    //有三种故障类型删除其他故障所在行数据
                    if("其他故障".equals(faultLabe1)){
                        voList.remove(i);
                    }else if("其他故障".equals(faultLabe2)){
                        voList.remove(i+1);
                    }else{
                        voList.remove(i+2);
                    }
                }
                //根据检定线名称获取在此时间段内的总故障频率，并插入一行数据
                FaultMonitoringTableResultVo faultMonitoringTableResultVo = new FaultMonitoringTableResultVo();
                faultMonitoringTableResultVo.setLineName(lineName1);
                Integer totalFrequency = faultMonitoringTableMapper.getTotalFrequency(lineName1,startTime,endTime);
                faultMonitoringTableResultVo.setFrequency(totalFrequency);
                voList.add(i,faultMonitoringTableResultVo);
            }
            return voList;
        }
    }

    @Override
    public List<FaultMonitoringTableResultVo> getFaultEquipmentAlarmTableList(FaultMonitoringVo faultMonitoringVo) {
        Date startDate = faultMonitoringVo.getStartDate();
        Date endDate = faultMonitoringVo.getEndDate();
        List<FaultMonitoringTableResultVo> faultMonitoringTableResultVos = new ArrayList<>();
        if(FAULTMONITORING_CLASSIFY_DAY.equals(faultMonitoringVo.getClassify())){
            SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
            String startTime = sft.format(startDate);
            String endTime = sft.format(endDate);
            //按天获取选中天内故障故障报警表记录
            faultMonitoringTableResultVos= faultMonitoringTableMapper.getFaultMonitoringTableListByDay(startTime,endTime,faultMonitoringVo);

        }else if(FAULTMONITORING_CLASSIFY_MONTH.equals(faultMonitoringVo.getClassify())){
            SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM");
            String startTime = sft.format(startDate);
            String endTime = sft.format(endDate);
            //按月获取选中天内故障故障报警表记录
            faultMonitoringTableResultVos= faultMonitoringTableMapper.getFaultMonitoringTableListByMonth(startTime,endTime,faultMonitoringVo);
        }
        return faultMonitoringTableResultVos;
    }


    /** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
            return false;
        } else {
            return true;
        }
    }
    /** 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true */
    public static boolean isEquals(String... agrs) {
        String last = null;
        for (int i = 0; i < agrs.length; i++) {
            String str = agrs[i];
            if (isEmpty(str)) {
                return false;
            }
            if (last != null && !str.equalsIgnoreCase(last)) {
                return false;
            }
            last = str;
        }
        return true;
    }


}
