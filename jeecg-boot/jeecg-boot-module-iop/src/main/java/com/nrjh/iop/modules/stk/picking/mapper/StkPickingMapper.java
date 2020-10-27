package com.nrjh.iop.modules.stk.picking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nrjh.iop.modules.order.vo.ProdAndStockVo;
import com.nrjh.iop.modules.stk.picking.entity.StkPicking;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description: 调拨单
 * @Author: jeecg-boot
 * @Date:   2020-05-18
 * @Version: V1.0
 */
public interface StkPickingMapper extends BaseMapper<StkPicking> {

    List<Map<String,Object>> getProdDetails(String orderPlanId);

    //查询物料信息列表
    List<ProdAndStockVo> queryProdList(ProdAndStockVo vo);

    /**
     *
     * @param id
     * @return
     */
    StkPicking getStkPickingById(@RequestParam("id") String id);

    /**
     * 保存物品和规格关系
     * @param prodVo
     */
    void setCateGory(ProdAndStockVo prodVo);

    /**
     * 通过pickingNo修改采购计划状态
     * @param stkPicking
     */
    void updateByPickingNo(@Param("StkPicking") StkPicking stkPicking);

    IPage<StkPicking> selectStkPickingPage(IPage page,@Param("stkPicking") StkPicking stkPicking);
}
