package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.strategy;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto.CentralizedControlMonitorDTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 统计图的策略模板
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/27 10:27
 */
@DS("ora")
public abstract class AbstractMethodOfCharts {

    /**
     * 检测是否支持该图标类型
     * @param chartsName 图型类型
     * @return
     */
    public abstract boolean isSupport(String chartsName);

    /**
     * 按小时获取(不超过24小时)
     * @param dto
     * @return
     */
    public abstract Map<String, List<Object>> getByHours(CentralizedControlMonitorDTO dto);

    /**
     * 按天数获取（不能超过31天）
     * @param dto
     * @return
     */
    public abstract Map<String,List<Object>> getByDays(CentralizedControlMonitorDTO dto);

    /**
     * 按月份获取（不能超过12个月）
     * @param dto
     * @return
     */
    public abstract Map<String,List<Object>> getByMonths(CentralizedControlMonitorDTO dto);

    /**
     * 获取x轴的点
     * 比如: 2020/09/23、2020/09/24
     * @param startDate
     * @param endDate
     * @param initDaysNum
     * @return
     */
    public String[] getXOfDaysStr(Date startDate, Date endDate, int initDaysNum){
        String[] resultDays = new String[initDaysNum];
        int i = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(startDate);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(endDate);
        // 日期加1(包含结束)
        tempEnd.add(Calendar.DATE, +1);
        while (tempStart.before(tempEnd)) {
            resultDays[i++] = (dateFormat.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return resultDays;
    }

    /**
     * 获取两个日期之间的月份
     * 比如:2020/09月、2020/10月
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return
     */
    public String[] getXOfMonthsStr(Date startDate,Date endDate,int monthsNums){
        String[] resultMonths = new String[monthsNums];
        int i = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM");
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(startDate);
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
        max.setTime(endDate);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        Calendar curr = min;
        while (curr.before(max)) {
            resultMonths[i++] = dateFormat.format(curr.getTime());
            curr.add(Calendar.MONTH, 1);
        }
        return resultMonths;
    }

    /**
     * 获取两个日期的月份差（包含起始月份）
     * 比如:09~11 有3个月（包含9月）
     * @param startDate
     * @param endDate
     * @return
     */
    public int getMonthCountOfTwoDate(Date startDate,Date endDate){
        Calendar c1=Calendar.getInstance();
        c1.setTime(startDate);
        Calendar c2=Calendar.getInstance();
        c2.setTime(endDate);
        int surplus = c2.get(Calendar.DATE) - c1.get(Calendar.DATE);
        int result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        surplus = surplus <= 0 ? 1 : 0;
        int tempMonth= (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12;
        int monthsNums = Math.abs(tempMonth+result)+surplus;
        return monthsNums+1;
    }

    /**
     * 获取两个日期之间相差的天数，包含起始日
     * 比如: 09-01~09-03 有3日
     * @param startDate
     * @param endDate
     * @return
     */
    public int getDayCountOfTwoDate(Date startDate,Date endDate){
        Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();
        c1.setTime(startDate);
        c2.setTime(endDate);
        int subtractDay = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
        return subtractDay+1;
    }
}
