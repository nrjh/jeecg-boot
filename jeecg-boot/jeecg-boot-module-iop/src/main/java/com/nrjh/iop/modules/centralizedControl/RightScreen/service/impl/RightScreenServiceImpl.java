package com.nrjh.iop.modules.centralizedControl.RightScreen.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.MtPositionDetectInfo;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.TDetectTask;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper.MtPositionDetectInfoMapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper.TDetectTaskMapper;
import com.nrjh.iop.modules.centralizedControl.ProductPlanInfo.entity.DevProduceCycle;
import com.nrjh.iop.modules.centralizedControl.RightScreen.entity.model.BarModel;
import com.nrjh.iop.modules.centralizedControl.RightScreen.entity.model.LineModel;
import com.nrjh.iop.modules.centralizedControl.RightScreen.entity.vo.*;
import com.nrjh.iop.modules.centralizedControl.RightScreen.mapper.RightScreenMapper;
import com.nrjh.iop.modules.centralizedControl.RightScreen.service.IRightScreenService;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.DictModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author Kou Shenhai 2413176044@leimingtech.com
 * @version 1.0
 * @date 2020/9/16 0016 下午 3:17
 */
@Service
@DS("ora")
public class RightScreenServiceImpl implements IRightScreenService {

    @Autowired
    private RightScreenMapper rightScreenMapper;

    @Autowired
    private MtPositionDetectInfoMapper mtPositionDetectInfoMapper;

    @Autowired
    private TDetectTaskMapper tDetectTaskMapper;

    @Autowired
    private ISysBaseAPI iSysBaseAPI;

    @Override
    /**
     * 查询在检列表
     */
    public Result<?> checkList(String[] areaId) {
        //1.按天显示当天的待检任务列表清单、时间按时间倒序显示
        //2.列表中当任务检定结束后，列表中不再显示,需要进行三方验证（MDS、检定中间库以及现场采集的数据中检定总数据量、合格数量、不合格数量一致）
        //3.查询所有列表，然后过滤

        //0查询所有
        List<DetectTaskInfoVO> detectTaskInfoVOList = new ArrayList<>();
        if (areaId.length == 1 && "0".equals(areaId[0])){
            detectTaskInfoVOList = rightScreenMapper.detailDetectTaskInfoVoList("");
        }else{
            for (String id : areaId){
                detectTaskInfoVOList.addAll(rightScreenMapper.detailDetectTaskInfoVoList(id));
            }
        }
        if (CollectionUtils.isEmpty(detectTaskInfoVOList)){
            return Result.ok(detectTaskInfoVOList);
        }
        //累计完成量添加到list里面
        detectTaskInfoVOList.forEach(item -> {
            //1表示查询当天 0表示查询所有
            //获取累计完成量和当天完成量
            Map<String,Object> cumulativeCompletionMap = rightScreenMapper.detailCompleteNum(item.getAreaId(),0);
            Map<String, Object> amountCompletedMap = rightScreenMapper.detailCompleteNum(item.getAreaId(), 1);
            item.setCumulativeCompletion(cumulativeCompletionMap.get("NUM") == null ? 0 : Integer.valueOf(cumulativeCompletionMap.get("NUM").toString()));
            item.setAmountCompleted(amountCompletedMap.get("NUM") == null ? 0 : Integer.valueOf(amountCompletedMap.get("NUM").toString()));
        });
        //列表中当任务检定结束后，列表中不再显示,需要进行三方验证（MDS、检定中间库以及现场采集的数据中检定总数据量、合格数量、不合格数量一致）
        //过滤数据
        detectTaskInfoVOList = detectTaskInfoVOList.stream().filter(item -> !filterCheckList(item)).collect(Collectors.toList());
        return Result.ok(detectTaskInfoVOList);
    }

    @Override
    public Result<?> taskList() {
        //获取配送任务信息列表
        List<DeliveryTaskVO> deliveryTaskVOList = rightScreenMapper.detailDeliveryTaskVOList();
        return Result.ok(deliveryTaskVOList);
    }

    @Override
    public Result<?> announcementList() {
        //暂没有表，后台虚拟数据
        List<AnnouncementVO> announcementVOList = new ArrayList<>(256);
        for (int i = 0; i < 10; i++) {
            AnnouncementVO announcementVO = new AnnouncementVO();
            announcementVO.setAnnouncementName("通知公告"+ (i + 1) +"：墨带用完后未报警未停止，有贴空白标签的合格表入库（后台虚拟数据）");
            announcementVO.setReleaseTime(new Date());
            announcementVO.setController("张" + (i + 1));
            announcementVOList.add(announcementVO);
        }
        return Result.ok(announcementVOList);
    }

    @Override
    public Result<?> alarmList(String[] areaId) {
        //在页面列表中，显示故障视图中当天的故障列表，按时间倒序显示
//0查询所有
        List<AlarmInfoVO> alarmInfoVOList = new ArrayList<> ();
        if (areaId.length == 1 && "0".equals(areaId[0])){
            alarmInfoVOList = rightScreenMapper.detailAlarmInfoVoList("");
        }else {
            for (String id : areaId) {
                alarmInfoVOList.addAll(rightScreenMapper.detailAlarmInfoVoList(id));
            }
        }
        if (CollectionUtils.isEmpty(alarmInfoVOList)){
            return Result.ok(alarmInfoVOList);
        }
        //给处理时长赋值
        alarmInfoVOList = alarmInfoVOList.stream().map(item -> computeHandleTime(item)).collect(Collectors.toList());
        return Result.ok(alarmInfoVOList);
    }

