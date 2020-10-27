package com.nrjh.iop.modules.alarm.bind.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.alarm.bind.entity.FaultGradeInfo;
import com.nrjh.iop.modules.alarm.bind.mapper.FaultGradeInfoMapper;
import com.nrjh.iop.modules.alarm.bind.service.IFaultGradeInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: fault_grade_info
 * @Author: jeecg-boot
 * @Date: 2020-08-27
 * @Version: V1.0
 */
@Service
@DS("ora")
public class FaultGradeInfoServiceImpl extends ServiceImpl<FaultGradeInfoMapper, FaultGradeInfo> implements IFaultGradeInfoService {

}
