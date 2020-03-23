package com.nrjh.iop.modules.reserve.pallet.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.reserve.pallet.entity.IopOReservePallet;
import com.nrjh.iop.modules.reserve.pallet.mapper.IopOReservePalletMapper;
import com.nrjh.iop.modules.reserve.pallet.service.IIopOReservePalletService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 托盘维护
 * @Author: jeecg-boot
 * @Date:   2020-03-16
 * @Version: V1.0
 */
@Service
@DS("iop")
public class IopOReservePalletServiceImpl extends ServiceImpl<IopOReservePalletMapper, IopOReservePallet> implements IIopOReservePalletService {

}
