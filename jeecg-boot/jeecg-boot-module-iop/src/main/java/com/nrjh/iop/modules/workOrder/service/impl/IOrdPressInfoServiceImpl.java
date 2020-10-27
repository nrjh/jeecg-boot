package com.nrjh.iop.modules.workOrder.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.workOrder.entity.OrdPressInfo;
import com.nrjh.iop.modules.workOrder.mapper.OrdPressInfoMapper;
import com.nrjh.iop.modules.workOrder.service.IOrdPressInfoService;
import org.springframework.stereotype.Service;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/7 17:36
 */
@Service
@DS("ora")
public class IOrdPressInfoServiceImpl extends ServiceImpl<OrdPressInfoMapper, OrdPressInfo> implements IOrdPressInfoService {
}
