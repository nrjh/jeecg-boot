package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.MtPositionDetectInfo;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper.MtPositionDetectInfoMapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.IMtPositionDetectInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @Description: MT_POSITION_DETECT_INFO
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@DS("ora")
@Service
@Slf4j
public class MtPositionDetectInfoServiceImpl extends ServiceImpl<MtPositionDetectInfoMapper, MtPositionDetectInfo> implements IMtPositionDetectInfoService {

    @Override
    public MtPositionDetectInfo getInfo(String taskNo) {
        QueryWrapper q1 = new QueryWrapper();
        q1.eq("TASK_NO",taskNo);
        MtPositionDetectInfo mtPositionDetectInfo = baseMapper.selectOne(q1);
        if(BeanUtil.isEmpty(mtPositionDetectInfo)){
            log.error("查询表位检定结果信息为空，检定任务编号:{}",taskNo);
        }
        return mtPositionDetectInfo;
    }

    @Override
    public Integer getUnQualified(String taskNo,Date startDate,Date endDate) {
        QueryWrapper<MtPositionDetectInfo> wrapper = new QueryWrapper();
        wrapper.eq("TASK_NO",taskNo);
        wrapper.eq("DET_CONC_CODE","02");
        wrapper.ge("DETECT_START_DATE",startDate);
        wrapper.le("DETECT_END_DATE",endDate);
        List<MtPositionDetectInfo> mtPositionDetectInfos = baseMapper.selectList(wrapper);
        return mtPositionDetectInfos.size();
    }

    @Override
    public Integer getQualified(String taskNo, Date startDate, Date endDate) {
        QueryWrapper<MtPositionDetectInfo> wrapper = new QueryWrapper();
        wrapper.eq("TASK_NO",taskNo);
        wrapper.eq("DET_CONC_CODE","01");
        wrapper.ge("DETECT_START_DATE",startDate);
        wrapper.le("DETECT_END_DATE",endDate);
        List<MtPositionDetectInfo> mtPositionDetectInfos = baseMapper.selectList(wrapper);
        return mtPositionDetectInfos.size();
    }

    @Override
    public Integer hadCheckByTaskNo(String taskNo) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("DET_CONC_CODE","03");
        Integer hadCheckNum = baseMapper.selectCount(queryWrapper);
        return hadCheckNum;
    }


}
