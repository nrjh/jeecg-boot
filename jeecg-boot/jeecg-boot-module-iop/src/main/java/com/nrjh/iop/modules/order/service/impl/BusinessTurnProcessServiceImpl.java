package com.nrjh.iop.modules.order.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.order.entity.BusinessTurnProcess;
import com.nrjh.iop.modules.order.mapper.BusinessTurnProcessMapper;
import com.nrjh.iop.modules.order.service.IBusinessTurnProcessService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: business_turn_process
 * @Author: jeecg-boot
 * @Date:   2020-10-24
 * @Version: V1.0
 */
@Service
@DS("iop")
public class BusinessTurnProcessServiceImpl extends ServiceImpl<BusinessTurnProcessMapper, BusinessTurnProcess> implements IBusinessTurnProcessService {

}
