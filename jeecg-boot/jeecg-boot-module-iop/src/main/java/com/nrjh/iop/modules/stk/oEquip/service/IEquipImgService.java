package com.nrjh.iop.modules.stk.oEquip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.stk.oEquip.entity.EquipImg;

import java.util.List;

/**
 * @Description: 设备图片表
 * @Author: jeecg-boot
 * @Date:   2020-10-11
 * @Version: V1.0
 */
public interface IEquipImgService extends IService<EquipImg> {
    EquipImg selectBymainId(String mainId);

}
