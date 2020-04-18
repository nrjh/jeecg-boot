package com.nrjh.iop.modules.prd.brand.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.prd.brand.entity.PrdBrand;
import com.nrjh.iop.modules.prd.brand.mapper.PrdBrandMapper;
import com.nrjh.iop.modules.prd.brand.service.IPrdBrandService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 品牌
 * @Author: jeecg-boot
 * @Date:   2020-04-16
 * @Version: V1.0
 */
@Service
@DS("iop")
public class PrdBrandServiceImpl extends ServiceImpl<PrdBrandMapper, PrdBrand> implements IPrdBrandService {

}
