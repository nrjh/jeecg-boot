package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.strategy.specific;

import cn.hutool.core.collection.CollectionUtil;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto.CentralizedControlMonitorDTO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.mapper.CheckPassRateMapper;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.strategy.AbstractMethodOfCharts;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.CheckPassRateVO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.PartOperationStatusVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/27 17:26
 */
@Service
public class CheckPassRateCharts extends AbstractMethodOfCharts {

    @Autowired
    private CheckPassRateMapper checkPassRateMapper;

    // 任务数量
    private Integer[] taskCountArray = null;
    // x轴值的显示
    private String[] xTimeArray = null;
    // 合格率
    private Double[] passRateArray = null;
    private Map<String, List<Object>> resultMapOfChart = null;

    @Override
    public boolean isSupport(String chartsName) {
        return chartsName.equals("CheckPassRate");
    }

    @Override
    public Map<String, List<Object>> getByHours(CentralizedControlMonitorDTO dto) {
        List<CheckPassRateVO> byHours = checkPassRateMapper.getByHours(dto);
        if (!CollectionUtil.isEmpty(byHours) && byHours.size() > 0) {
            Date startDate = dto.getStartDealTime();
            resultMapOfChart = new HashMap<>();
            taskCountArray = new Integer[24];
            xTimeArray = new String[24];
            passRateArray = new Double[24];
            // 给x轴初始化
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String format = sdf.format(startDate);
            for (int i = 0; i < xTimeArray.length; i++) {
                xTimeArray[i] = format + " " + i + "时";
            }
            // 初始化为0
            taskCountArray = Arrays.stream(taskCountArray).map(t -> t = 0).toArray(Integer[]::new);
            passRateArray = Arrays.stream(passRateArray).map(a -> a = 0d).toArray(Double[]::new);

            byHours.forEach(h -> {
                // 下标
                int index = Integer.parseInt(h.getXTime());
                taskCountArray[index] = h.getTaskCount() == null ? 0 : h.getTaskCount();
                passRateArray[index] = h.getPassRate() == null ? 0d : h.getPassRate();
            });
            if (dto.getEquipSpecCode() != null) {
                // 如果是查询小时的，则不要传入结束处理时间
                // 因为只查24小时，比如18号~19号，那么只查18号当天的00:00:00 ~ 23:59:59这段时间
                if (dto.getTimeFlag().equals(1)) {
                    dto.setEndDealTime(null);
                }
                List<PartOperationStatusVO> taskCountEquipSpecCode = checkPassRateMapper.getTaskCountEquipSpecCodeWithHours(dto);
                taskCountEquipSpecCode.forEach(t -> {
                    Integer xTime = Integer.parseInt(t.getXTime());
                    taskCountArray[xTime] = t.getTaskCount() == null ? 0 : t.getTaskCount();
                });
            }
            resultMapOfChart.put("xArray", Arrays.asList(xTimeArray));
            resultMapOfChart.put("taskCountArray", Arrays.asList(taskCountArray));
            resultMapOfChart.put("passRate", Arrays.asList(passRateArray));
        }
        else{
            resultMapOfChart = null;
        }
        return resultMapOfChart;
    }

