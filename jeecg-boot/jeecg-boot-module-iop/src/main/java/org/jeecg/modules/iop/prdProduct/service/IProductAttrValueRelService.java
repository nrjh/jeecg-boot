package org.jeecg.modules.iop.prdProduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.iop.prdProduct.entity.ProductAttrValueRel;

import java.util.List;

/**
 * @Description: 备品备件属性值关系
 * @Author: jeecg-boot
 * @Date:   2020-08-06
 * @Version: V1.0
 */
public interface IProductAttrValueRelService extends IService<ProductAttrValueRel> {

	public List<ProductAttrValueRel> selectByMainId(String mainId);
}
