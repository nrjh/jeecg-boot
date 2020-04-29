package com.nrjh.iop.modules.uom.category.service;

import com.nrjh.iop.modules.uom.category.entity.UomUom;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 计量单位
 * @Author: jeecg-boot
 * @Date:   2020-04-29
 * @Version: V1.0
 */
public interface IUomUomService extends IService<UomUom> {

	public List<UomUom> selectByMainId(String mainId);
}
