package com.nrjh.iop.modules.stk.oEquip.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.stk.oEquip.entity.EquipImg;
import com.nrjh.iop.modules.stk.oEquip.mapper.EquipImgMapper;
import com.nrjh.iop.modules.stk.oEquip.service.IEquipImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 设备图片表
 * @Author: jeecg-boot
 * @Date:   2020-10-11
 * @Version: V1.0
 */
@Service
@DS("ora")
public class EquipImgServiceImpl extends ServiceImpl<EquipImgMapper, EquipImg> implements IEquipImgService {
    @Autowired
    EquipImgMapper equipImgMapper;

    @Override
    public EquipImg selectBymainId(String mainId) {
        return equipImgMapper.selectByMainId(mainId);
    }
}
