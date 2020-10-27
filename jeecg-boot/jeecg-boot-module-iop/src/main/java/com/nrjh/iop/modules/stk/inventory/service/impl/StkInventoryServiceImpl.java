package com.nrjh.iop.modules.stk.inventory.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.stk.inventory.entity.InventoryProdVo;
import com.nrjh.iop.modules.stk.inventory.entity.StkInventory;
import com.nrjh.iop.modules.stk.inventory.entity.StkInventoryLine;
import com.nrjh.iop.modules.stk.inventory.entity.StkInventoryRel;
import com.nrjh.iop.modules.stk.inventory.mapper.StkInventoryMapper;
import com.nrjh.iop.modules.stk.inventory.model.StkInventoryModel;
import com.nrjh.iop.modules.stk.inventory.service.IStkInventoryLineService;
import com.nrjh.iop.modules.stk.inventory.service.IStkInventoryRelService;
import com.nrjh.iop.modules.stk.inventory.service.IStkInventoryService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: stk_inventory
 * @Author: jeecg-boot
 * @Date:   2020-08-04
 * @Version: V1.0
 */
@Service
@DS("iop")
public class StkInventoryServiceImpl extends ServiceImpl<StkInventoryMapper, StkInventory> implements IStkInventoryService {

    @Autowired
    private StkInventoryMapper stkInventoryMapper;

    @Autowired
    private IStkInventoryRelService  iStkInventoryRelService;
    @Autowired
    private IStkInventoryLineService iStkInventoryLineService;

    //静态变量存储最大值
    private static final AtomicInteger atomicNum = new AtomicInteger();
    //初始化分组编号
    private final int initIdNum = 0;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInventoryDeal(StkInventoryModel stkInventoryModel) throws BusinessException {
        try{
            StkInventory stkInventory=new StkInventory();

            BeanUtils.copyProperties(stkInventoryModel, stkInventory);

//            stkInventory.setCompanyId(stkInventoryModel.getCompanyId());
//            stkInventory.setPlanStartTime(stkInventoryModel.getPlanStartTime());
//            stkInventory.setPlanEndTime(stkInventoryModel.getPlanEndTime());
//            stkInventory.setRemark(stkInventoryModel.getRemark());
//            stkInventory.setTaskNo(stkInventoryModel.getTaskNo());
//            stkInventory.setName(stkInventoryModel.getName());

            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

//            // 获取年月日
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            String date = sdf.format(new Date());
//            // 获取最大主键id值
//            Integer maxIdNum = stkInventoryMapper.getMaxIdNum();
//            if(maxIdNum == null){
//                maxIdNum = initIdNum;
//            }
//            atomicNum.set(maxIdNum);
//            // 自增操作
//            int newIdNum = atomicNum.incrementAndGet();
//            int maxId = 1000;
//            String newNum = newIdNum + "";
//            if(newIdNum < maxId){
//                newNum = String.format("%03d",newIdNum);
//            }
//            // 获取盘点管理任务编码
//            String taskNo = "PD" + date + newNum;
//            stkInventory.setTaskNo(taskNo);
            stkInventory.setCreateTime(new Date());
            stkInventory.setState("draft");//新建
            stkInventory.setCreateBy(sysUser.getRealname());
            stkInventory.setExecBy(stkInventoryModel.getExecBy());
            this.save(stkInventory);

            int inventoryId= stkInventory.getId();
            log.debug("---inventoryId---"+inventoryId);
            if(StringUtils.isNotBlank(stkInventoryModel.getStockLocationId())){
                String[] locationIds=stkInventoryModel.getStockLocationId().split(",");
                for (int i=0;i<locationIds.length;i++){
                    StkInventoryRel stkInventoryRel=new StkInventoryRel();
                    stkInventoryRel.setLocationId(Integer.valueOf(locationIds[i]));
                    stkInventoryRel.setIinventoryId(inventoryId);
                    stkInventoryRel.setCreateTime(new Date());
                    stkInventoryRel.setCreateBy(sysUser.getRealname());
                    iStkInventoryRelService.save(stkInventoryRel);
                }
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException("业务异常");
        }


    }

    @Override
    public void updateInventory(String id,String opt) {
        StkInventory stkInventory = this.getById(id);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        stkInventory.setUpdateBy(sysUser.getUsername());
        stkInventory.setUpdateTime(new Date());
        if(StringUtils.equals("send",opt)){
            stkInventory.setSendTime(new Date());
            stkInventory.setState("confirm");
            stkInventory.setIssueState("confirm");
            this.updateById(stkInventory);
        }else if(StringUtils.equals("cancel",opt)){
            stkInventory.setState("cancel");
            this.updateById(stkInventory);
        }
    }

    @Override
    public List<InventoryProdVo> queryProdList(String inventoryId) {
        List<InventoryProdVo> result=stkInventoryMapper.queryProdList(inventoryId);
        return result;
    }

    @Override
    public void dealInventoryResult(List<InventoryProdVo> inventoryProdVo) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(!CollectionUtils.isEmpty(inventoryProdVo)){
            for(InventoryProdVo prodVo:inventoryProdVo){
                StkInventoryLine inventoryLine=new StkInventoryLine();
                inventoryLine.setInventoryId(prodVo.getInventoryId());
                inventoryLine.setBrandId(prodVo.getBrandId());
                inventoryLine.setCategId(prodVo.getCategoryId());
                inventoryLine.setProductId(prodVo.getProductId());
                inventoryLine.setProductStatus(prodVo.getStatus());
                inventoryLine.setLocationId(prodVo.getLocationId());
                inventoryLine.setPrice(prodVo.getPrice());
                inventoryLine.setProductQty(prodVo.getProductQty());
                inventoryLine.setCreateBy(sysUser.getUsername());
                inventoryLine.setCreateTime(new Date());
                inventoryLine.setInventoryDate(new Date());
                iStkInventoryLineService.save(inventoryLine);
            }
        }
    }

    @Override
    public String getReultInventory(StkInventory stkInventory) {
         String result = "";

        List<InventoryProdVo> list=this.queryProdList(String.valueOf(stkInventory.getId()));
        Map<String, Object> var2=new HashMap<String, Object>();
        var2.put("inventory_id",stkInventory.getId());
        //结果
        Collection<StkInventoryLine> list2= iStkInventoryLineService.listByMap(var2);
        for(int i=0;i<list.size();i++){
            InventoryProdVo ip=list.get(i);
            for (StkInventoryLine item : list2){
                if(item.getProductId()==ip.getProductId()){
                    if(item.getProductQty()!=null
                            && ip.getProductQty()!=null
                            && item.getProductQty().compareTo(ip.getProductQty())==1){
                        result ="盘亏";
                        return result;
                    }else  if (item.getProductQty()!=null
                            && ip.getProductQty()!=null
                            && item.getProductQty().compareTo(ip.getProductQty())==-1){
                        result ="盘盈";
                        return result;
                    }
                }
            }

        }
        return result;
    }

    @Override
    public List<InventoryProdVo> queryProdTotal(String inventoryId) {
        return stkInventoryMapper.queryProdTotal(inventoryId);
    }

}
