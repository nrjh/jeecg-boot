package com.nrjh.iop.modules.workOrder.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.workOrder.entity.OrdRedeployInfo;
import com.nrjh.iop.modules.workOrder.mapper.OrdRedeployInfoMapper;
import com.nrjh.iop.modules.workOrder.service.IOrdRedeployInfoService;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author Kou Shenhai 2413176044@leimingtech.com
 * @version 1.0
 * @date 2020/9/8 0008 上午 10:16
 */
@Service
@DS("ora")
public class OrdRedeployInfoServiceImpl  extends ServiceImpl<OrdRedeployInfoMapper, OrdRedeployInfo> implements IOrdRedeployInfoService {
}
