package com.nrjh.iop.modules.reserve.store.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.reserve.store.entity.OReserveStore;
import com.nrjh.iop.modules.reserve.store.mapper.OReserveStoreMapper;
import com.nrjh.iop.modules.reserve.store.service.IOReserveStoreService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: O_RESERVE_STORE
 * @Author: jeecg-boot
 * @Date:   2020-03-06
 * @Version: V1.0
 */
@Service
@DS("iop")
public class OReserveStoreServiceImpl extends ServiceImpl<OReserveStoreMapper, OReserveStore> implements IOReserveStoreService {

}
