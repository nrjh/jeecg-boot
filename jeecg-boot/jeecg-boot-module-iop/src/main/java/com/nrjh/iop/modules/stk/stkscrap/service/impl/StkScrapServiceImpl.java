package com.nrjh.iop.modules.stk.stkscrap.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.stk.stkscrap.entity.StkScrap;
import com.nrjh.iop.modules.stk.stkscrap.mapper.StkScrapMapper;
import com.nrjh.iop.modules.stk.stkscrap.service.IStkScrapService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 报废管理
 * @Author: jeecg-boot
 * @Date:   2020-08-06
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkScrapServiceImpl extends ServiceImpl<StkScrapMapper, StkScrap> implements IStkScrapService {

}
