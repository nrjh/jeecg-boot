package com.nrjh.iop.modules.prd.attribute.service;

import com.nrjh.iop.modules.prd.attribute.entity.PrdAttributeValue;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 属性值
 * @Author: jeecg-boot
 * @Date:   2020-04-17
 * @Version: V1.0
 */
public interface IPrdAttributeValueService extends IService<PrdAttributeValue> {

	public List<PrdAttributeValue> selectByMainId(String mainId);
}
