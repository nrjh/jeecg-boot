package com.nrjh.iop.modules.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.order.vo.OrderPlanVo;
import com.nrjh.iop.modules.order.vo.ProdAndStockVo;
import com.nrjh.iop.modules.stk.move.entity.StkMove;
import com.nrjh.iop.modules.stk.move.service.IStkMoveService;
import com.nrjh.iop.modules.stk.picking.entity.StkPicking;
import com.nrjh.iop.modules.stk.picking.service.IStkPickingService;
import com.nrjh.iop.modules.stk.pickingtype.entity.StkPickingType;
import com.nrjh.iop.modules.stk.pickingtype.service.IStkPickingTypeService;
import com.nrjh.iop.modules.stk.stkscrap.entity.ScrapPro;
import com.nrjh.iop.modules.stk.stkscrap.service.IScrapProService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by shiyiteng on 2020/8/10.
 */
@Api(tags = "采购计划")
@RestController
@RequestMapping("/iop/order/orderPlan")
@Slf4j
public class OrderPlanController {

    @Autowired
    private IStkPickingService stkPickingService;
    @Autowired
    private IStkMoveService stkMoveService;
    @Autowired
    private IStkPickingTypeService stkPickingTypeService;
    @Autowired
    private IScrapProService scrapProService;

    /**
     * 分页列表查询
     *
     * @param stkPicking
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "采购计划-分页列表查询")
    @ApiOperation(value = "采购计划-分页列表查询", notes = "采购计划-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(StkPicking stkPicking,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Map<String, Object> var1=new HashMap<>();
        var1.put("code","orderplan");
        Collection<StkPickingType> list= stkPickingTypeService.listByMap(var1);
        if(!CollectionUtils.isEmpty(list)) {
            Iterator<StkPickingType> iterator = list.iterator();
            while (iterator.hasNext()) {
                StkPickingType stkPickingType = iterator.next();
                if (Objects.nonNull(stkPickingType)) {
                    Integer id = stkPickingType.getId();
                    stkPicking.setPickingTypeId(id);
                    break;
                }
            }
        }
        Page<StkPicking> page = new Page<>(pageNo, pageSize);
        IPage<StkPicking> pageList = stkPickingService.selectStkPickingPage(page, stkPicking);
        if (CollectionUtils.isEmpty(pageList.getRecords())){
            return Result.ok(pageList);
        }
        pageList.getRecords().forEach(item ->{
            item.setProdDetails(stkPickingService.getProdDetails(String.valueOf(item.getId())));
        });
        return Result.ok(pageList);
    }


    /**
     * 添加
     *
     * @param
     * @return
     */
    @AutoLog(value = "采购计划-添加")
    @ApiOperation(value = "采购计划-添加", notes = "采购计划-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody OrderPlanVo orderPlanVo) throws BusinessException {
        stkPickingService.saveOrderPlan(orderPlanVo);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param orderPlanVo
     * @return
     */
    @AutoLog(value = "采购计划-编辑")
    @ApiOperation(value = "采购计划-编辑", notes = "采购计划-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody OrderPlanVo orderPlanVo) {

        stkPickingService.updateOrderPlan(orderPlanVo);
        return Result.ok("编辑成功!");
    }



    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "采购计划-通过id删除")
    @ApiOperation(value = "采购计划-通过id删除", notes = "采购计划-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        stkPickingService.delMain(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "采购计划-批量删除")
    @ApiOperation(value = "采购计划-批量删除", notes = "采购计划-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.stkPickingService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "采购计划-通过id查询")
    @ApiOperation(value = "采购计划-通过id查询", notes = "采购计划-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        StkPicking stkPicking = stkPickingService.getById(id);
        if (stkPicking == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(stkPicking);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "采购计划-通过id查询")
    @ApiOperation(value = "采购计划-通过id查询", notes = "采购计划-通过id查询")
    @GetMapping(value = "/queryStkMoveByMainId")
    public Result<?> queryStkMoveListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<StkMove> stkMoveList = stkMoveService.selectByPickingId(id);
        List<ProdAndStockVo> result=new ArrayList<>();
        if(!CollectionUtils.isEmpty(stkMoveList)){
            for(StkMove stkMove:stkMoveList){
                ProdAndStockVo prodAndStockVo=new ProdAndStockVo();
                prodAndStockVo.setProductId(Integer.parseInt(stkMove.getProductId()));

                ScrapPro scrapPro=new ScrapPro();
                scrapPro.setProductId(Integer.parseInt(stkMove.getProductId()));
                List<ScrapPro> records=scrapProService.queryProdList(scrapPro);
                if(!CollectionUtils.isEmpty(records)){
                    prodAndStockVo.setAcName(records.get(0).getAcName());
                    prodAndStockVo.setUuName(records.get(0).getUuName());
                    prodAndStockVo.setCateName(records.get(0).getCateName());
                    prodAndStockVo.setName(records.get(0).getRpoductName());
                    prodAndStockVo.setRpoductName(records.get(0).getRpoductName());
                    prodAndStockVo.setProductNo(records.get(0).getProductNo());
                }
                prodAndStockVo.setPartnerId(stkMove.getVendorId());
                prodAndStockVo.setProductUomQty(stkMove.getProductUomQty());
                result.add(prodAndStockVo);
            }

        }
        return Result.ok(result);
    }


    @GetMapping(value = "/wlList")
    public Result<?> queryWlPageList( ScrapPro scrapPro,
                                      @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                      @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                      HttpServletRequest req) {

        Result<List<ScrapPro>> result=new Result<>();
        result.setCode(0);
        List<ScrapPro> records=scrapProService.queryProdList(scrapPro);
        result.setResult(records);
        return result;

    }



}