    @Override
    public Result<?> equipAlarmList(String[] areaId) {
        //按设备分组显示当前所有设备故障频率较高的设备列表。
        //0查询所有
        List<EquipFailureVO> detailEquipFailureVoList = new ArrayList<>();
        if (areaId.length == 1 && "0".equals(areaId[0])){
            detailEquipFailureVoList = rightScreenMapper.detailEquipFailureVoList("");
        }else{
            List<EquipFailureVO> equipFailureVOS = rightScreenMapper.detailEquipFailureVoList("");
            for (String id : areaId){
                for (EquipFailureVO equipFailureVO : equipFailureVOS){
                    if (id.equals(equipFailureVO.getAreaId())){
                        detailEquipFailureVoList.add(equipFailureVO);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(detailEquipFailureVoList)){
            Collections.sort(detailEquipFailureVoList, new Comparator<EquipFailureVO>() {
                @Override
                public int compare(EquipFailureVO o1, EquipFailureVO o2) {
                    return o2.getFrequency().compareTo(o1.getFrequency());
                }
            });
        }
        System.out.println(detailEquipFailureVoList);
        return Result.ok(detailEquipFailureVoList);
    }

    @Override
    public Result<?> detectCountList() {
        DetectCountVO detectCountVO = new DetectCountVO();
        //获取单相、三相、互感器、采集设备的总合（本月）
        //获取本月时间并添加到Map
        Map<String,Object> dataMap = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        Integer year = cal.get(Calendar.YEAR);
        Integer month = cal.get(Calendar.MONTH ) + 1;
        dataMap.put("year",year);
        dataMap.put("month",month > 9 ? month : "0" + month);
        //查询本月待检总数列表
        List<DevProduceCycle> detailDevProduceCycleList = rightScreenMapper.detailDevProduceCycleList(dataMap);
        //本月待检总数
        Integer pendMonthTotal = 0;
        //本月完成检定数
        Integer completeVerificeMonthCount = 0;
        //今日待检总数
        Integer pendDayTotal = 0;
        //本日完成检定数
        Integer completeVerificeDayCount = 0;
        if (!CollectionUtils.isEmpty(detailDevProduceCycleList)){
            //本月待检总数
            pendMonthTotal = getDSHGCount(detailDevProduceCycleList.get(0));
            detectCountVO.setPendMonthTotal(pendMonthTotal);
        }
        //本月完成检定数统计列表
        //1本月 2本日
        List<Map<String, Object>> detailDetectTaskInfoMapList = rightScreenMapper.detailDetectTaskInfoMap(1);
        if (!CollectionUtils.isEmpty(detailDetectTaskInfoMapList)){
            //本月完成检定数
            completeVerificeMonthCount = getDSHGCount(detailDetectTaskInfoMapList);
            detectCountVO.setCompleteVerificeMonthCount(completeVerificeMonthCount);
        }
        if (completeVerificeMonthCount > 0 && pendMonthTotal > 0){
            //将int转为double，进行完成率计算
            //本月完成率=本月完成检测定数/本月待检总数
            //拼接字符串
            detectCountVO.setCompleteMonth((int) (Double.valueOf(String.format("%.2f", completeVerificeMonthCount.doubleValue() / pendMonthTotal.doubleValue())) * 100));
        }
        Integer day = cal.get(Calendar.DAY_OF_MONTH);
        dataMap.put("day",day > 9 ? day : "0" + day);
        List<Map<String, Object>> detailProduceDPlanMapList = rightScreenMapper.detailProduceDPlanMapList(dataMap);
        if (!CollectionUtils.isEmpty(detailProduceDPlanMapList)){
            //今日待检总数
            pendDayTotal = getDSHGCount(detailProduceDPlanMapList);
            detectCountVO.setPendDayTotal(pendDayTotal);
        }
        //1本月 2本日
        detailDetectTaskInfoMapList = rightScreenMapper.detailDetectTaskInfoMap(2);
        if (!CollectionUtils.isEmpty(detailDetectTaskInfoMapList)){
            //本日完成检定数
            completeVerificeDayCount = getDSHGCount(detailDetectTaskInfoMapList);
            detectCountVO.setCompleteVerificeDayCount(completeVerificeDayCount);
        }
        System.out.println(detailDetectTaskInfoMapList);
        if (completeVerificeDayCount > 0 && pendDayTotal > 0){
            //将int转为double，进行完成率计算
            //本日完成率=本日完成检测定数/本日待检总数
            //拼接字符串
            detectCountVO.setCompleteDay((int) (Double.valueOf(String.format("%.2f", completeVerificeDayCount.doubleValue() / pendDayTotal.doubleValue())) * 100));
        }
        //遍历当日计划检定数据
        for (Map<String,Object> data : detailProduceDPlanMapList){
            if ("D".equals(data.get("CATEG"))) {
                //当日计划检定单相
                detectCountVO.setPlanD(getDSHGMapValue(data, "D"));
            }else if("S".equals(data.get("CATEG"))){
                //当日计划检定三相
                detectCountVO.setPlanS(getDSHGMapValue(data,"S"));
            }else if("H".equals(data.get("CATEG"))){
                //当日计划检定互感器
                detectCountVO.setPlanH(getDSHGMapValue(data,"H"));
            }else if("C".equals(data.get("CATEG"))){
                //当日计划检定采集终端
                detectCountVO.setPlanC(getDSHGMapValue(data,"C"));
            }
        }
        //遍历本日完成检定数据
        for (Map<String,Object> data : detailDetectTaskInfoMapList){
            if ("D".equals(data.get("CATEG"))) {
                //当日完成检定单相
                detectCountVO.setCompleteD(getDSHGMapValue(data, "D"));
            }else if("S".equals(data.get("CATEG"))){
                //当日完成检定三相
                detectCountVO.setCompleteS(getDSHGMapValue(data,"S"));
            }else if("H".equals(data.get("CATEG"))){
                //当日完成检定互感器
                detectCountVO.setCompleteH(getDSHGMapValue(data,"H"));
            }else if("C".equals(data.get("CATEG"))){
                //当日完成检定采集终端
                detectCountVO.setCompleteC(getDSHGMapValue(data,"C"));
            }
        }
        //计算单相完成率
        //单相计划数
        Integer planD = detectCountVO.getPlanD();
        //完成单相数
        Integer completeD = detectCountVO.getCompleteD();
        if (planD > 0 && completeD > 0){
            detectCountVO.setCompleteRateD((int) (Double.valueOf(String.format("%.2f", completeD.doubleValue() / planD.doubleValue())) * 100));
        }
        //三相完成率
        //三相计划数量
        Integer planS = detectCountVO.getPlanS();
        //三相实际完成数量
        Integer completeS = detectCountVO.getCompleteS();
        if (planS > 0 && completeS > 0){
            detectCountVO.setCompleteRateS((int) (Double.valueOf(String.format("%.2f", completeS.doubleValue() / planS.doubleValue())) * 100));
        }
        //互感器完成率
        //互感器计划数量
        Integer planH = detectCountVO.getPlanH();
        //互感器实际完成数量
        Integer completeH = detectCountVO.getCompleteH();
        if (planH > 0 && completeH > 0) {
            detectCountVO.setCompleteRateH((int) (Double.valueOf(String.format("%.2f", completeH.doubleValue() / planH.doubleValue())) * 100));
        }
        //采集终端完成率
        //采集终端计划数量
        Integer planC = detectCountVO.getPlanC();
        //采集终端完成数量
        Integer completeC = detectCountVO.getPlanC();
        if (completeC > 0 && completeC > 0){
            detectCountVO.setCompleteRateC((int) (Double.valueOf(String.format("%.2f", completeC.doubleValue() / planC.doubleValue())) * 100));
        }
        return Result.ok(detectCountVO);
    }

    //根据名字查询区域编号id
    private List<String> getList(List<String> data){
        List<String> list = new ArrayList<> ();
        for (String d : data){
            list.add(rightScreenMapper.detailArea(d));
        }
        return list;
    }


    @Override
    public Result<?> faultTypeList(String[] areaId,List<DictModel> dictModels) {
        //0查询所有
        List<FaultTypeVO> detailFaultTypeVoList = new ArrayList<>();
        if (areaId.length == 1 && "0".equals(areaId[0])){
           detailFaultTypeVoList = rightScreenMapper.detailFaultTypeVoList("");
        }else {
            for (String id : areaId) {
                detailFaultTypeVoList.addAll(rightScreenMapper.detailFaultTypeVoList(id));
            }
        }
        if (CollectionUtils.isEmpty(detailFaultTypeVoList)){
            Map map = new HashMap();
            map.put("names","");
            map.put("data","");
            map.put("title","");
            return Result.ok(map);
        }
        System.out.println("------------------------");
        System.out.println(detailFaultTypeVoList);
        System.out.println("------------------------------------------------------");
        //01是机械故障 否则 电测故障

        for (DictModel dict : dictModels){
            for (FaultTypeVO faultTypeVO1 : detailFaultTypeVoList){
                if (faultTypeVO1.getLabe().equals(dict.getValue())){
                    faultTypeVO1.setLabe(dict.getText());
                }
            }
        }


        //组装数据
        List<Map<String, Object>> list = detailFaultTypeVoList.stream().map(item -> parseMap(item)).collect(Collectors.toList());
        Map map = new HashMap();
        map.put("names",detailFaultTypeVoList.stream().map(item -> item.getAreaName() + item.getLabe()).collect(Collectors.toList()));
        map.put("data",list);
        map.put("title","");
        return Result.ok(map);
    }

    @Override
    public Result<?> lineFaultList(String[] areaId) {
        //按流水线统计当天七条线故障的对比，数据取故障视图表
        //0查询所有
        List<LineFaultVO> detailLineFaultVoList = new ArrayList<>();
        if (areaId.length == 1 && "0".equals(areaId[0])){
            detailLineFaultVoList = rightScreenMapper.detailLineFaultVoList("");
        }else {
            for (String id : areaId) {
                detailLineFaultVoList.addAll(rightScreenMapper.detailLineFaultVoList(id));
            }
        }
        if (CollectionUtils.isEmpty(detailLineFaultVoList)){
            Map map = new HashMap();
            map.put("names","");
            map.put("data","");
            map.put("title","");
            return Result.ok(map);
        }
        //组装数据
        Map<String,Object> dataMap = new HashMap<>();
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        detailLineFaultVoList.forEach(item -> {
            Map<String,Object> map = new HashMap<>();
            map.put("name",item.getAreaName());
            map.put("value",item.getNum());
            list.add(map);
        });
        dataMap.put("names",detailLineFaultVoList.stream().map(item -> item.getAreaName()).collect(Collectors.toList()));
        dataMap.put("data",list);
        dataMap.put("title","");
        return Result.ok(dataMap);
    }

    private List<String> getList(String[] data){
        List list = new ArrayList();
        String[] arr = LineArea.LINE_NO.getLineArea();
        for (int i = 0; i < data.length; i++){
            list.add(arr[Integer.valueOf(data[i])]);
        }
        return list;
    }

    //过滤
    private boolean filter(String name,List<String> list){
        for (String str : list){
            if (name.equals(str)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Result<?> faultAreaList(String[] areaId) {
        //查询当天数据
        //0查询所有
        List<FaultAreaVO> detailAreaFaultVoList = new ArrayList<>();
        if (areaId.length == 1 && "0".equals(areaId[0])){
            detailAreaFaultVoList = rightScreenMapper.detailAreaFaultVoList("");
        }else{
            for (String id : areaId){
                detailAreaFaultVoList.addAll(rightScreenMapper.detailAreaFaultVoList(id));
            }
        }
        if (CollectionUtils.isEmpty(detailAreaFaultVoList)){
            Map map = new HashMap();
            map.put("names","");
            map.put("data","");
            map.put("title","");
            return Result.ok(map);
        }
        if (detailAreaFaultVoList.size() > 10){
            //因分类的种类太多，凡是超过10条数据，取前10位（数据库查询时已倒序），然后其他的数据合并到其他当中
            //获取设备总数
            Integer totalAreaCount = detailAreaFaultVoList.stream().mapToInt(item -> item.getNum()).sum();
            //截取数据
            detailAreaFaultVoList = detailAreaFaultVoList.subList(0, 10);
            //组装数据
            List<Map<String, Object>> listMaps = detailAreaFaultVoList.stream().map(item -> parseMap(item)).collect(Collectors.toList());
            //获取前10条的总数
            Integer totalAreaCountTOP10 = detailAreaFaultVoList.stream().mapToInt(item -> item.getNum()).sum();
            //其他
            Map<String,Object> dataMap = new HashMap<>(256);
            dataMap.put("name","其他");
            dataMap.put("value",totalAreaCount - totalAreaCountTOP10);
            listMaps.add(dataMap);
            System.out.println(listMaps);
            Map<String,Object> map = new HashMap<>();
            map.put("data",listMaps);
            List<String> stringList = detailAreaFaultVoList.stream().map(item -> item.getEquipType()).collect(Collectors.toList());
            stringList.add("其他");
            map.put("names",stringList);
            map.put("title","");
            return  Result.ok(map);
        }else{
            //组装数据
            List<Map<String, Object>> listMaps = detailAreaFaultVoList.stream().map(item -> parseMap(item)).collect(Collectors.toList());
            Map<String,Object> map = new HashMap<>();
            map.put("data",listMaps);
            map.put("names",detailAreaFaultVoList.stream().map(item -> item.getEquipType()).collect(Collectors.toList()));
            map.put("title","");
            return Result.ok(map);
        }
    }

    @Override
    public Result<?> detectTrendList(String[] areaId) {
        //查询数据
        //-1全部
        //0单相I
        //1单相2
        //2三相1
        //3三相2
        //4采集1
        //5采集2
        //6互感器
        //0查询所有
        List<DictModel> linkNOResult = rightScreenMapper.detailAreaList();
        List<DetectTrendVO> detailDetectTrendVoList = new ArrayList<>();
        if (areaId.length == 1 && "0".equals(areaId[0])){
            detailDetectTrendVoList = rightScreenMapper.detailDetectTrendVoList("");
        }else{
            for (String id : areaId){
                detailDetectTrendVoList.addAll(rightScreenMapper.detailDetectTrendVoList(id));
            }
        }
        List<DetectTrendVO> list1=new ArrayList<>();//单相Ⅰ
        List<DetectTrendVO> list2=new ArrayList<>();//单相Ⅱ
        List<DetectTrendVO> list3=new ArrayList<>();//三相Ⅰ
        List<DetectTrendVO> list4=new ArrayList<>();//三相Ⅱ
        List<DetectTrendVO> list5=new ArrayList<>();//采集Ⅰ
        List<DetectTrendVO> list6=new ArrayList<>();//采集Ⅱ
        List<DetectTrendVO> list7=new ArrayList<>();//互感器

        Map map1 = new HashMap();//标记单相Ⅰ
        Map map2 = new HashMap();//标记单相Ⅱ
        Map map3 = new HashMap();//标记三相Ⅰ
        Map map4 = new HashMap();//标记三相Ⅱ
        Map map5 = new HashMap();//标记采集Ⅰ
        Map map6 = new HashMap();//标记采集Ⅱ
        Map map7 = new HashMap();//标记互感器
        for (DictModel dictModel : linkNOResult){
            if ("FJ00AREA0000000000001101001".equals(dictModel.getValue() )){
                map1.put("data",dictModel.getText());//初始化
                map1.put("id",dictModel.getValue());
            }else if ("FJ00AREA0000000000001101002".equals(dictModel.getValue())){
                map2.put("id",dictModel.getValue());
                map2.put("data",dictModel.getText());//初始化
            }else if ("FJ00AREA0000000000001201001".equals(dictModel.getValue())){
                map3.put("id",dictModel.getValue());
                map3.put("data",dictModel.getText());//初始化
            }else if ("28ee70730f8ed101730f95eca10002".equals(dictModel.getValue())){
                map4.put("id",dictModel.getValue());
                map4.put("data",dictModel.getText());//初始化
            }else if ("285e0c6e6799a3016e67a808cd0002".equals(dictModel.getValue())){
                map5.put("id",dictModel.getValue());
                map5.put("data",dictModel.getText());//初始化
            }else if ("285e0c6e6799a3016e67a897ef0003".equals(dictModel.getValue())){
                map6.put("data",dictModel.getText());//初始化
                map6.put("id",dictModel.getValue());
            }else if ("FJ00AREA0000000000001301001".equals(dictModel.getValue())){
                map7.put("data",dictModel.getText());//初始化
                map7.put("id",dictModel.getValue());
            }
        }

        //组装小时数据 类似['1','2','3','4','5','6','7','8']
        List<Integer> hourList = new ArrayList<>();
        for (int i = 1; i <= 24; i++){
            hourList.add(i);
        }

        System.out.println(linkNOResult);
        System.out.println(hourList);
        //组装图标数据，类似['总数', '单相I']
        List<String> lineList = filterList(areaId,linkNOResult);
        lineList.add("总数");

        System.out.println(detailDetectTrendVoList);

        if (!CollectionUtils.isEmpty(detailDetectTrendVoList)) {
            for (int i = 0; i < detailDetectTrendVoList.size(); i++) {
                DetectTrendVO detectTrendVO = detailDetectTrendVoList.get(i);
                //将0转为24
                if (detectTrendVO.getHours() == 0) {
                    detectTrendVO.setHours(24);
                }
                if (StringUtils.equals(linkNOResult.get(0).getValue(), detectTrendVO.getAreaId())) {
                    map1.put(detectTrendVO.getHours(), detectTrendVO.getHours());
                    detectTrendVO.setAreaName(linkNOResult.get(0).getText());
                    detectTrendVO.setAreaId(linkNOResult.get(0).getValue());
                    list1.add(detectTrendVO);
                } else if (StringUtils.equals(linkNOResult.get(1).getValue(), detectTrendVO.getAreaId())) {
                    System.out.println(detectTrendVO.getHours());
                    map2.put(detectTrendVO.getHours(), detectTrendVO.getHours());
                    detectTrendVO.setAreaId(linkNOResult.get(1).getValue());
                    detectTrendVO.setAreaName(linkNOResult.get(1).getText());
                    list2.add(detectTrendVO);
                } else if (StringUtils.equals(linkNOResult.get(2).getValue(), detectTrendVO.getAreaId())) {
                    map3.put(detectTrendVO.getHours(), detectTrendVO.getHours());
                    detectTrendVO.setAreaId(linkNOResult.get(2).getValue());
                    detectTrendVO.setAreaName(linkNOResult.get(2).getText());
                    list3.add(detectTrendVO);
                } else if (StringUtils.equals(linkNOResult.get(3).getValue(), detectTrendVO.getAreaId())) {
                    map4.put(detectTrendVO.getHours(), detectTrendVO.getHours());
                    detectTrendVO.setAreaName(linkNOResult.get(3).getText());
                    detectTrendVO.setAreaId(linkNOResult.get(3).getValue());
                    list4.add(detectTrendVO);
                } else if (StringUtils.equals(linkNOResult.get(4).getValue(), detectTrendVO.getAreaId())) {
                    map5.put(detectTrendVO.getHours(), detectTrendVO.getHours());
                    detectTrendVO.setAreaName(linkNOResult.get(4).getText());
                    detectTrendVO.setAreaId(linkNOResult.get(4).getValue());
                    list5.add(detectTrendVO);
                } else if (StringUtils.equals(linkNOResult.get(5).getValue(), detectTrendVO.getAreaId())) {
                    map6.put(detectTrendVO.getHours(), detectTrendVO.getHours());
                    detectTrendVO.setAreaName(linkNOResult.get(5).getText());
                    detectTrendVO.setAreaId(linkNOResult.get(5).getValue());
                    list6.add(detectTrendVO);
                } else if (StringUtils.equals(linkNOResult.get(6).getValue(), detectTrendVO.getAreaId())) {
                    map7.put(detectTrendVO.getHours(), detectTrendVO.getHours());
                    detectTrendVO.setAreaName(linkNOResult.get(6).getText());
                    detectTrendVO.setAreaId(linkNOResult.get(6).getValue());
                    list7.add(detectTrendVO);
                }
            }
        }

        //填充数据
        for (Integer hour : hourList){
            if (!map1.containsKey(hour)){
                DetectTrendVO detectTrendVO = new DetectTrendVO();
                detectTrendVO.setHours(hour);
                detectTrendVO.setAreaName(map1.get("data").toString());
                detectTrendVO.setAreaId(map1.get("id").toString());
                detectTrendVO.setNum(0);
                list1.add(detectTrendVO);
            }
            if (!map2.containsKey(hour)){
                DetectTrendVO detectTrendVO = new DetectTrendVO();
                detectTrendVO.setHours(hour);
                detectTrendVO.setAreaName(map2.get("data").toString());
                detectTrendVO.setAreaId(map2.get("id").toString());
                detectTrendVO.setNum(0);
                list2.add(detectTrendVO);
            }
            if (!map3.containsKey(hour)){
                DetectTrendVO detectTrendVO = new DetectTrendVO();
                detectTrendVO.setHours(hour);
                detectTrendVO.setAreaName(map3.get("data").toString());
                detectTrendVO.setAreaId(map3.get("id").toString());
                detectTrendVO.setNum(0);
                list3.add(detectTrendVO);
            }
            if (!map4.containsKey(hour)){
                DetectTrendVO detectTrendVO = new DetectTrendVO();
                detectTrendVO.setHours(hour);
                detectTrendVO.setAreaId(map4.get("id").toString());
                detectTrendVO.setAreaName(map4.get("data").toString());
                detectTrendVO.setNum(0);
                list4.add(detectTrendVO);
            }
            if (!map5.containsKey(hour)){
                DetectTrendVO detectTrendVO = new DetectTrendVO();
                detectTrendVO.setHours(hour);
                detectTrendVO.setAreaId(map5.get("id").toString());
                detectTrendVO.setAreaName(map5.get("data").toString());
                detectTrendVO.setNum(0);
                list5.add(detectTrendVO);
            }
            if (!map6.containsKey(hour)){
                DetectTrendVO detectTrendVO = new DetectTrendVO();
                detectTrendVO.setHours(hour);
                detectTrendVO.setAreaId(map6.get("id").toString());
                detectTrendVO.setAreaName(map6.get("data").toString());
                detectTrendVO.setNum(0);
                list6.add(detectTrendVO);
            }
            if (!map7.containsKey(hour)){
                DetectTrendVO detectTrendVO = new DetectTrendVO();
                detectTrendVO.setHours(hour);
                detectTrendVO.setAreaId(map7.get("id").toString());
                detectTrendVO.setAreaName(map7.get("data").toString());
                detectTrendVO.setNum(0);
                list7.add(detectTrendVO);
            }
        }
        System.out.println("-----------------" + list1);
        System.out.println("-----------------" + list2);
        System.out.println("-----------------" + list3);
        System.out.println("-----------------" + list4);
        System.out.println("-----------------" + list5);
        System.out.println("-----------------" + list6);
        System.out.println("-----------------" + list7);

        //创建list
        List seriesList = new ArrayList();
        Integer[] arrD1 = new Integer[list1.size()];
        Integer[] arrD2 = new Integer[list2.size()];
        Integer[] arrS1 = new Integer[list3.size()];
        Integer[] arrS2 = new Integer[list4.size()];
        Integer[] arrC1 = new Integer[list5.size()];
        Integer[] arrC2 = new Integer[list6.size()];
        Integer[] arrH = new Integer[list7.size()];
        //提取单相I对应的每时数据
        //根据枚举而决定
        for (String id : areaId) {
            if ("0".equals(id) || "FJ00AREA0000000000001101001".equals(id)) {
                for (int i = 0; i < list1.size(); i++) {
                    DetectTrendVO detectTrendVO = list1.get(i);
                    arrD1[detectTrendVO.getHours() - 1] = detectTrendVO.getNum();
                }
                LineModel lineModel1 = new LineModel();
                lineModel1.setName(map1.get("data").toString());
                lineModel1.setData(arrD1);
                seriesList.add(lineModel1);
            }
            //单相2
            if ("0".equals(id) || "FJ00AREA0000000000001101002".equals(id)) {
                for (int i = 0; i < list2.size(); i++) {
                    DetectTrendVO detectTrendVO = list2.get(i);
                    arrD2[detectTrendVO.getHours() - 1] = detectTrendVO.getNum();
                }
                LineModel lineModel2 = new LineModel();
                lineModel2.setName(map2.get("data").toString());
                lineModel2.setData(arrD2);
                seriesList.add(lineModel2);
            }

            //三相1
            if ("0".equals(id) || "FJ00AREA0000000000001201001".equals(id)) {
                for (int i = 0; i < list3.size(); i++) {
                    DetectTrendVO detectTrendVO = list3.get(i);
                    arrS1[detectTrendVO.getHours() - 1] = detectTrendVO.getNum();
                }
                LineModel lineModel3 = new LineModel();
                lineModel3.setName(map3.get("data").toString());
                lineModel3.setData(arrS1);
                seriesList.add(lineModel3);
                System.out.println("三相1");
            }

            //三相2
            if ("0".equals(id) || "28ee70730f8ed101730f95eca10002".equals(id)) {
                for (int i = 0; i < list4.size(); i++) {
                    DetectTrendVO detectTrendVO = list4.get(i);
                    arrS2[detectTrendVO.getHours() - 1] = detectTrendVO.getNum();
                }
                LineModel lineModel4 = new LineModel();
                lineModel4.setName(map4.get("data").toString());
                lineModel4.setData(arrS2);
                seriesList.add(lineModel4);
                System.out.println("三相2");
            }
            //采集1
            if ("0".equals(id) || "285e0c6e6799a3016e67a808cd0002".equals(id)) {
                for (int i = 0; i < list5.size(); i++) {
                    DetectTrendVO detectTrendVO = list5.get(i);
                    arrC1[detectTrendVO.getHours() - 1] = detectTrendVO.getNum();
                }
                LineModel lineModel5 = new LineModel();
                lineModel5.setName(map5.get("data").toString());
                lineModel5.setData(arrC1);
                seriesList.add(lineModel5);
                System.out.println("采集1");
            }
            //采集2

            if ("0".equals(id) || "285e0c6e6799a3016e67a897ef0003".equals(id)) {
                for (int i = 0; i < list6.size(); i++) {
                    DetectTrendVO detectTrendVO = list6.get(i);
                    arrC2[detectTrendVO.getHours() - 1] = detectTrendVO.getNum();
                }
                LineModel lineModel6 = new LineModel();
                lineModel6.setName(map6.get("data").toString());
                lineModel6.setData(arrC2);
                seriesList.add(lineModel6);
                System.out.println("采集2");
            }

            //互感器
            if ("0".equals(id) || "FJ00AREA0000000000001301001".equals(id)) {
                for (int i = 0; i < list7.size(); i++) {
                    DetectTrendVO detectTrendVO = list7.get(i);
                    arrH[detectTrendVO.getHours() - 1] = detectTrendVO.getNum();
                }
                LineModel lineModel7 = new LineModel();
                lineModel7.setName(map7.get("data").toString());
                lineModel7.setData(arrH);
                seriesList.add(lineModel7);
                System.out.println("互感器");
            }
        }
            //添加总数
            //获取最大值
            int maxNum = 0;
            Integer[] totalArr = new Integer[hourList.size()];
            for (int i = 0; i < hourList.size(); i++) {
                //七条线累加
                int total = 0;
                for (String id : areaId) {
                    if ("0".equals(id) || "FJ00AREA0000000000001101001".equals(id)){
                        total += arrD1[i];
                    }
                    if ("0".equals(id) || "FJ00AREA0000000000001101002".equals(id)){
                        total += arrD2[i];
                    }
                    if ("0".equals(id) || "FJ00AREA0000000000001201001".equals(id)){
                        total += arrS1[i];
                    }
                    if ("0".equals(id) || "28ee70730f8ed101730f95eca10002".equals(id)){
                        total += arrS2[i];
                    }
                    if ("0".equals(id) || "285e0c6e6799a3016e67a808cd0002".equals(id)){
                        total += arrC1[i];
                    }
                    if ("0".equals(id) || "285e0c6e6799a3016e67a897ef0003".equals(id)){
                        total += arrC2[i];
                    }
                    if ("0".equals(id) || "FJ00AREA0000000000001301001".equals(id)){
                        total += arrH[i];
                    }
                    totalArr[i] = total;
                    if (totalArr[i] > maxNum) {
                        maxNum = totalArr[i];
                    }
                }
            }

            BarModel barModel = new BarModel();
            barModel.setData(totalArr);
            barModel.setName("总数");
            seriesList.add(barModel);
        //用map组装所有的值
        Map allData = new HashMap();

        allData.put("series",seriesList);

        allData.put("legend", lineList);
        //设置10个刻度
        int kedu = 5;
        allData.put("max",(maxNum / kedu == 0 ? 1 : maxNum / kedu) * kedu);
        allData.put("step",maxNum / kedu == 0 ? 1 : maxNum / kedu);
        return Result.ok(allData);
    }

    private List<String> filterList(String[] areaId,List<DictModel> linkNOResult){
        if (areaId.length == 1 && "0".equals(areaId[0])){
            return linkNOResult.stream().map(item -> item.getText()).collect(Collectors.toList());
        }else{
            List<String> list = new ArrayList<>();
            for (String id : areaId){
                for(DictModel item : linkNOResult){
                    if (id.equals(item.getValue())){
                        list.add(item.getText());
                    }
                }
            }
            return list;
        }
    }

    @Override
    public Result<?> switchMode() {
        //1代表无任务（灰色），2代表有任务（在检，绿色），无故障，3代码有任务（在检，红色），有故障
        // 若有任务且任务状态为“在检”，则监控图标为绿色，若无任务，则监控图标为灰色。
        //若检定线任务状态为在检，故障视图中检定线有故障，则监控图标为红色，
        Integer status = 1;//默认
        //查询当天的数据
        List<DetectInfoVO> detailDetectInfoVoList = rightScreenMapper.detailDetectInfoVoList();
        Map map = new HashMap();
        map.put("status",status);
        if (CollectionUtils.isEmpty(detailDetectInfoVoList)){
            return Result.ok(map);
        }
        //	0-未执行
        //1-执行中
        //2-执行完毕
        //3-暂停
        //4-中止
        String taskStatus = "1";
        for (int i = 0; i < detailDetectInfoVoList.size(); i++){
            DetectInfoVO item = detailDetectInfoVoList.get(i);
            if (taskStatus.equals(item.getTaskStatus()) && item.getStatus() == 1){
                status = 3;
            }else if (i == detailDetectInfoVoList.size() -1 && status != 3 && item.getStatus() != 1){
                status = 2;
            }
        };
        map.put("status",status);
        return Result.ok(map);
    }

    @Override
    public Result<?> inOutEquipList(String areaId) {
        List<AccessEquipVO> accessEquipVOS;
        if ("0".equals(areaId)) {
             accessEquipVOS = rightScreenMapper.detailStkInOutTEquipInfo("");
        }else{
            accessEquipVOS = rightScreenMapper.detailStkInOutTEquipInfo(areaId);
        }
        return Result.ok(accessEquipVOS);
    }

    //过滤检定线区域
    private boolean filterAreaName(String areaName){
        String[] lineArea = LineArea.LINE_NO.getLineArea();
        boolean contains = Arrays.asList(lineArea).contains(areaName);
        System.out.println(areaName +"-----------------------------" + contains );
        return contains;
    }

    //组装为map
    private Map<String,Object> parseMap(FaultAreaVO faultAreaVO){
        Map<String,Object> map = new HashMap(256);
        map.put("name",faultAreaVO.getEquipType());
        map.put("value",faultAreaVO.getNum());
        return map;
    }

    //组装为map
    private Map<String,Object> parseMap(FaultTypeVO faultTypeVO){
        //统计名称等价于 区域名 + 类型名称
        String item = faultTypeVO.getAreaName() + faultTypeVO.getLabe();
        Integer count = faultTypeVO.getNum();
        Map<String,Object> map = new HashMap(256);
        map.put("name",item);
        map.put("value",count);
        return map;
    }

    //组装为map
/*    private Map<String, Object> parseMap(LineFaultVO lineFaultVO){
        Map<String,Object> map = new HashMap(256);
        map.put("item",lineFaultVO.getArea());
        map.put("count",lineFaultVO.getNum());
        return map;
    }*/


    /**
     * 完成检定数（可复用）
     */
    private Integer getDSHGCount(List<Map<String, Object>> detailDetectTaskInfoMapList){
        //单相数量
        Integer numD = 0;
        //三相数量
        Integer numS = 0;
        //互感器
        Integer numH = 0;
        //采集终端
        Integer numC = 0;
        for (Map<String,Object> item : detailDetectTaskInfoMapList){
            if ("D".equals(item.get("CATEG"))) {
                numD= getDSHGMapValue(item, "D");
            }else if("S".equals(item.get("CATEG"))){
                numS = getDSHGMapValue(item,"S");
            }else if("H".equals(item.get("CATEG"))){
                numH = getDSHGMapValue(item,"H");
            }else if("C".equals(item.get("CATEG"))){
                numC = getDSHGMapValue(item,"C");
            }
        }
        return numC + numD + numS + numH;
    }

    /**
     * 获取对应设备类别检定数量
     * @param dataMap
     * @param key
     * @return
     */
    private Integer getDSHGMapValue(Map<String,Object> dataMap,String key){
        if (key.equals(dataMap.get("CATEG"))){
            if (dataMap.get("NUM") != null) {
                return Integer.valueOf(dataMap.get("NUM").toString());
            }
        }
        return 0;
    }

    /**
     * 本月待检总数
     * @param devProduceCycle
     * @return
     */
    private Integer getDSHGCount(DevProduceCycle devProduceCycle){
        //单相数量
        Integer numD = devProduceCycle.getCycleSumD() == null ? 0 : devProduceCycle.getCycleSumD();
        //三相数量
        Integer numS = devProduceCycle.getCycleSumS() == null ? 0 : devProduceCycle.getCycleSumS();
        //互感器
        Integer numH = devProduceCycle.getCycleSumH() == null ? 0 : devProduceCycle.getCycleSumH();
        //采集终端
        Integer numC = devProduceCycle.getCycleSumC() == null ? 0 : devProduceCycle.getCycleSumC();
        Integer total = numC + numD + numS + numH;
        System.out.println("total -------------------" + total);
        return total;
    }

    /**
     * 计算处理时长
     * @param alarmInfoVO
     * @return
     */
    private AlarmInfoVO computeHandleTime(AlarmInfoVO alarmInfoVO){
        //处理时长 = 报警时间 - 结束时间
        //报警时间
        Date time = alarmInfoVO.getAlarmTime();
        if (time == null){
            return alarmInfoVO;
        }
        long alarmTime = time.getTime();
        //结束时间
        time = alarmInfoVO.getEndTime();
        if (time == null){
            return alarmInfoVO;
        }
        long endTime = time.getTime();
        //计算差多少秒
        alarmInfoVO.setHandleTime((int) (Math.abs(alarmTime - endTime) / 1000));
        return alarmInfoVO;
    }

    private boolean filterCheckList(DetectTaskInfoVO detectTaskInfoVO){
        // 1、根据检定任务编号，统计表位信息表的合格量和不合格量个数
        //01：合格；02：不合格；03：未检
        Integer qualified = queryCount(detectTaskInfoVO.getTaskId(),"01");
        Integer unQualified = queryCount(detectTaskInfoVO.getTaskId(),"02");
        // 2、合格量与T_DETECT_TASK中的合格数量、不合格数量一致则无异常
        TDetectTask tDetectTask = queryDetectTask(detectTaskInfoVO.getTaskId(), detectTaskInfoVO.getBatchId());
        // 3、检定任务信息的设备数量 = 检定任务的检定次数
        //数据不存在为true，过滤掉不存在的数据
        if (detectTaskInfoVO == null || tDetectTask == null){
            return false;
        }
        //必须是执行完成状态
        //00-未执行
        //01-执行中
        //02-执行完毕
        //03-暂停
        //04-中止
        //先比较总量
        if (tDetectTask.getDetectQty().equals(detectTaskInfoVO.getTotalTask()) && TaskStatus.COMPLETE.getCode().equals(detectTaskInfoVO.getTaskStatus())){
            if (qualified.equals(tDetectTask.getDetectConcP()) && unQualified.equals(tDetectTask.getDetectConcU())){
                return true;
            }
        }
        return false;
    }

     enum TaskStatus {
        COMPLETE("02");
         private String code;
        TaskStatus(String code) {
            this.code = code;
        }
         public String getCode() {
             return code;
         }

     }

     enum LineArea{
        LINE_NO(new String[]{"单相Ⅰ","单相Ⅱ","三相Ⅰ","三相Ⅱ","采集Ⅰ","采集Ⅱ","互感器"});
        private String[] lineArea;

        private LineArea(String[] LineArea){
            this.lineArea = LineArea;
        }

         public String[] getLineArea() {
             return lineArea;
         }
     }

    private Integer queryCount(String taskNo,String status){
        QueryWrapper<MtPositionDetectInfo> wrapper = new QueryWrapper();
        wrapper.eq("TASK_NO",taskNo);
        wrapper.eq("DET_CONC_CODE",status);
        List<MtPositionDetectInfo> mtPositionDetectInfoList = mtPositionDetectInfoMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(mtPositionDetectInfoList)){
            return 0;
        }
        return mtPositionDetectInfoList.size();
    }

    private TDetectTask queryDetectTask(String taskId,String batchId) {
        QueryWrapper<TDetectTask> queryWrapper = new QueryWrapper();
        queryWrapper.eq("TASK_NO",taskId);
        queryWrapper.eq("ARRIVE_BATCH_NO",batchId);
        List<TDetectTask> tDetectTasks = tDetectTaskMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tDetectTasks)){
            return null;
        }
        return tDetectTasks.get(0);
    }

}
