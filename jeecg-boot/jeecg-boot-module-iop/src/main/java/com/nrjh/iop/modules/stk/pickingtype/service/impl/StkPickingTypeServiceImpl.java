package com.nrjh.iop.modules.stk.pickingtype.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.stk.pickingtype.entity.StkPickingType;
import com.nrjh.iop.modules.stk.pickingtype.mapper.StkPickingTypeMapper;
import com.nrjh.iop.modules.stk.pickingtype.service.IStkPickingTypeService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 作业类型
 * @Author: jeecg-boot
 * @Date:   2020-04-28
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkPickingTypeServiceImpl extends ServiceImpl<StkPickingTypeMapper, StkPickingType> implements IStkPickingTypeService {

}
