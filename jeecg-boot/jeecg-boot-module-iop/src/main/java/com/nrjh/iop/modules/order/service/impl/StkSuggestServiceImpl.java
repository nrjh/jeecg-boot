package com.nrjh.iop.modules.order.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.order.entity.StkSuggest;
import com.nrjh.iop.modules.order.mapper.StkSuggestMapper;
import com.nrjh.iop.modules.order.service.IStkSuggestService;
import org.springframework.stereotype.Service;

/**
 * @Description: stk_suggest
 * @Author: jeecg-boot
 * @Date:   2020-08-18
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkSuggestServiceImpl extends ServiceImpl<StkSuggestMapper, StkSuggest> implements IStkSuggestService {

}
