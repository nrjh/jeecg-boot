package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.strategy.specific;

import cn.hutool.core.collection.CollectionUtil;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto.CentralizedControlMonitorDTO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.mapper.OperationStatusMapper;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.strategy.AbstractMethodOfCharts;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.OperationStatusVO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.PartOperationStatusVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/26 19:41
 */
@Service
public class OperationStatusCharts extends AbstractMethodOfCharts {
    @Autowired
    private OperationStatusMapper operationStatusMapper;
    private Integer[] checkCountArray = null;
    private Integer[] taskCountArray = null;
    private String[] actionTimeArray = null;
    private String[] xTimeArray = null;
    private Map<String, List<Object>> resultMapOfChart = null;

    @Override
    public boolean isSupport(String chartsName) {
        return chartsName.equals("OperationStatus");
    }

    @Override
    public Map<String, List<Object>> getByHours(CentralizedControlMonitorDTO dto) {
        Date startDate = dto.getStartDealTime();
        List<OperationStatusVO> byHours = operationStatusMapper.getByHours(dto);
        if (!CollectionUtil.isEmpty(byHours) && byHours.size() > 0) {
            resultMapOfChart = new HashMap<>();
            // 初始化
            // 检定量
            checkCountArray = new Integer[24];
            // 任务量
            taskCountArray = new Integer[24];
            // 运行时长,按小时查询时则以小时为单位
            actionTimeArray = new String[24];
            // x轴
            xTimeArray = new String[24];
            // 给x轴初始化
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String format = sdf.format(startDate);
            for (int i = 0; i < xTimeArray.length; i++) {
                xTimeArray[i] = format + " " + i + "时";
            }
            // 初始化为0
            checkCountArray = Arrays.stream(checkCountArray).map(c -> c = 0).toArray(Integer[]::new);
            taskCountArray = Arrays.stream(taskCountArray).map(t -> t = 0).toArray(Integer[]::new);
            actionTimeArray = Arrays.stream(actionTimeArray).map(a -> a = 0 + "时").toArray(String[]::new);

            byHours.forEach(h -> {
                // 下标
                Integer index = Integer.parseInt(h.getXTime());
                checkCountArray[index] = h.getCheckCount() == null ? 0 : h.getCheckCount();
                taskCountArray[index] = h.getTaskCount() == null ? 0 : h.getTaskCount();
                // 以小时为单位
                actionTimeArray[index] = h.getActionTime() == null ? 0+"" : h.getActionTime();
            });
            if (dto.getEquipSpecCode() != null) {
                // 如果是查询小时的，则不要传入结束处理时间
                // 因为只查24小时，比如18号~19号，那么只查18号当天的00:00:00 ~ 23:59:59这段时间
                if (dto.getTimeFlag().equals(1)) {
                    dto.setEndDealTime(null);
                }
                List<PartOperationStatusVO> taskCountEquipSpecCode = operationStatusMapper.getTaskCountEquipSpecCodeWithHours(dto);
                taskCountEquipSpecCode.forEach(t -> {
                    Integer xTime = Integer.parseInt(t.getXTime());
                    taskCountArray[xTime] = t.getTaskCount() == null ? 0 : t.getTaskCount();
                });
            }
            resultMapOfChart.put("checkCountArray", Arrays.asList(checkCountArray));
            resultMapOfChart.put("taskCountArray", Arrays.asList(taskCountArray));
            resultMapOfChart.put("actionTimeArray", Arrays.asList(actionTimeArray));
            resultMapOfChart.put("xTimeArray", Arrays.asList(xTimeArray));

        }
        else{
            resultMapOfChart = null;
        }
        return resultMapOfChart;
    }

