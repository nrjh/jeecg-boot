package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.EquipRemoteUser;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.mapper.EquipRemoteUserMapper;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.IEquipRemoteUserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 设备远程使用情况
 * @Author: jeecg-boot
 * @Date:   2020-09-28
 * @Version: V1.0
 */
@Service
@DS("ora")
public class EquipRemoteUserServiceImpl extends ServiceImpl<EquipRemoteUserMapper, EquipRemoteUser> implements IEquipRemoteUserService {

}
