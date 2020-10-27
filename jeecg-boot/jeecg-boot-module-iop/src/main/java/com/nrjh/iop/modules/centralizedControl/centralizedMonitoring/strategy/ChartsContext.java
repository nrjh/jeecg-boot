package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.strategy;

import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto.CentralizedControlMonitorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 图像环境角色
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/27 11:52
 */
@Component
@Slf4j
public class ChartsContext {
    /**
     * 通过Spring将实现BaseMessageStrategy的实现类都自动注入到strategyMap类中，
     * 当用户传入选择的资源池时，自动根据资源池id去对应的资源池实现中查找资源。
     */
    @Resource
    private final Map<String, AbstractMethodOfCharts> strategyMap = new ConcurrentHashMap<>();

    /**
     * Spring加载bean时执行
     *
     * @param strategyMap 策略Map
     */
    public ChartsContext(Map<String, AbstractMethodOfCharts> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach((k, v) -> this.strategyMap.put(k, v));
    }

    public Map<String, List<Object>> executeStrategy(CentralizedControlMonitorDTO dto){
        Map<String, List<Object>> resultMap = null;

        // 循环策略集合取出满足策略的集合
        for (Map.Entry<String, AbstractMethodOfCharts> entry : strategyMap.entrySet()) {
            AbstractMethodOfCharts abstractMethodOfCharts = entry.getValue();
            // 判断是否满足对应的策略
            if (abstractMethodOfCharts.isSupport(dto.getChartType())) {
                switch (dto.getTimeFlag()){
                    // 按小时查询
                    case 1:
                        resultMap = abstractMethodOfCharts.getByHours(dto);
                        break;
                    // 按天数查询
                    case 2:
                        resultMap = abstractMethodOfCharts.getByDays(dto);
                        break;
                    // 按月份查询
                    case 3:
                        resultMap = abstractMethodOfCharts.getByMonths(dto);
                        break;
                }
            }
        }
        return resultMap;
    }

}
