package com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.strategy.specific;

import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.dto.CentralizedControlMonitorDTO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.mapper.CheckEquipOccupyRateMapper;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.po.KeyIdAndValuePO;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.serivce.EquipCategAndAreaService;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.strategy.AbstractMethodOfCharts;
import com.nrjh.iop.modules.centralizedControl.centralizedMonitoring.vo.CheckEquipOccupyRateVO;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/28 11:37
 */
@Service
public class CheckEquipOccupyRateCharts extends AbstractMethodOfCharts {

    private Map<String, List<Object>> resultMapOfChart = null;
    static final Map<String,String> nameMap = new HashMap<>();

    static {
        EquipCategAndAreaService equipCategAndAreaService = SpringContextUtils.getBean(EquipCategAndAreaService.class);
        // 获取设备类型id 、name
        List<KeyIdAndValuePO> allEquipCateg = equipCategAndAreaService.getAllEquipCateg();
        allEquipCateg.forEach(a -> {
            nameMap.put(a.getKeyId(),a.getValue());
        });
        // 获取检定线的id 、name
        List<KeyIdAndValuePO> allCheckLine = equipCategAndAreaService.getAllCheckLine();
        allCheckLine.forEach(a -> {
            nameMap.put(a.getKeyId(),a.getValue());
        });
        // todo 设备规格的存储需要优化，优化成数据库中获取或者从字典中获取
//        nameMap.put("10","设备规格1");
//        nameMap.put("20","设备规格2");
//        Set<Map.Entry<String, String>> entries = nameMap.entrySet();
//        打印key和value值
//        entries.forEach(e ->{
//            System.out.println(e.getKey()+":"+e.getValue());
//        });
    }

    // 设备名称
    List<String> nameList = null;
    // 设备名称和数量
    List<CheckEquipOccupyRateVO> nameAndValueList = null;

    @Autowired
    private CheckEquipOccupyRateMapper checkEquipOccupyRateMapper;

    @Override
    public boolean isSupport(String chartsName) {
        return chartsName.equals("checkEquipOccupyRate");
    }

    @Override
    public Map<String, List<Object>> getByHours(CentralizedControlMonitorDTO dto) {
        return resultMap(dto);
    }

    @Override
    public Map<String, List<Object>> getByDays(CentralizedControlMonitorDTO dto) {
        return resultMap(dto);
    }

    @Override
    public Map<String, List<Object>> getByMonths(CentralizedControlMonitorDTO dto) {
        return resultMap(dto);
    }

    private Map<String, List<Object>> resultMap(CentralizedControlMonitorDTO dto) {
        // 设备类别不为null，则显示设备规格
        if (dto.getEquipCateg() != null && dto.getEquipSpecCode() == null) {
            resultMapOfChart = new HashMap<>();
            nameList = new ArrayList<>();
            nameAndValueList = new ArrayList<>();
            List<CheckEquipOccupyRateVO> byHourWithEquipType = checkEquipOccupyRateMapper.getByEquipType(dto);
            if(byHourWithEquipType.size()>0){
                byHourWithEquipType.forEach(withEquipType -> {
                    withEquipType.setName(nameMap.get(withEquipType.getName()));
                    nameAndValueList.add(withEquipType);
                    nameList.add(withEquipType.getName());
                });
            }
            resultMapOfChart.put("nameList", Collections.singletonList(nameList));
            resultMapOfChart.put("nameAndValueList", Collections.singletonList(nameAndValueList));
        }
        // 设备规格不为null,则只显示那个设备
        else if(dto.getEquipSpecCode()!=null){
            resultMapOfChart = new HashMap<>();
            nameList = new ArrayList<>();
            nameAndValueList = new ArrayList<>();
            List<CheckEquipOccupyRateVO> byHourWithEquipTypeAndCateg = checkEquipOccupyRateMapper.getByEquipTypeAndCateg(dto);
            if(byHourWithEquipTypeAndCateg.size() > 0){
                byHourWithEquipTypeAndCateg.forEach(witheEquipOccupyRate -> {
                    witheEquipOccupyRate.setName(nameMap.get(dto.getEquipSpecCode()));
                    nameAndValueList.add(witheEquipOccupyRate);
                    nameList.add(witheEquipOccupyRate.getName());
                });
            }
            resultMapOfChart.put("nameList", Collections.singletonList(nameList));
            resultMapOfChart.put("nameAndValueList", Collections.singletonList(nameAndValueList));
        }
        else {
            resultMapOfChart = new HashMap<>();
            nameList = new ArrayList<>();
            nameAndValueList = new ArrayList<>();
            List<CheckEquipOccupyRateVO> byHoursHaveOne = checkEquipOccupyRateMapper.getHaveOne(dto);
            List<CheckEquipOccupyRateVO> byHoursNotOne = checkEquipOccupyRateMapper.getNotOne(dto);
            if(byHoursHaveOne.size()>0){
                byHoursHaveOne.forEach(one -> {
                    one.setName(nameMap.get(one.getName()));
                    nameAndValueList.add(one);
                    nameList.add(one.getName());
                });
            }
            if(byHoursNotOne.size() > 0){
                byHoursNotOne.forEach(notNoe -> {
                    notNoe.setName(nameMap.get(notNoe.getName()));
                    nameAndValueList.add(notNoe);
                    nameList.add(notNoe.getName());
                });
            }
            resultMapOfChart.put("nameList", Collections.singletonList(nameList));
            resultMapOfChart.put("nameAndValueList", Collections.singletonList(nameAndValueList));
        }
        return resultMapOfChart;
    }
}
