package com.nrjh.iop.modules.message.trackingvalue.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.message.trackingvalue.entity.MessageTrackingValue;
import com.nrjh.iop.modules.message.trackingvalue.mapper.MessageTrackingValueMapper;
import com.nrjh.iop.modules.message.trackingvalue.service.IMessageTrackingValueService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 变更记录
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Service
@DS("iop")
public class MessageTrackingValueServiceImpl extends ServiceImpl<MessageTrackingValueMapper, MessageTrackingValue> implements IMessageTrackingValueService {

}
