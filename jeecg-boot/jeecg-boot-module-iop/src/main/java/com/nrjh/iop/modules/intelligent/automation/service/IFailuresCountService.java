package com.nrjh.iop.modules.intelligent.automation.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.intelligent.automation.entity.FailuresCount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: FAILURES_COUNT
 * @Author: jeecg-boot
 * @Date:   2020-10-14
 * @Version: V1.0
 */
@DS("ora")
public interface IFailuresCountService extends IService<FailuresCount> {

}
