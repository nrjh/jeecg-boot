package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.strategy.specific;

import cn.hutool.core.collection.CollectionUtil;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto.CentralizedControlMonitorDTO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.mapper.PrdTaskTraceMapper;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.strategy.AbstractMethodOfCharts;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.PrdTaskTraceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/25 10:46
 */
@Service
public class PrdTaskTraceCharts extends AbstractMethodOfCharts {
    private Integer[] actNumArray = null;
    private Integer[] planNumArray = null;
    private String[] timeArray = null;
    private Map<String, List<Object>> resultList = null;
    @Autowired
    private PrdTaskTraceMapper prdTaskTraceMapper;

    @Override
    public boolean isSupport(String chartsName) {
        return chartsName.equals("PrdTaskTrace");
    }

    /**
     * 获取每个小时的计划数（y轴）、生产数（y轴）、x轴
     * @param dto
     * @return
     */
    @Override
    public Map<String, List<Object>> getByHours(CentralizedControlMonitorDTO dto) {
        Date startDate = dto.getStartDealTime();
        List<PrdTaskTraceVO> hours = prdTaskTraceMapper.getByHours(dto);
        if (!CollectionUtil.isEmpty(hours)) {
            resultList = new HashMap<>();
            actNumArray = new Integer[24];
            planNumArray = new Integer[24];
            timeArray = new String[24];
            // 给计划量赋值
            planNumArray = Arrays.stream(planNumArray).map(p -> p = hours.get(0).getPlanNum()).toArray(Integer[]::new);
            // 给实际量初始化
            actNumArray = Arrays.stream(actNumArray).map(a -> a = 0).toArray(Integer[]::new);
            // 给时间初始化
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String format = sdf.format(startDate);
            for (int i = 0; i < timeArray.length; i++) {
                timeArray[i] = format + " " + i + "时";
            }
            hours.stream().forEach(h -> {
                if (h.getPlanTime().equals(h.getActTime()) || (h.getPlanTime()!=null && h.getActTime() == null)) {
                    // 数组是从0开始的
                    actNumArray[Integer.parseInt(h.getPlanTime())] = h.getActNum() == null ? 0 : h.getActNum();
                }
            });
            resultList.put("actNumArray", Arrays.asList(actNumArray));
            resultList.put("planNumArray", Arrays.asList(planNumArray));
            resultList.put("timeArray", Arrays.asList(timeArray));
        }
        else {
            resultList = null;
        }
        return resultList;
    }

    @Override
    public Map<String, List<Object>> getByDays(CentralizedControlMonitorDTO dto) {
        Date startDate = dto.getStartDealTime();
        Date endDate = dto.getEndDealTime();
        List<PrdTaskTraceVO> byDays = prdTaskTraceMapper.getByDays(dto);
        if (!CollectionUtil.isEmpty(byDays)) {
            resultList = new HashMap<>();
            // 先计算两天数差
            Calendar c1=Calendar.getInstance();
            Calendar c2=Calendar.getInstance();
            c1.setTime(startDate);
            c2.setTime(endDate);
            int subtractDay = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
            actNumArray = new Integer[subtractDay+1];
            planNumArray = new Integer[subtractDay+1];
            timeArray = getXOfDaysStr(startDate,endDate,subtractDay+1);
            // 初始化
            actNumArray = Arrays.stream(actNumArray).map(a -> a = 0).toArray(Integer[]::new);
            planNumArray =  Arrays.stream(planNumArray).map(p -> p = 0).toArray(Integer[]::new);

            Map<String,Integer> timeMap = new HashMap<>();
            for (int i = 0; i < timeArray.length; i++) {
                timeMap.put(timeArray[i],i);
            }
            // 组装数据
            byDays.stream().sorted(Comparator.comparing(PrdTaskTraceVO::getPlanTime)).forEach(d -> {
                if (d.getPlanTime().equals(d.getActTime()) && timeMap.containsKey(d.getPlanTime())) {
                    Integer index = timeMap.get(d.getPlanTime());
                    actNumArray[index] = d.getActNum();
                    planNumArray[index] = d.getPlanNum();
                }

            });
            resultList.put("actNumArray", Arrays.asList(actNumArray));
            resultList.put("planNumArray", Arrays.asList(planNumArray));
            resultList.put("timeArray", Arrays.asList(timeArray));
        }
        else {
            resultList = null;
        }
        return resultList;
    }

    @Override
    public Map<String, List<Object>> getByMonths(CentralizedControlMonitorDTO dto) {
        Date startDate = dto.getStartDealTime();
        Date endDate = dto.getEndDealTime();
        List<PrdTaskTraceVO> byMonths = prdTaskTraceMapper.getByMonths(dto);
        if (!CollectionUtil.isEmpty(byMonths)) {
            resultList = new HashMap<>();
            // 计算月份
            Calendar c1=Calendar.getInstance();
            c1.setTime(startDate);
            Calendar c2=Calendar.getInstance();
            c2.setTime(endDate);
            int surplus = c2.get(Calendar.DATE) - c1.get(Calendar.DATE);
            int result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
            surplus = surplus <= 0 ? 1 : 0;
            int tempMonth= (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12;
            int monthsNums = Math.abs(tempMonth+result)+surplus;
            // 初始化月份
            timeArray = getXOfMonthsStr(startDate,endDate,monthsNums+1);
            actNumArray = new Integer[monthsNums+1];
            planNumArray = new Integer[monthsNums+1];
            // 初始化实际量
            actNumArray = Arrays.stream(actNumArray).map(a -> a = 0).toArray(Integer[]::new);
            // 初始化计划量
            planNumArray =  Arrays.stream(planNumArray).map(p -> p = 0).toArray(Integer[]::new);
            Map<String,Integer> timeMap = new HashMap<>();
            for (int i = 0; i < timeArray.length; i++) {
                timeMap.put(timeArray[i],i);
            }
            // 组装数据
            byMonths.stream().sorted(Comparator.comparing(PrdTaskTraceVO::getPlanTime)).forEach(m -> {
                if (
                        (m.getPlanTime().equals(m.getActTime()) && timeMap.containsKey(m.getPlanTime()))
                        || (m.getPlanTime()!=null && m.getActTime() == null)
                ) {
                    Integer index = timeMap.get(m.getPlanTime());
                    actNumArray[index] = m.getActNum() == null ? 0 : m.getActNum();
                    planNumArray[index] = m.getPlanNum();
                }
            });
            resultList.put("actNumArray", Arrays.asList(actNumArray));
            resultList.put("planNumArray", Arrays.asList(planNumArray));
            resultList.put("timeArray", Arrays.asList(timeArray));
        }
        else{
            resultList = null;
        }

        return resultList;
    }

}
