package com.nrjh.iop.modules.stk.spareparts.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nrjh.iop.modules.prd.product.entity.PrdProduct;
import com.nrjh.iop.modules.prd.product.mapper.PrdProductMapper;
import com.nrjh.iop.modules.stk.spareparts.entity.StkSpareParts;
import com.nrjh.iop.modules.stk.spareparts.entity.StkStock;
import com.nrjh.iop.modules.stk.spareparts.mapper.StkSparePartsMapper;
import com.nrjh.iop.modules.stk.spareparts.mapper.StkStockMapper;
import com.nrjh.iop.modules.stk.spareparts.service.IStkSparePartsService;
import lombok.val;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 备品备件
 * @Author: jeecg-boot
 * @Date:   2020-08-19
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkSparePartsServiceImpl extends ServiceImpl<StkSparePartsMapper, StkSpareParts> implements IStkSparePartsService {

    @Autowired
    private PrdProductMapper prdProductMapper;

    @Autowired
    private StkStockMapper stkStockMapper;

    @Override
    @Transactional
    public void saveProdAndStock(StkSpareParts stkSpareParts) {
        if(stkSpareParts == null){
            return ;
        }
        //添加基础信息到物品表
        PrdProduct prd = new PrdProduct();
        prd.setName(stkSpareParts.getProductName());//设置物品名称
        prd.setDefaultCode(stkSpareParts.getProductNo());//设置物品编码
        prd.setCategoryId(stkSpareParts.getCategoryId());//品类
        prd.setBrandId(stkSpareParts.getBrandId());//品牌
        prd.setVendorId(stkSpareParts.getVendorId());//供应商
        prd.setBelongEquip(stkSpareParts.getJoinParts());//所属生产设施
        prd.setUomId(stkSpareParts.getUomId());//单位

        prdProductMapper.insert(prd);
        //保存物品信息成功后，把物品id回传给备品备件表
        stkSpareParts.setProId(prd.getId());

        //添加扩展信息到库存表
        StkStock stkStock = new StkStock();

        stkStock.setProductId(prd.getId());//物品id
        stkStock.setCategoryType("spare");//"物品类型，equip:生产设施，spare:备品备件，office_supplies:办公用品"
        stkStock.setStatus("normal");//设置状态 freeze：冻结 normal：正常 zero: 库存为0
        stkStock.setProductLotId(2);//定义新增备品备件默认走LS合同号
        stkStock.setProductNo(stkSpareParts.getProductNo());//物品编号
        stkStock.setRpoductName(stkSpareParts.getProductName());//物品名称
        stkStock.setRpoductNameAbb(stkSpareParts.getProductNameAbb());//缩写
        stkStock.setAttributeCategoryId(stkSpareParts.getAttributeCategoryId());//规格
        stkStock.setBrandId(stkSpareParts.getBrandId());//品牌
        stkStock.setCategoryId(stkSpareParts.getCategoryId());//品类
        stkStock.setLocationId(stkSpareParts.getLocationId());//库位
        stkStock.setInitQty(stkSpareParts.getInitQty());//初始数量
        stkStock.setIntSumPrice(stkSpareParts.getInitAmount());//初始总金额
        stkStock.setProductQty(stkSpareParts.getInitQty());//新建时初始库存就是库存数量
        stkStock.setPrice(stkSpareParts.getInitAmount());//金额
        stkStock.setStockMin(stkSpareParts.getStockMin());//最低库存
        stkStock.setStockMax(stkSpareParts.getStockMax());//最高库存
        stkStock.setProductUomId(stkSpareParts.getUomId());//单位
        stkStock.setRemark(stkSpareParts.getRemark());//备注
        stkStock.setVirtualQty(stkSpareParts.getInitQty());//虚拟数量

        stkStockMapper.insert(stkStock);
       //保存库存信息成功后，把库存id回传给备品备件表
        stkSpareParts.setStockId(stkStock.getId());
    }

    @Override
    @Transactional(rollbackFor = JeecgBootException.class)
    public void updateProdAndStock(StkSpareParts stkSpareParts) {
        if(stkSpareParts == null){
            return ;
        }
        //获取修改的物品id
        int proID = stkSpareParts.getProId();
        //通过物品id查到物品对象
        PrdProduct prdProduct = prdProductMapper.selectById(proID);

        // 获取stkSpareParts中的物品信息
        //  设置物品名称
        prdProduct.setName(stkSpareParts.getProductName());
        //  设置物品编码
        prdProduct.setDefaultCode(stkSpareParts.getProductNo());
        //  品类
        prdProduct.setCategoryId(stkSpareParts.getCategoryId());
        //  品牌
        prdProduct.setBrandId(stkSpareParts.getBrandId());
        //  供应商
        prdProduct.setVendorId(stkSpareParts.getVendorId());
        //  所属生产设施
        prdProduct.setBelongEquip(stkSpareParts.getJoinParts());
        //  单位
        prdProduct.setUomId(stkSpareParts.getUomId());

        //修改物品信息
        prdProductMapper.updateById(prdProduct);

        int stockID = stkSpareParts.getStockId();
        StkStock stkStock = stkStockMapper.selectById(stockID);

        stkStock.setProductId(proID);//物品id
        stkStock.setCategoryType("office_supplies");//"物品类型，equip:生产设施，spare:备品备件，office_supplies:办公用品"
        stkStock.setStatus("normal");//设置状态 freeze：冻结 normal：正常 zero: 库存为0
        stkStock.setProductLotId(2);//定义新增备品备件默认走LS合同号
        stkStock.setProductNo(stkSpareParts.getProductNo());//物品编号
        stkStock.setRpoductName(stkSpareParts.getProductName());//物品名称
        stkStock.setRpoductNameAbb(stkSpareParts.getProductNameAbb());//缩写
        stkStock.setAttributeCategoryId(stkSpareParts.getAttributeCategoryId());//规格
        stkStock.setBrandId(stkSpareParts.getBrandId());//品牌
        stkStock.setCategoryId(stkSpareParts.getCategoryId());//品类
        stkStock.setLocationId(stkSpareParts.getLocationId());//库位
        stkStock.setInitQty(stkSpareParts.getInitQty());//初始数量
        stkStock.setIntSumPrice(stkSpareParts.getInitAmount());//初始总金额
        stkStock.setProductQty(stkSpareParts.getInitQty());//新建时初始库存就是库存数量
        stkStock.setPrice(stkSpareParts.getInitAmount());//金额
        stkStock.setStockMin(stkSpareParts.getStockMin());//最低库存
        stkStock.setStockMax(stkSpareParts.getStockMax());//最高库存
        stkStock.setProductUomId(stkSpareParts.getUomId());//单位
        stkStock.setRemark(stkSpareParts.getRemark());//备注
        stkStock.setVirtualQty(stkSpareParts.getInitQty());//虚拟数量

        stkStockMapper.updateById(stkStock);

        baseMapper.updateById(stkSpareParts);
    }

    @Override
    @Transactional(rollbackFor = JeecgBootException.class)
    public void deleteProdAndStock(String id) {
        if(id.isEmpty()){
            return ;
        }
        //通过id获取备品备件对象
        StkSpareParts stkSpareParts = this.getById(id);
        //删除物品信息
        prdProductMapper.deleteById(stkSpareParts.getProId());
        //删除库存信息
        stkStockMapper.deleteById(stkSpareParts.getStockId());

    }

}
