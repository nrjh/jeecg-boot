package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.TDetectTask;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper.TDetectTaskMapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.ITDetectTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: T_DETECT_TASK
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Service
@DS("ora")
@Slf4j
public class TDetectTaskServiceImpl extends ServiceImpl<TDetectTaskMapper, TDetectTask> implements ITDetectTaskService {

    @Override
    public TDetectTask getInfoByDetectTaskNo(String detectTaskNo,String arriveBatchNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("TASK_NO",detectTaskNo);
        queryWrapper.eq("ARRIVE_BATCH_NO",arriveBatchNo);
        TDetectTask tDetectTask = baseMapper.selectOne(queryWrapper);
        if(BeanUtil.isEmpty(tDetectTask)){
            log.error("检定任务单查询失败，检定任务号：{}，到货批次号:{}",detectTaskNo,arriveBatchNo);
        }
        return tDetectTask;
    }
}
