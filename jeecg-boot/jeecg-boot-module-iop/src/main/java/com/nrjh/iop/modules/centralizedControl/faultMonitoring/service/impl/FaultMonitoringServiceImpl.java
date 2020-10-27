package com.nrjh.iop.modules.centralizedControl.faultMonitoring.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.mapper.FaultMonitoringMapper;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.service.FaultMonitoringService;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringResultVo;
import com.nrjh.iop.modules.centralizedControl.faultMonitoring.vo.FaultMonitoringVo;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lei-li
 * @create: 2020-09-25 14:31
 */
@Service
@DS("ora")
public class FaultMonitoringServiceImpl implements FaultMonitoringService {

    private final static String FAULTMONITORING_CLASSIFY_DAY = "day";
    private final static String FAULTMONITORING_CLASSIFY_MONTH = "month";

    @Autowired
    private FaultMonitoringMapper faultMonitoringMapper;

    @Override
    public List<FaultMonitoringResultVo> getFaultEquipMentBarList(FaultMonitoringVo faultMonitoringVo) {
        Date startDate = faultMonitoringVo.getStartDate();
        Date endDate = faultMonitoringVo.getEndDate();
        List<FaultMonitoringResultVo> faultMonitoringResultVoList = new ArrayList<>();
        if(FAULTMONITORING_CLASSIFY_DAY.equals(faultMonitoringVo.getClassify())){
            //获取天数
            int dayNum = 0;
            try {
                dayNum = daysBetween(startDate,endDate);
            }catch (Exception e){
                e.printStackTrace();
            }
            //获取本月天数
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(startDate);
            int a = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);

            for(int i=0;i < dayNum; i++){
                FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
                Calendar c = Calendar.getInstance();
                c.setTime(startDate);
                c.add(Calendar.DAY_OF_MONTH,i);
                Date tomorrow = c.getTime();
                SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
                String nextDay = sft.format(tomorrow);
                List<Map<String,Object>> mapList = faultMonitoringMapper.getFaultEquipMentBarByDay(nextDay,faultMonitoringVo);
                faultMonitoringResultVo.setDate(nextDay);

                List<Map<String,Object>> filterList = mapList.stream().filter(item->item.get("EQUIPCATEG")!=null).collect(Collectors.toList());
                filterList.forEach(x->{
                    if("01".equals(x.get("EQUIPCATEG").toString())){
                       faultMonitoringResultVo.setSingle(x.get("NUMS").toString());
                    }else if("02".equals(x.get("EQUIPCATEG").toString())){
                        faultMonitoringResultVo.setTriphase(x.get("NUMS").toString());
                    }else if("03".equals(x.get("EQUIPCATEG").toString())){
                        faultMonitoringResultVo.setMutual(x.get("NUMS").toString());
                    }else if("04".equals(x.get("EQUIPCATEG").toString())) {
                        faultMonitoringResultVo.setTerminal(x.get("NUMS").toString());
                    }else if("05".equals(x.get("EQUIPCATEG").toString())){
                        faultMonitoringResultVo.setStorage(x.get("NUMS").toString());
                    }
                });
                faultMonitoringResultVoList.add(faultMonitoringResultVo);
            }
        }else if(FAULTMONITORING_CLASSIFY_MONTH.equals(faultMonitoringVo.getClassify())){
            //获取月数
            int monthNum = getMonthDiff(startDate,endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String month = sdf.format(startDate);
            Date monthDate = null;
            try {
                 monthDate = sdf.parse(month);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(int i = 0;i < monthNum; i++){
                FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
                Calendar m = Calendar.getInstance();
                if(monthDate == null){
                    throw new JeecgBootException("日期为空");
                }
                m.setTime(monthDate);
                m.add(Calendar.MONTH,i);
                Date nextMonth = m.getTime();
                String proximo = sdf.format(nextMonth);
                List<Map<String,Object>> mapList = faultMonitoringMapper.getFaultEquipMentBarByMonth(proximo,faultMonitoringVo);
                faultMonitoringResultVo.setDate(proximo);
                List<Map<String,Object>> filterList = mapList.stream().filter(item->item.get("EQUIPCATEG")!=null).collect(Collectors.toList());
                filterList.forEach(x->{
                    if("01".equals(x.get("EQUIPCATEG").toString())){
                        faultMonitoringResultVo.setSingle(x.get("NUMS").toString());
                    }else if("02".equals(x.get("EQUIPCATEG").toString())){
                        faultMonitoringResultVo.setTriphase(x.get("NUMS").toString());
                    }else if("03".equals(x.get("EQUIPCATEG").toString())){
                        faultMonitoringResultVo.setMutual(x.get("NUMS").toString());
                    }else if("04".equals(x.get("EQUIPCATEG").toString())) {
                        faultMonitoringResultVo.setTerminal(x.get("NUMS").toString());
                    }else if("05".equals(x.get("EQUIPCATEG").toString())){
                        faultMonitoringResultVo.setStorage(x.get("NUMS").toString());
                    }
                });
                faultMonitoringResultVoList.add(faultMonitoringResultVo);
            }
        }

        return faultMonitoringResultVoList;

    }

    @Override
    public FaultMonitoringResultVo getAreaLinePieList(FaultMonitoringVo faultMonitoringVo) {
        Date startDate = faultMonitoringVo.getStartDate();
        Date endDate = faultMonitoringVo.getEndDate();
        FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
        if(FAULTMONITORING_CLASSIFY_DAY.equals(faultMonitoringVo.getClassify())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startTime = sdf.format(startDate);
            String endTime = sdf.format(endDate);
            List<Map<String,Object>> mapList = faultMonitoringMapper.getAreaLinePieByDay(startTime,endTime,faultMonitoringVo);
            List<String> userList = new ArrayList<>();
            List< Map<String, Object>> mapData = new ArrayList<>();
            List<Map<String,Object>> filterList = mapList.stream().filter(item->item.get("AREANAME")!=null && item.get("AREAID")!=null).collect(Collectors.toList());
            filterList.forEach(item->{
                Map<String, Object> map = new HashMap<>();
                userList.add(item.get("AREANAME").toString());
                map.put("name",item.get("AREANAME"));
                map.put("value",item.get("NUMS"));
                map.put("areaId",item.get("AREAID"));
                mapData.add(map);
            });
            faultMonitoringResultVo.setMapData(mapData);
            faultMonitoringResultVo.setFaultNameList(userList);
        }else if(FAULTMONITORING_CLASSIFY_MONTH.equals(faultMonitoringVo.getClassify())){
            //获取月数
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String startTime = sdf.format(startDate);
            String endTime = sdf.format(endDate);
            List<Map<String,Object>> mapList = faultMonitoringMapper.getAreaLinePieByMonth(startTime,endTime,faultMonitoringVo);
            List<String> userList = new ArrayList<>();
            List< Map<String, Object>> mapData = new ArrayList<>();
            List<Map<String,Object>> filterList = mapList.stream().filter(item->item.get("AREANAME")!=null && item.get("AREAID")!=null).collect(Collectors.toList());
            filterList.forEach(item->{
                Map<String, Object> map = new HashMap<>();
                userList.add(item.get("AREANAME").toString());
                map.put("name",item.get("AREANAME"));
                map.put("value",item.get("NUMS"));
                mapData.add(map);
            });
            faultMonitoringResultVo.setMapData(mapData);
            faultMonitoringResultVo.setFaultNameList(userList);
        }
        return faultMonitoringResultVo;
    }

    @Override
    public FaultMonitoringResultVo getFailureEquipmentPieList(FaultMonitoringVo faultMonitoringVo) {
        Date startDate = faultMonitoringVo.getStartDate();
        Date endDate = faultMonitoringVo.getEndDate();
        FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
        if(FAULTMONITORING_CLASSIFY_DAY.equals(faultMonitoringVo.getClassify())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startTime = sdf.format(startDate);
            String endTime = sdf.format(endDate);
            List<Map<String,Object>> mapList = faultMonitoringMapper.getFailureEquipmentPieByDay(startTime,endTime,faultMonitoringVo);
            List<String> userList = new ArrayList<>();
            List< Map<String, Object>> mapData = new ArrayList<>();
            List<Map<String,Object>> filterList = mapList.stream().filter(item->item.get("EQUIPNAME")!=null).collect(Collectors.toList());
            filterList.forEach(item->{
                Map<String, Object> map = new HashMap<>();
                userList.add(item.get("EQUIPNAME").toString());
                map.put("name",item.get("EQUIPNAME"));
                map.put("value",item.get("NUMS"));
                mapData.add(map);
            });
            faultMonitoringResultVo.setMapData(mapData);
            faultMonitoringResultVo.setFaultNameList(userList);
        }else if(FAULTMONITORING_CLASSIFY_MONTH.equals(faultMonitoringVo.getClassify())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String startTime = sdf.format(startDate);
            String endTime = sdf.format(endDate);
            List<Map<String,Object>> mapList = faultMonitoringMapper.getFailureEquipmentPieByMonth(startTime,endTime,faultMonitoringVo);
            List<String> userList = new ArrayList<>();
            List< Map<String, Object>> mapData = new ArrayList<>();
            List<Map<String,Object>> filterList = mapList.stream().filter(item->item.get("EQUIPNAME")!=null).collect(Collectors.toList());
            filterList.forEach(item->{
                Map<String, Object> map = new HashMap<>();
                userList.add(item.get("EQUIPNAME").toString());
                map.put("name",item.get("EQUIPNAME"));
                map.put("value",item.get("NUMS"));
                mapData.add(map);
            });
            faultMonitoringResultVo.setMapData(mapData);
            faultMonitoringResultVo.setFaultNameList(userList);
        }
        return faultMonitoringResultVo;
    }

    @Override
    public FaultMonitoringResultVo getFaultTypePieList(FaultMonitoringVo faultMonitoringVo) {
        Date startDate = faultMonitoringVo.getStartDate();
        Date endDate = faultMonitoringVo.getEndDate();
        FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
        if(FAULTMONITORING_CLASSIFY_DAY.equals(faultMonitoringVo.getClassify())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startTime = sdf.format(startDate);
            String endTime = sdf.format(endDate);
            List<Map<String,Object>> mapList = faultMonitoringMapper.getFaultTypePieByDay(startTime,endTime,faultMonitoringVo);
            List<String> userList = new ArrayList<>();
            List< Map<String, Object>> mapData = new ArrayList<>();
            List<Map<String,Object>> filterList = mapList.stream().filter(item->item.get("AREANAME")!=null).collect(Collectors.toList());
            filterList.forEach(item->{
                Map<String, Object> map = new HashMap<>();
                userList.add(item.get("AREANAME").toString()+item.get("FAULTLABE").toString());
                map.put("name",item.get("AREANAME").toString()+item.get("FAULTLABE").toString());
                map.put("value",item.get("NUMS"));
                mapData.add(map);
            });
            faultMonitoringResultVo.setMapData(mapData);
            faultMonitoringResultVo.setFaultNameList(userList);
        }else if(FAULTMONITORING_CLASSIFY_MONTH.equals(faultMonitoringVo.getClassify())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String startTime = sdf.format(startDate);
            String endTime = sdf.format(endDate);
            List<Map<String,Object>> mapList = faultMonitoringMapper.getFaultTypePieByMonth(startTime,endTime,faultMonitoringVo);
            List<String> userList = new ArrayList<>();
            List< Map<String, Object>> mapData = new ArrayList<>();
            List<Map<String,Object>> filterList = mapList.stream().filter(item->item.get("AREANAME")!=null).collect(Collectors.toList());
            filterList.forEach(item->{
                Map<String, Object> map = new HashMap<>();
                userList.add(item.get("AREANAME").toString()+item.get("FAULTLABE").toString());
                map.put("name",item.get("AREANAME").toString()+item.get("FAULTLABE").toString());
                map.put("value",item.get("NUMS"));
                mapData.add(map);
            });
            faultMonitoringResultVo.setMapData(mapData);
            faultMonitoringResultVo.setFaultNameList(userList);
        }
        return faultMonitoringResultVo;
    }

    @Override
    public List<FaultMonitoringResultVo> getFailureTotalTimeLineData(FaultMonitoringVo faultMonitoringVo) {
        Date startDate = faultMonitoringVo.getStartDate();
        Date endDate = faultMonitoringVo.getEndDate();
        List<FaultMonitoringResultVo> faultMonitoringResultVoList = new ArrayList<>();
        if(FAULTMONITORING_CLASSIFY_DAY.equals(faultMonitoringVo.getClassify())){
            //获取天数
            int dayNum = 0;
            try {
                dayNum = daysBetween(startDate,endDate);
            }catch (Exception e){
                e.printStackTrace();
            }
            for(int i=0;i < dayNum; i++){
                FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
                Calendar c = Calendar.getInstance();
                c.setTime(startDate);
                c.add(Calendar.DAY_OF_MONTH,i);
                Date tomorrow = c.getTime();
                SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
                String nextDay = sft.format(tomorrow);
                //按天获取故障数量
                Map<String,Object> map = faultMonitoringMapper.getFailureEquipmetTotalByDay(nextDay,faultMonitoringVo);
                faultMonitoringResultVo.setDate(nextDay);
                faultMonitoringResultVo.setCount(map.get("COUNT").toString());
                //按天获取故障时长
                List<Map<String,Object>> timeMap = faultMonitoringMapper.getFailureTotalTimeByDay(nextDay,faultMonitoringVo);
                if("totalTime".equals(faultMonitoringVo.getLineCode())){
                    Double totalTime = 0.0;
                    for(int j = 0;j<timeMap.size();j++){
                        totalTime += Double.valueOf(timeMap.get(j).get("PROCESSTIME").toString());
                    }
                    faultMonitoringResultVo.setTotalTime(totalTime);
                    faultMonitoringResultVoList.add(faultMonitoringResultVo);
                }else if("timeRate".equals(faultMonitoringVo.getLineCode())){
                    faultMonitoringResultVo.setMapData(timeMap);
                    faultMonitoringResultVoList.add(faultMonitoringResultVo);
                }
            }
        }else if(FAULTMONITORING_CLASSIFY_MONTH.equals(faultMonitoringVo.getClassify())){
            //获取月数
            int monthNum = getMonthDiff(startDate,endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String month = sdf.format(startDate);
            Date monthDate = null;
            try {
                monthDate = sdf.parse(month);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(int i = 0;i < monthNum; i++){
                FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
                Calendar m = Calendar.getInstance();
                if(monthDate == null){
                    throw new JeecgBootException("日期为空");
                }
                m.setTime(monthDate);
                m.add(Calendar.MONTH,i);
                Date nextMonth = m.getTime();
                String proximo = sdf.format(nextMonth);
                //按月获取故障数量
                Map<String,Object> map = faultMonitoringMapper.getFailureEquipmetTotalByMonth(proximo,faultMonitoringVo);
                faultMonitoringResultVo.setDate(proximo);
                faultMonitoringResultVo.setCount(map.get("COUNT").toString());
                //按月获取故障时长
                List<Map<String,Object>> timeMap = faultMonitoringMapper.getFailureTotalTimeByMonth(proximo,faultMonitoringVo);
                if("totalTime".equals(faultMonitoringVo.getLineCode())){
                    Double totalTime = 0.0;
                    for(int j = 0;j<timeMap.size();j++){
                        totalTime += Double.valueOf(timeMap.get(j).get("PROCESSTIME").toString());
                    }
                    faultMonitoringResultVo.setTotalTime(totalTime);
                    faultMonitoringResultVoList.add(faultMonitoringResultVo);
                }else if("timeRate".equals(faultMonitoringVo.getLineCode())){
                    faultMonitoringResultVo.setMapData(timeMap);
                    faultMonitoringResultVoList.add(faultMonitoringResultVo);
                }
            }
        }

        return faultMonitoringResultVoList;

    }

    @Override
    public FaultMonitoringResultVo getFailureEquipTypePieByDay(FaultMonitoringVo faultMonitoringVo) {
        Date startDate = faultMonitoringVo.getStartDate();
        Date endDate = faultMonitoringVo.getEndDate();
        FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String startTime = sdf.format(startDate);
//            String endTime = sdf.format(endDate);
            String startTime=sdf.format(getmindate());
            String endTime=sdf.format(getmaxdate());
            String lastStartTime=getLastMindate();
            String lastEndTime=getLastMaxdate();

            List<Map<String,Object>> lastmapList = faultMonitoringMapper.getFailureEquipTypePieByDay(lastStartTime,lastEndTime,faultMonitoringVo);

            int lastTotal=0;

            for(Map<String,Object> map:lastmapList){
                lastTotal+=Integer.parseInt(String.valueOf(map.get("TOTAL")==null?"0":map.get("TOTAL")));
            }

            List<Map<String,Object>> mapList = faultMonitoringMapper.getFailureEquipTypePieByDay(startTime,endTime,faultMonitoringVo);

            int nowTotal=0;
            for(Map<String,Object> map:mapList){
                nowTotal+=Integer.parseInt(String.valueOf(map.get("TOTAL")==null?"0":map.get("TOTAL")));
            }
            Double ascPoint=0.0;

            NumberFormat numberFormat = NumberFormat.getInstance();
            String yearAddition="0";
            //同比增加
            if(nowTotal>lastTotal){
                numberFormat.setMaximumFractionDigits(2);
                yearAddition = numberFormat.format((float)   (nowTotal-lastTotal)/ (float)lastTotal* 100);//所占百分比
            }
            faultMonitoringResultVo.setNowTotal(String.valueOf(nowTotal));
            faultMonitoringResultVo.setLastTotal(String.valueOf(lastTotal));
            faultMonitoringResultVo.setYearAddition(yearAddition);
            List<String> userList = new ArrayList<>();
            if(!CollectionUtil.isEmpty(mapList) && mapList.size()>10){
                List<Map<String,Object>> newList=mapList.subList(10,mapList.size());
                int num=0;
                for(Map<String,Object> map:newList){
                    num+=Integer.parseInt(String.valueOf(map.get("TOTAL")==null?"0":map.get("TOTAL")));
                }
                Map<String,Object> mapNew=new HashMap<String,Object>();
                mapNew.put("PARAM_NAME","其他");
                mapNew.put("TOTAL",num);
                mapList.add(mapNew);
            }
            List< Map<String, Object>> mapData = new ArrayList<>();
            List<Map<String,Object>> filterList = mapList.stream().filter(item->item.get("PARAM_NAME")!=null).collect(Collectors.toList());
            filterList.forEach(item->{
                Map<String, Object> map = new HashMap<>();
//                userList.add(item.get("PARAM_NAME").toString());
                map.put("item",item.get("PARAM_NAME"));
                map.put("count",item.get("TOTAL"));
                map.put("equip_type",item.get("EQUIP_TYPE"));
                mapData.add(map);
            });
            faultMonitoringResultVo.setMapData(mapData);
//            faultMonitoringResultVo.setFaultNameList(userList);
        return faultMonitoringResultVo;
    }

    @Override
    public FaultMonitoringResultVo getFailureEquipTypeBarByDay(FaultMonitoringVo faultMonitoringVo) {
        Date startDate = faultMonitoringVo.getStartDate();
        Date endDate = faultMonitoringVo.getEndDate();
        FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = sdf.format(startDate);
        String endTime = sdf.format(endDate);
        List<Map<String,Object>> mapList = faultMonitoringMapper.getFailureEquipTypeBarByArea(startTime,endTime,faultMonitoringVo.getEquipType());
        if(!CollectionUtil.isEmpty(mapList) && mapList.size()>10){
            List<Map<String,Object>> newList=mapList.subList(10,mapList.size());
            int num=0;
            for(Map<String,Object> map:newList){
                num+=Integer.parseInt(String.valueOf(map.get("TOTAL")==null?"0":map.get("TOTAL")));
            }
            Map<String,Object> mapNew=new HashMap<String,Object>();
            mapNew.put("y","其他");
            mapNew.put("y",num);
            mapList.add(mapNew);
        }
        List< Map<String, Object>> mapData = new ArrayList<>();
        List<Map<String,Object>> filterList = mapList.stream().filter(item->item.get("NAME")!=null).collect(Collectors.toList());
        filterList.forEach(item->{
            Map<String, Object> map = new HashMap<>();
//                userList.add(item.get("PARAM_NAME").toString());
            map.put("x",item.get("NAME"));
            map.put("y",item.get("TOTAL"));
            map.put("area_id",item.get("AREA_ID"));
            mapData.add(map);
        });
        faultMonitoringResultVo.setMapData(mapData);
        return faultMonitoringResultVo;
    }

    @Override
    public FaultMonitoringResultVo getFailureEquipNoBarByEquipType(FaultMonitoringVo faultMonitoringVo) {
        Date startDate = faultMonitoringVo.getStartDate();
        Date endDate = faultMonitoringVo.getEndDate();
        FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = sdf.format(startDate);
        String endTime = sdf.format(endDate);
        List< Map<String, Object>> mapData = new ArrayList<>();
        List<Map<String,Object>> mapList = faultMonitoringMapper.getFailureEquipNoBarByEquipType(startTime,endTime,faultMonitoringVo.getEquipType(),faultMonitoringVo.getAreaId());
        if(!CollectionUtil.isEmpty(mapList) && mapList.size()>10){
            List<Map<String,Object>> newList=mapList.subList(10,mapList.size());
            int num=0;
            for(Map<String,Object> map:newList){
                num+=Integer.parseInt(String.valueOf(map.get("TOTAL")==null?"0":map.get("TOTAL")));
            }
            Map<String,Object> mapNew=new HashMap<String,Object>();
            mapNew.put("x","其他");
            mapNew.put("y",num);
            mapList.add(mapNew);
            List<Map<String,Object>> filterList = mapList.subList(0,10).stream().filter(item->item.get("NAME")!=null).collect(Collectors.toList());
            filterList.forEach(item->{
                Map<String, Object> map = new HashMap<>();
//                userList.add(item.get("PARAM_NAME").toString());
                map.put("x",item.get("NAME"));
                map.put("y",item.get("TOTAL"));
                map.put("equipName",item.get("equipname"));
                map.put("equip_no",item.get("EQUIP_NO"));
                mapData.add(map);
            });
        }else {
            List<Map<String,Object>> filterList = mapList.stream().filter(item->item.get("NAME")!=null).collect(Collectors.toList());
            filterList.forEach(item->{
                Map<String, Object> map = new HashMap<>();
//                userList.add(item.get("PARAM_NAME").toString());
                map.put("x",item.get("NAME"));
                map.put("y",item.get("TOTAL"));
                map.put("equipName",item.get("equipname"));
                map.put("equip_no",item.get("EQUIP_NO"));
                mapData.add(map);
            });
        }

        faultMonitoringResultVo.setMapData(mapData);
        return faultMonitoringResultVo;
    }

    @Override
    public Result<?> detailEquipTypeByAreaId(FaultMonitoringVo faultMonitoringVo) {
        FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
        String startTime = DateUtils.formatDate(faultMonitoringVo.getStartDate(),"yyyy-MM-dd");
        String endTime = DateUtils.formatDate(faultMonitoringVo.getEndDate(),"yyyy-MM-dd");
        List<Map<String,Object>> dataMapList = faultMonitoringMapper.detailEquipTypeByAreaId(startTime,endTime,faultMonitoringVo);
        List list = new ArrayList();
        if (!CollectionUtils.isEmpty(dataMapList)){
            dataMapList.forEach(item -> {
                Map map = new HashMap();
                map.put("count",item.get("NUM") == null ? 0 : item.get("NUM"));
                map.put("item",item.get("NAME"));
                map.put("equipType",item.get("ID"));
                map.put("areaId",item.get("AREA_ID"));
                list.add(map);
            });
        }
        faultMonitoringResultVo.setMapData(list);
        return Result.ok(faultMonitoringResultVo);
    }

    @Override
    public Result<?> detailEquipByAreaIdAndEquipNO(FaultMonitoringVo faultMonitoringVo) {
        FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
        String startTime = DateUtils.formatDate(faultMonitoringVo.getStartDate(),"yyyy-MM-dd");
        String endTime = DateUtils.formatDate(faultMonitoringVo.getEndDate(),"yyyy-MM-dd");
        List<Map<String,Object>> dataMapList = faultMonitoringMapper.detailEquipByAreaIdAndEquipNO(startTime,endTime,faultMonitoringVo);
        Map data = new HashMap();
        List<Map<String, Object>> list = new ArrayList();
        if (!CollectionUtils.isEmpty(dataMapList)){
            dataMapList.forEach(item -> {
                Map map = new HashMap();
                map.put("value",item.get("NUM") == null ? 0 : item.get("NUM"));
                map.put("name",item.get("NAME"));
                map.put("areaId",item.get("AREA_ID"));
                map.put("equipNO",item.get("ID"));
                list.add(map);
            });
            data.put("max",list.get(0));
        }
        data.put("data",list);
        faultMonitoringResultVo.setData(data);
        return Result.ok(faultMonitoringResultVo);
    }

    @Override
    public Result<?> detailFaultByEquipNOAndAreaId(FaultMonitoringVo faultMonitoringVo) {
        FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
        String startTime = DateUtils.formatDate(faultMonitoringVo.getStartDate(),"yyyy-MM-dd");
        String endTime = DateUtils.formatDate(faultMonitoringVo.getEndDate(),"yyyy-MM-dd");
        List<Map<String,Object>> dataMapList = faultMonitoringMapper.detailFaultByEquipNOAndAreaId(startTime,endTime,faultMonitoringVo);
        List<Map<String, Object>> list = new ArrayList();
        Map data = new HashMap();
        if (!CollectionUtils.isEmpty(dataMapList)){
            //获取总数
            Integer total = dataMapList.stream().mapToInt( item -> item.get("NUM") == null ? 0 : Integer.parseInt(item.get("NUM").toString())).sum();
            dataMapList.forEach(item -> {
                Map map = new HashMap();
                map.put("count",item.get("NUM") == null ? 0 : item.get("NUM"));
                map.put("item",item.get("NAME"));
                map.put("areaId",item.get("AREA_ID"));
                map.put("equipNO",item.get("EQUIP_NO"));
                list.add(map);
            });
            List topList = new ArrayList();
            if (dataMapList.size() >= 1){
                Integer num = Integer.parseInt(dataMapList.get(0).get("NUM").toString());
                Integer proportion = (int) (Double.valueOf(String.format("%.2f", num.doubleValue()  / total.doubleValue())) * 100);
                //构建紧气缸故障占比28%的数据
                Map map = new HashMap();
                map.put("name",dataMapList.get(0).get("NAME"));
                map.put("prop",proportion);
                topList.add(map);
            }
            if (dataMapList.size() == 2){
                Integer num = Integer.parseInt(dataMapList.get(1).get("NUM").toString());
                Integer proportion = (int) (Double.valueOf(String.format("%.2f", num.doubleValue()  / total.doubleValue())) * 100);
                //构建紧气缸故障占比28%的数据
                Map map = new HashMap();
                map.put("name",dataMapList.get(1).get("NAME"));
                map.put("prop",proportion);
                topList.add(map);
            }
            data.put("top",topList);
        }
        data.put("data",list);
        faultMonitoringResultVo.setData(data);
        return Result.ok(faultMonitoringResultVo);
    }



    @Override
    public Result<?> detailFaultComparisonByAreaIdAndEquipNO(FaultMonitoringVo faultMonitoringVo) {
        String time = DateUtils.formatDate(new Date(),"yyyy-MM");
        int month = new Date().getMonth() + 1;
        List<Map<String,Object>> dataMapList = new ArrayList<>();
        FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
        for (int i = 0; i < 6; i++){
            Map<String,Object> map = new HashMap();
            List<Map<String, Object>> list = faultMonitoringMapper.detailFaultComparisonByAreaIdAndEquipNO(time, faultMonitoringVo);
            map.put("month",month -i);
            map.put("jeecg",0);
            if (!CollectionUtils.isEmpty(list)) {
                map.put("jeecg", list.size() == 0 ? "" : list.get(0).get("NUM") == null ? 0 : list.get(0).get("NUM"));
                map.put("type", (month -i)+"月"+(list.size() == 0 ? "" : list.get(0).get("NAME")));
            }else{
                map.put("jeecg", 0);
                map.put("type", (month -i)+"月"+faultMonitoringVo.getFaultName() );
            }
            dataMapList.add(map);
            time = getSubMonth(time);
        }
        faultMonitoringResultVo.setMapData(dataMapList);
        return Result.ok(faultMonitoringResultVo);
    }

    @Override
    public FaultMonitoringResultVo detailFaultComparisonByDay() {
        FaultMonitoringResultVo responseVo = new FaultMonitoringResultVo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTime=sdf.format(getmindate());
        String endTime=sdf.format(getmaxdate());
        String lastStartTime=getLastMindate();
        String lastEndTime=getLastMaxdate();
        Integer total = 0;
        List<Map<String,Object>> mapList = faultMonitoringMapper.getAreaLinePieByDay(startTime,endTime,null);
        Map<String,Object> map = new HashMap<>();
        if (mapList.size() > 0 && !CollectionUtils.isEmpty(mapList)){
            //获取当月总数
            total = mapList.stream().mapToInt(item -> Integer.parseInt(item.get("NUMS").toString())).sum();
            map = mapList.get(0);
        }
        Map<String,Object> map2 = new HashMap();
        Integer total2 = 0;
        List<Map<String,Object>> mapList2 = faultMonitoringMapper.getAreaLinePieByDay(lastStartTime,lastEndTime,null);
        if (CollectionUtils.isEmpty(mapList2) && mapList2.size() > 0){
            //获取当月总数
            total2 = mapList2.stream().mapToInt(item -> Integer.parseInt(item.get("NUMS").toString())).sum();
            for (Map<String,Object> m : mapList2){
                if (map.get("NUMS").equals(m.get("NUMS")) && map.get("AREAID").equals(m.get("AREAID"))){
                    map2 = m;
                }
            }
        }

        Double proportion = 0.00;
        Double proportion2 = 0.00;
        if (map.containsKey("NUMS")){
            proportion =  (Double.valueOf(String.format("%.4f", (Integer.valueOf(map.get("NUMS").toString())).doubleValue()  / total.doubleValue())) * 100);
        }

        if (map2.containsKey("NUMS")){
             proportion2 =  (Double.valueOf(String.format("%.4f", (Integer.valueOf(map2.get("NUMS").toString())).doubleValue()  / total2.doubleValue())) * 100);
        }
        responseVo.setYearAddition(proportion - proportion2 < 0 ? (proportion2 - proportion) + "%" : (proportion - proportion2) + "%");
        responseVo.setMsg(proportion - proportion2 == 0 ? "增加" : proportion - proportion2 < 0 ? "减少" : "增加");
        return responseVo;
    }


    private String getSubMonth(String time){
        String[] split = time.split("-");
        Integer year = Integer.parseInt(split[0]);
        Integer month = Integer.parseInt(split[1]);
        month -= 1;
        if (month == 0){
            month = 12;
            year -= year;
        }
        return year + "-" + month;
    }

//    @Override
//    public List<FaultMonitoringResultVo> getFailureHandlingTimeRateLineData(FaultMonitoringVo faultMonitoringVo) {
//        Date startDate = faultMonitoringVo.getStartDate();
//        Date endDate = faultMonitoringVo.getEndDate();
//        List<FaultMonitoringResultVo> faultMonitoringResultVoList = new ArrayList<>();
//        if(FAULTMONITORING_CLASSIFY_DAY.equals(faultMonitoringVo.getClassify())){
//            //获取天数
//            int dayNum = 0;
//            try {
//                dayNum = daysBetween(startDate,endDate);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            for(int i=0;i < dayNum; i++){
//                FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
//                Calendar c = Calendar.getInstance();
//                c.setTime(startDate);
//                c.add(Calendar.DAY_OF_MONTH,i);
//                Date tomorrow = c.getTime();
//                SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
//                String nextDay = sft.format(tomorrow);
//                //按天获取故障数量
//                Map<String,Object> map = faultMonitoringMapper.getFailureEquipmetTotalByDay(nextDay,faultMonitoringVo);
//                faultMonitoringResultVo.setDate(nextDay);
//                faultMonitoringResultVo.setCount(map.get("COUNT").toString());
//                //按天获取故障时长
//                List<Map<String,Object>> timeMap = faultMonitoringMapper.getfailureHandlingTimeRateDataByDay(nextDay,faultMonitoringVo);
//                Double totalTime = 0.0;
//                for(int j = 0;j<timeMap.size();j++){
//                    totalTime += Double.valueOf(timeMap.get(j).get("PROCESSTIME").toString());
//                }
//                faultMonitoringResultVo.setTotalTime(totalTime);
//                faultMonitoringResultVoList.add(faultMonitoringResultVo);
//            }
//        }else if(FAULTMONITORING_CLASSIFY_MONTH.equals(faultMonitoringVo.getClassify())){
//            //获取月数
//            int monthNum = getMonthDiff(startDate,endDate);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//            String month = sdf.format(startDate);
//            Date monthDate = null;
//            try {
//                monthDate = sdf.parse(month);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            for(int i = 0;i < monthNum; i++){
//                FaultMonitoringResultVo faultMonitoringResultVo = new FaultMonitoringResultVo();
//                Calendar m = Calendar.getInstance();
//                if(monthDate == null){
//                    throw new JeecgBootException("日期为空");
//                }
//                m.setTime(monthDate);
//                m.add(Calendar.MONTH,i);
//                Date nextMonth = m.getTime();
//                String proximo = sdf.format(nextMonth);
//                //按月获取故障数量
//                Map<String,Object> map = faultMonitoringMapper.getFailureEquipmetTotalByMonth(proximo,faultMonitoringVo);
//                faultMonitoringResultVo.setDate(proximo);
//                faultMonitoringResultVo.setCount(map.get("COUNT").toString());
//                //按月获取故障时长
//                List<Map<String,Object>> timeMap = faultMonitoringMapper.getFailureTotalTimeByMonth(proximo,faultMonitoringVo);
//                Double totalTime = 0.0;
//                for(int j = 0;j<timeMap.size();j++){
//                    totalTime += Double.valueOf(timeMap.get(j).get("PROCESSTIME").toString());
//                }
//                faultMonitoringResultVo.setTotalTime(totalTime);
//                faultMonitoringResultVoList.add(faultMonitoringResultVo);
//            }
//        }
//
//        return faultMonitoringResultVoList;
//
//    }

    /**
     *  获取两个日期相差的天数
     */
    public int daysBetween(Date smdate, Date bdate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取两个日期相差的月数
     */
    public static int getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        int monthsDiff = Math.abs(yearInterval * 12 + monthInterval);
        return monthsDiff;
    }


    /**
     * 获取本月第一天
     * @return
     */
    public  Date getmindate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        return calendar.getTime();
    }

    /**
     * 获取本月最后一天
     * @return
     */
    public  Date getmaxdate(){
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        calendar2.set(Calendar.DAY_OF_MONTH, calendar2.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar2.getTime();
    }

    //获取上个月的第一天
    public String getLastMindate(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar=Calendar.getInstance();

        calendar.add(Calendar.MONTH, -1);

        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return format.format(calendar.getTime());
    }

//获取上个月的最后一天
    public String  getLastMaxdate(){
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar=Calendar.getInstance();

        int month=calendar.get(Calendar.MONTH);

        calendar.set(Calendar.MONTH, month-1);

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return sf.format(calendar.getTime());
    }

}
