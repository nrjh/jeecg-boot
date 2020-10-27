package com.nrjh.iop.modules.stk.stkscrap.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nrjh.iop.modules.stk.quant.service.IStkQuantService;
import com.nrjh.iop.modules.stk.spareparts.entity.StkStock;
import com.nrjh.iop.modules.stk.spareparts.service.IStkStockService;
import com.nrjh.iop.modules.stk.stkscrap.entity.ScrapPro;
import com.nrjh.iop.modules.stk.stkscrap.entity.StkScrap;
import com.nrjh.iop.modules.stk.stkscrap.mapper.ScrapProMapper;
import com.nrjh.iop.modules.stk.stkscrap.service.IScrapProService;
import com.nrjh.iop.modules.stk.stkscrap.service.IStkScrapService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description: 报废管理---物料信息
 * @Author: chenchao
 * @Date:   2020-08-07
 * @Version: V1.0
 */
@Service
@DS("iop")
public class ScrapProServiceImpl extends ServiceImpl<ScrapProMapper, ScrapPro> implements IScrapProService {

    @Autowired
    private ScrapProMapper scrapProMapper;
    @Autowired
    private IStkScrapService stkScrapService;
    @Autowired
    private IStkStockService stkStockService;
    @Override
    public List<ScrapPro> queryProdList(ScrapPro scrapPro) {
        return scrapProMapper.queryProdList(scrapPro);
    }

    @Override
    public void saveProdList(ScrapPro scrapPro) {

        scrapProMapper.saveProdList(scrapPro);

    }

    @Override
    public List<ScrapPro> lookScrapPro(String scrapInventoryOrder) {
        return scrapProMapper.lookScrapPro(scrapInventoryOrder);
    }

    @Override
    @Transactional
    public Boolean submitScrapPro(StkScrap stkScrap)  {

            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录人
            stkScrap.setUpdateBy(sysUser.getRealname());
            stkScrap.setUpdateTime(new Date());//设置当前时间
            //设置单据状态
            stkScrap.setStatus("2");//1.为新建单据保存状态；2.提交状态;3.确认状态;4.审核状态;5.作废诶状态
            //stkScrapService.save(stkScrap);
        return stkScrapService.saveOrUpdate(stkScrap);

    }

    @Override
    @Transactional
    //点击确认的时候 修改货位库存数量 减少（虚拟和实际库存都减去报废单件数）
    public Boolean sureScrapPro(String id) throws BusinessException {
        try {
            StkScrap stkScrap = new StkScrap();
            stkScrap.setId(id);
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录人
            //设置确认人和确认时间
            stkScrap.setSureBy(sysUser.getRealname());
            stkScrap.setSureTime(new Date());//设置当前时间
            //获取报废列表总报废数
            StkScrap stkScrap2 = stkScrapService.getById(id);//通过id得到报废对象
            String scrapOder = stkScrap2.getScrapInventoryOrder();//得到报废单号
            //通过单号查询明细表报废物料列表信息
            List<ScrapPro> scrapProList = scrapProMapper.lookScrapPro(scrapOder);

            for(int i = 0; i < scrapProList.size(); i++) {
                //根据报废物料列表的物料编号，扣减对应物料库存信息
                this.updateStockQty(scrapProList.get(i).getProductNo(),scrapProList.get(i).getProductQty(),scrapProList.get(i).getProductQty());
            }
            //获取报废物料名称
            //更新库存信息
            //StkStock stkStock = stkStockService.getById(id);//拿到修改的库存信息对象
            //int newQty = stkStock.getProductQty() - scrapQtySum;
            //int newVirtualQty =  stkStock.getVirtualQty() - scrapQtySum;
            //if(newQty < 0 && newVirtualQty < 0){
            //    throw new BusinessException("报废数量不能大于原有库存数量!");
            //}
            //stkStock.setVirtualQty(newVirtualQty);
            //stkStock.setProductQty(newQty);
            //stkStockService.saveOrUpdate(stkStock);
            //this.updateStockQty();
            //设置单据状态
            stkScrap.setStatus("3");//1.为新建单据保存状态；2.提交状态;3.确认状态;4.审核状态;5.作废诶状态
            //stkScrapService.save(stkScrap);
            return stkScrapService.updateById(stkScrap);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw new BusinessException("业务异常");

        }
    }

    @Override
    @Transactional
    public Boolean shenheScrapPro(String id,String info) throws BusinessException {
        try {
            StkScrap stkScrap = new StkScrap();
            stkScrap.setId(id);
            stkScrap.setCheckInfo(info);
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录人
            //设置审核人和审核时间
            stkScrap.setShenheBy(sysUser.getRealname());
            stkScrap.setShenheTime(new Date());//设置当前时间
            //设置单据状态
            stkScrap.setStatus("4");//1.为新建单据保存状态；2.提交状态;3.确认状态;4.审核状态;5.作废诶状态
            //stkScrapService.save(stkScrap);

            return stkScrapService.updateById(stkScrap);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw new BusinessException("业务异常");
        }
    }

    @Override
    @Transactional
    public Boolean badScrapPro(String id) throws BusinessException {
        try {
            StkScrap stkScrap = new StkScrap();
            stkScrap.setId(id);
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录人
            stkScrap.setUpdateBy(sysUser.getRealname());
            stkScrap.setUpdateTime(new Date());//设置当前时间
            //设置单据状态
            stkScrap.setStatus("5");//1.为新建单据保存状态；2.提交状态;3.确认状态;4.审核状态;5.作废诶状态
            //stkScrapService.save(stkScrap);

            return stkScrapService.updateById(stkScrap);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw new BusinessException("业务异常");
        }
    }

    @Override
    public Boolean updateStockQty(String prdNo, Integer realQty, Integer virtualQty) throws BusinessException {
        //通过物料编号得到物料库存信息
        List<StkStock> stkStockList = scrapProMapper.queryStockList(prdNo);
        int r = stkStockList.get(0).getProductQty();//实际库存
        int v = stkStockList.get(0).getVirtualQty();//虚拟库存
        if(r < realQty && v < virtualQty){
            return  false;
        }
        return true;
    }
}
