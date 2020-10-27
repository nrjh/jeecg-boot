package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.DIoDet;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper.DIoDetMapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.IDIoDetService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: D_IO_DET
 * @Author: jeecg-boot
 * @Date:   2020-09-10
 * @Version: V1.0
 */
@Service
@DS("ora")
public class DIoDetServiceImpl extends ServiceImpl<DIoDetMapper, DIoDet> implements IDIoDetService {

}
