package com.nrjh.iop.modules.intelligent.automation.service.impl;

import com.nrjh.iop.modules.intelligent.automation.entity.FailuresCount;
import com.nrjh.iop.modules.intelligent.automation.mapper.FailuresCountMapper;
import com.nrjh.iop.modules.intelligent.automation.service.IFailuresCountService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: FAILURES_COUNT
 * @Author: jeecg-boot
 * @Date:   2020-10-14
 * @Version: V1.0
 */
@Service
public class FailuresCountServiceImpl extends ServiceImpl<FailuresCountMapper, FailuresCount> implements IFailuresCountService {

}