    @Override
    public Map<String, List<Object>> getByDays(CentralizedControlMonitorDTO dto) {
        List<CheckPassRateVO> byDays = checkPassRateMapper.getByDays(dto);
        if (!CollectionUtil.isEmpty(byDays) && byDays.size() > 0) {
            resultMapOfChart = new HashMap<>();
            Date startDealTime = dto.getStartDealTime();
            Date endDealTime = dto.getEndDealTime();
            // 获取两日期间的查
            int dayCountOfTwoDate = getDayCountOfTwoDate(startDealTime, endDealTime);
            taskCountArray = new Integer[dayCountOfTwoDate];
            xTimeArray = new String[dayCountOfTwoDate];
            passRateArray = new Double[dayCountOfTwoDate];
            // 给x轴初始值
            xTimeArray = getXOfDaysStr(startDealTime, endDealTime, dayCountOfTwoDate);
            // 给合格率和任务量初始值
            taskCountArray = Arrays.stream(taskCountArray).map(t -> t = 0).toArray(Integer[]::new);
            passRateArray = Arrays.stream(passRateArray).map(a -> a = 0d).toArray(Double[]::new);
            Map<String, Integer> timeMap = new HashMap<>();
            for (int i = 0; i < xTimeArray.length; i++) {
                timeMap.put(xTimeArray[i], i);
            }
            byDays.stream().sorted(Comparator.comparing(CheckPassRateVO::getXTime)).forEach(d -> {
                if (timeMap.containsKey(d.getXTime())) {
                    Integer index = timeMap.get(d.getXTime());
                    taskCountArray[index] = d.getTaskCount();
                    passRateArray[index] = d.getPassRate();
                }
            });
            if (dto.getEquipSpecCode() != null) {
                List<PartOperationStatusVO> taskCountEquipSpecCodeWithDays = checkPassRateMapper.getTaskCountEquipSpecCodeWithDays(dto);
                taskCountEquipSpecCodeWithDays.stream().forEach(t -> {
                    if(timeMap.containsKey(t.getXTime())){
                        Integer index = timeMap.get(t.getXTime());
                        taskCountArray[index] = t.getTaskCount() == null ? 0 : t.getTaskCount();
                    }
                });
            }
            resultMapOfChart.put("xTimeArray", Arrays.asList(xTimeArray));
            resultMapOfChart.put("taskCountArray", Arrays.asList(taskCountArray));
            resultMapOfChart.put("passRate", Arrays.asList(passRateArray));
        }
        else{
            resultMapOfChart = null;
        }
        return resultMapOfChart;
    }

    @Override
    public Map<String, List<Object>> getByMonths(CentralizedControlMonitorDTO dto) {
        List<CheckPassRateVO> byMonths = checkPassRateMapper.getByMonths(dto);
        if (!CollectionUtil.isEmpty(byMonths) && byMonths.size() > 0) {
            Date startDealTime = dto.getStartDealTime();
            Date endDealTime = dto.getEndDealTime();
            resultMapOfChart = new HashMap<>();
            // 先获取两日期差
            int monthCountOfTwoDate = getMonthCountOfTwoDate(startDealTime, endDealTime);
            taskCountArray = new Integer[monthCountOfTwoDate];
            xTimeArray = new String[monthCountOfTwoDate];
            passRateArray = new Double[monthCountOfTwoDate];
            // 给x轴赋值
            xTimeArray = getXOfMonthsStr(startDealTime,endDealTime,monthCountOfTwoDate);

            Map<String, Integer> timeMap = new HashMap<>();
            for (int i = 0; i < xTimeArray.length; i++) {
                timeMap.put(xTimeArray[i], i);
            }
            byMonths.stream().sorted(Comparator.comparing(CheckPassRateVO::getXTime)).forEach(m -> {
                if(timeMap.containsKey(m.getXTime())){
                    Integer index = timeMap.get(m.getXTime());
                    taskCountArray[index] = m.getTaskCount();
                    passRateArray[index] = m.getPassRate();
                }
            });
            if (dto.getEquipSpecCode() != null) {
                List<PartOperationStatusVO> taskCountEquipSpecCodeWithDays = checkPassRateMapper.getTaskCountEquipSpecCodeWithMonths(dto);
                taskCountEquipSpecCodeWithDays.stream().forEach(t -> {
                    if(timeMap.containsKey(t.getXTime())){
                        Integer index = timeMap.get(t.getXTime());
                        taskCountArray[index] = t.getTaskCount() == null ? 0 : t.getTaskCount();
                    }
                });
            }
            resultMapOfChart.put("xTimeArray", Arrays.asList(xTimeArray));
            resultMapOfChart.put("taskCountArray", Arrays.asList(taskCountArray));
            resultMapOfChart.put("passRate", Arrays.asList(passRateArray));
        }
        else{
            resultMapOfChart = null;
        }
        return resultMapOfChart;
    }
}
