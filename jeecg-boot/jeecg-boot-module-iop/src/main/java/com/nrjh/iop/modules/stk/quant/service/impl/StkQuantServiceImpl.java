package com.nrjh.iop.modules.stk.quant.service.impl;

import com.nrjh.iop.modules.stk.quant.entity.StkQuant;
import com.nrjh.iop.modules.stk.quant.mapper.StkQuantMapper;
import com.nrjh.iop.modules.stk.quant.service.IStkQuantService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 库存数量
 * @Author: jeecg-boot
 * @Date:   2020-06-23
 * @Version: V1.0
 */
@Service
public class StkQuantServiceImpl extends ServiceImpl<StkQuantMapper, StkQuant> implements IStkQuantService {

}
