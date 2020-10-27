package com.nrjh.iop.modules.prd.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.prd.product.entity.PrdProductAttrValueRel;
import com.nrjh.iop.modules.prd.product.entity.PrdProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nrjh.iop.modules.prd.product.vo.RefQuery;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 物品
 * @Author: jeecg-boot
 * @Date: 2020-05-06
 * @Version: V1.0
 */
public interface IPrdProductService extends IService<PrdProduct> {

    /**
     * 添加一对多
     */
    public void saveMain(PrdProduct prdProduct, List<PrdProductAttrValueRel> prdProductAttrValueRelList);

    /**
     * 修改一对多
     */
    public void updateMain(PrdProduct prdProduct, List<PrdProductAttrValueRel> prdProductAttrValueRelList);

    /**
     * 删除一对多
     */
    public void delMain(String id);

    /**
     * 批量删除一对多
     */
    public void delBatchMain(Collection<? extends Serializable> idList);

    /**
     * 根据关联信息查询物品
     */
    public IPage<PrdProduct> refPage(Page page, RefQuery refQuery);

}