    @Override
    public Map<String, List<Object>> getByDays(CentralizedControlMonitorDTO dto) {
        List<OperationStatusVO> byDays = operationStatusMapper.getByDays(dto);
        if (!CollectionUtil.isEmpty(byDays) && byDays.size() > 0) {
            // 获取两个日期的
            Date startDealTime = dto.getStartDealTime();
            Date endDealTime = dto.getEndDealTime();
            int dayCountOfTwoDate = getDayCountOfTwoDate(startDealTime, endDealTime);
            resultMapOfChart = new HashMap<>();
            // 初始化
            // 检定量
            checkCountArray = new Integer[dayCountOfTwoDate];
            // 运行时长
            actionTimeArray = new String[dayCountOfTwoDate];
            // x轴
            xTimeArray = getXOfDaysStr(startDealTime, endDealTime, dayCountOfTwoDate);
            // 任务量
            taskCountArray = new Integer[dayCountOfTwoDate];
            Map<String, Integer> timeMap = new HashMap<>();
            for (int i = 0; i < xTimeArray.length; i++) {
                timeMap.put(xTimeArray[i], i);
            }
            byDays.stream().sorted(Comparator.comparing(OperationStatusVO::getXTime)).forEach(d -> {
                if (timeMap.containsKey(d.getXTime())) {
                    Integer index = timeMap.get(d.getXTime());
                    checkCountArray[index] = d.getCheckCount() == null ? 0 : d.getCheckCount();
                    // 获取天数
                    actionTimeArray[index] = d.getActionTime() == null ? "0" : d.getActionTime();
                    taskCountArray[index] = d.getTaskCount() == null ? 0 : d.getTaskCount();
                }
            });

            if (dto.getEquipSpecCode() != null) {
                List<PartOperationStatusVO> taskCountEquipSpecCodeWithDays = operationStatusMapper.getTaskCountEquipSpecCodeWithDays(dto);
                taskCountEquipSpecCodeWithDays.stream().forEach(t -> {
                    if(timeMap.containsKey(t.getXTime())){
                        Integer index = timeMap.get(t.getXTime());
                        taskCountArray[index] = t.getTaskCount() == null ? 0 : t.getTaskCount();
                    }
                });
            }

            resultMapOfChart.put("checkCountArray", Arrays.asList(checkCountArray));
            resultMapOfChart.put("taskCountArray", Arrays.asList(taskCountArray));
            resultMapOfChart.put("actionTimeArray", Arrays.asList(actionTimeArray));
            resultMapOfChart.put("xTimeArray", Arrays.asList(xTimeArray));
        }
        else{
            resultMapOfChart = null;
        }
        return resultMapOfChart;
    }

    @Override
    public Map<String, List<Object>> getByMonths(CentralizedControlMonitorDTO dto) {
        List<OperationStatusVO> byMonths = operationStatusMapper.getByMonths(dto);
        if (!CollectionUtil.isEmpty(byMonths) && byMonths.size() > 0) {
            // 获取两个日期的月份差
            Date startDealTime = dto.getStartDealTime();
            Date endDealTime = dto.getEndDealTime();
            int monthCountOfTwoDate = getMonthCountOfTwoDate(startDealTime, endDealTime);
            // 初始化数组大小
            resultMapOfChart = new HashMap<>();
            // 初始化
            // 检定量
            checkCountArray = new Integer[monthCountOfTwoDate];
            // 运行时长
            actionTimeArray = new String[monthCountOfTwoDate];
            // x轴
            xTimeArray = getXOfMonthsStr(startDealTime, endDealTime, monthCountOfTwoDate);
            // 任务量
            taskCountArray = new Integer[monthCountOfTwoDate];
            Map<String, Integer> timeMap = new HashMap<>();
            for (int i = 0; i < xTimeArray.length; i++) {
                timeMap.put(xTimeArray[i], i);
            }
            byMonths.stream().sorted(Comparator.comparing(OperationStatusVO::getXTime)).forEach(m -> {
                if (timeMap.containsKey(m.getXTime())) {
                    Integer index = timeMap.get(m.getXTime());
                    checkCountArray[index] = m.getCheckCount() == null ? 0 : m.getCheckCount();
                    // 获取天数
                    actionTimeArray[index] = m.getActionTime() == null ? "0" : m.getActionTime() ;
                    taskCountArray[index] = m.getTaskCount() == null ? 0 : m.getTaskCount();
                }
            });

            if (dto.getEquipSpecCode() != null) {
                List<PartOperationStatusVO> taskCountEquipSpecCodeWithDays = operationStatusMapper.getTaskCountEquipSpecCodeWithMonths(dto);
                taskCountEquipSpecCodeWithDays.stream().forEach(t -> {
                    if(timeMap.containsKey(t.getXTime())){
                        Integer index = timeMap.get(t.getXTime());
                        taskCountArray[index] = t.getTaskCount() == null ? 0 : t.getTaskCount();
                    }
                });
            }
            resultMapOfChart.put("checkCountArray", Arrays.asList(checkCountArray));
            resultMapOfChart.put("taskCountArray", Arrays.asList(taskCountArray));
            resultMapOfChart.put("actionTimeArray", Arrays.asList(actionTimeArray));
            resultMapOfChart.put("xTimeArray", Arrays.asList(xTimeArray));
        }
        else{
            resultMapOfChart = null;
        }
        return resultMapOfChart;
    }
}
