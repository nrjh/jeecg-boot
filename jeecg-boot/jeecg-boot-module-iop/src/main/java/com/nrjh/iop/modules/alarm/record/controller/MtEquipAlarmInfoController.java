package com.nrjh.iop.modules.alarm.record.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.alarm.alarmtype.service.IEquipldTreeService;
import com.nrjh.iop.modules.alarm.alarmtype.vo.EquipIdModel;
import com.nrjh.iop.modules.alarm.bind.entity.Equip;
import com.nrjh.iop.modules.alarm.bind.mapper.FaultPrdProductInfoMapper;
import com.nrjh.iop.modules.alarm.bind.service.IEquipService;
import com.nrjh.iop.modules.alarm.bind.service.IFaultPrdProductInfoService;
import com.nrjh.iop.modules.alarm.bind.vo.FaultPrdProductInfoVO;
import com.nrjh.iop.modules.alarm.record.entity.MtEquipAlarmInfo;
import com.nrjh.iop.modules.alarm.record.entity.VAlarmInfo;
import com.nrjh.iop.modules.alarm.record.service.IMtEquipAlarmInfoService;
import com.nrjh.iop.modules.alarm.record.service.IVAlarmInfoService;
import com.nrjh.iop.modules.workOrder.entity.OrdWorkOrderInfo;
import com.nrjh.iop.modules.workOrder.service.IOrdWorkOrderInfoService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @Description: MT_EQUIP_ALARM_INFO
 * @Author: jeecg-boot
 * @Date: 2020-08-28
 * @Version: V1.0
 */
@Api(tags = "MT_EQUIP_ALARM_INFO")
@RestController
@RequestMapping("/alarm/record/mtEquipAlarmInfo")
@Slf4j
public class MtEquipAlarmInfoController extends JeecgController<MtEquipAlarmInfo, IMtEquipAlarmInfoService> {
    @Autowired
    private IMtEquipAlarmInfoService mtEquipAlarmInfoService;
    @Autowired
    private IEquipldTreeService iEquipldTreeService;

    @Autowired
    private IOrdWorkOrderInfoService orderInfoService;

    @Autowired
    private IVAlarmInfoService ivAlarmInfoService;

    @Autowired
    private IFaultPrdProductInfoService faultPrdProductInfoService;
    @Autowired
    private IEquipService equipService;

    /**
     * 分页列表查询
     *
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "MT_EQUIP_ALARM_INFO-分页列表查询")
    @ApiOperation(value = "MT_EQUIP_ALARM_INFO-分页列表查询", notes = "MT_EQUIP_ALARM_INFO-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(VAlarmInfo vAlarmInfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<VAlarmInfo> queryWrapper = new QueryWrapper<>(vAlarmInfo);
        Page<VAlarmInfo> page = new Page<VAlarmInfo>(pageNo, pageSize);
        queryWrapper.isNotNull("ALARM_TIME");
        queryWrapper.orderByDesc("ALARM_TIME");
        IPage<VAlarmInfo> pageList = ivAlarmInfoService.page(page, queryWrapper);
        pageList.getRecords().forEach(item ->{
            if(!StringUtil.isNullOrEmpty(item.getEquipNo())){
                List<Equip> equips=ivAlarmInfoService.queryListEquip(item.getEquipNo());
                if (!CollectionUtil.isEmpty(equips)){
                    item.setEquipName(equips.get(0).getName());
                    FaultPrdProductInfoVO faultPrdProductInfoVO =new FaultPrdProductInfoVO();
                    faultPrdProductInfoVO.setProductId(equips.get(0).getEquipId());
                    faultPrdProductInfoVO.setFaultName(item.getRealReason());
                    if(!StringUtils.isEmpty(item.getLineNo())){
                        String lineNo=faultPrdProductInfoService.queryExplanInfo(item.getLineNo());
                        faultPrdProductInfoVO.setLineNo(lineNo);
                        item.setLineNo(lineNo);
                    }
                    List<FaultPrdProductInfoVO> faultPrdProductInfoVOList=  faultPrdProductInfoService.queryFaultPrdProduct(faultPrdProductInfoVO);
                    //todo 存在多个设备绑定不同的等级怎么处理？
                    if(!CollectionUtil.isEmpty(faultPrdProductInfoVOList)){
                        item.setAlarmLevel(faultPrdProductInfoVOList.get(0).getFaultGrade());
                    }
                }
                item.setAlarmAmount(ivAlarmInfoService.alarmAmount(item.getEquipNo()));
            }
            QueryWrapper queryWrapperorder = new QueryWrapper();
            queryWrapperorder.eq("FAULT_ID",item.getId());
            List<OrdWorkOrderInfo> list = orderInfoService.list(queryWrapperorder);
            if(!CollectionUtil.isEmpty(list)){
                item.setIsOrder("1");
                item.setOrdWorkOrderInfo(list.get(0));
            }else {
                item.setIsOrder("0");
            }
        });
        return Result.ok(pageList);
    }


    @GetMapping(value = "/queryFaultName")
    public Result<?> queryFaultNamePageList(VAlarmInfo vAlarmInfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<VAlarmInfo> queryWrapper = new QueryWrapper<>(vAlarmInfo);
        Page<VAlarmInfo> page = new Page<VAlarmInfo>(pageNo, pageSize);
        queryWrapper.isNotNull("ALARM_TIME");
        queryWrapper.orderByDesc("ALARM_TIME");
        if(!StringUtils.isEmpty(vAlarmInfo.getFaultType())){
            List<String> equipNos=equipService.quwerEquipNo(vAlarmInfo.getFaultType());
            queryWrapper.in("EQUIP_NO",equipNos);
        }
        IPage<VAlarmInfo> pageList = ivAlarmInfoService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param mtEquipAlarmInfo
     * @return
     */
    @AutoLog(value = "MT_EQUIP_ALARM_INFO-添加")
    @ApiOperation(value = "MT_EQUIP_ALARM_INFO-添加", notes = "MT_EQUIP_ALARM_INFO-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody MtEquipAlarmInfo mtEquipAlarmInfo) {
        mtEquipAlarmInfo.setAlarmSource("人工录入");
        mtEquipAlarmInfo.setWriteTime(new Date());
        List<EquipIdModel> equipIdModel=iEquipldTreeService.selectEquipById(mtEquipAlarmInfo.getEquipName());
        if(!CollectionUtils.isEmpty(equipIdModel)){
            mtEquipAlarmInfo.setEquipName(equipIdModel.get(0).getTitle());
            mtEquipAlarmInfo.setEquipNo(equipIdModel.get(0).getEquipNo());
        }
        mtEquipAlarmInfoService.save(mtEquipAlarmInfo);
        return Result.ok("添加成功！");
    }

    //获取当前登录用户的用户名
    @ApiOperation(value = "获取当前登录的用户名")
    @GetMapping("/user")
    private Result<?> getUserName(){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return Result.ok(sysUser);
    }

    /**
     * 编辑
     *
     * @param mtEquipAlarmInfo
     * @return
     */
    @AutoLog(value = "MT_EQUIP_ALARM_INFO-编辑")
    @ApiOperation(value = "MT_EQUIP_ALARM_INFO-编辑", notes = "MT_EQUIP_ALARM_INFO-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody MtEquipAlarmInfo mtEquipAlarmInfo) {
        mtEquipAlarmInfoService.updateById(mtEquipAlarmInfo);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "MT_EQUIP_ALARM_INFO-通过id删除")
    @ApiOperation(value = "MT_EQUIP_ALARM_INFO-通过id删除", notes = "MT_EQUIP_ALARM_INFO-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        mtEquipAlarmInfoService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "MT_EQUIP_ALARM_INFO-批量删除")
    @ApiOperation(value = "MT_EQUIP_ALARM_INFO-批量删除", notes = "MT_EQUIP_ALARM_INFO-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.mtEquipAlarmInfoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "MT_EQUIP_ALARM_INFO-通过id查询")
    @ApiOperation(value = "MT_EQUIP_ALARM_INFO-通过id查询", notes = "MT_EQUIP_ALARM_INFO-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        MtEquipAlarmInfo mtEquipAlarmInfo = mtEquipAlarmInfoService.getById(id);
        if (mtEquipAlarmInfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(mtEquipAlarmInfo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param mtEquipAlarmInfo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MtEquipAlarmInfo mtEquipAlarmInfo) {
        return super.exportXls(request, mtEquipAlarmInfo, MtEquipAlarmInfo.class, "MT_EQUIP_ALARM_INFO");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MtEquipAlarmInfo.class);
    }

    /**
     * 查询设备
     *
     * @return
     */
    @RequestMapping(value = "/queryEquip", method = RequestMethod.GET)
    public Result<List<EquipIdModel>> queryEquip(String paramId) {
        Result<List<EquipIdModel>> result = new Result<>();
        List<EquipIdModel> children=iEquipldTreeService.selectThreeEquip(paramId);
        result.setResult(children);
        result.setSuccess(true);
        return result;
    }

    /**
     * 查询设备 通过设备类型
     *
     * @return
     */
    @RequestMapping(value = "/selectEquipByEquipTypeId", method = RequestMethod.GET)
    public Result<List<EquipIdModel>> selectEquipByEquipTypeId(String paramId) {
        Result<List<EquipIdModel>> result = new Result<>();
        List<EquipIdModel> children=iEquipldTreeService.selectEquipByEquipTypeId(paramId);
        result.setResult(children);
        result.setSuccess(true);
        return result;
    }
    /**
     * 查询设备类型
     *
     * @return
     */
    @RequestMapping(value = "/queryEquipType", method = RequestMethod.GET)
    public Result<List<EquipIdModel>> queryEquipType(String paramId) {
        Result<List<EquipIdModel>> result = new Result<>();
        List<EquipIdModel> children=iEquipldTreeService.selectEquipType(paramId);
        result.setResult(children);
        result.setSuccess(true);
        return result;
    }


    @PostMapping(value = "/updateAlarmLevel")
    public Result<?> updateAlarmLevel(@RequestBody MtEquipAlarmInfo mtEquip) {

        MtEquipAlarmInfo mtEquipAlarmInfo = mtEquipAlarmInfoService.getById(mtEquip.getId());
        mtEquipAlarmInfo.setAlarmLevel(mtEquip.getAlarmLevel());
        mtEquipAlarmInfoService.updateById(mtEquipAlarmInfo);
        return Result.ok("更新成功!");
    }


    @PutMapping(value = "/productWorkOrder")
    public Result<?> productWorkOrder(@RequestBody MtEquipAlarmInfo mtEquip) {
        //判断是否已经生成工单
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //获取网省编码
        String provinceCode = mtEquip.getProvinceCode();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("FAULT_ID",mtEquip.getId());
        List list = orderInfoService.list(queryWrapper);
        if (!CollectionUtils.isEmpty(list)){
            return Result.error("更新失败：已经生成工单");
        }
        mtEquip = mtEquipAlarmInfoService.getById(mtEquip.getId());
        OrdWorkOrderInfo info = new OrdWorkOrderInfo();

        //artificial:手工生成
        info.setOrderCreLabe("artificial");
        //获取日期
        SimpleDateFormat fmt = new SimpleDateFormat("yyMMdd");
        String date = fmt.format(new Date());

        //获取6位随机数字
        Integer randomNum = new Random().nextInt(1000000);
        String newStrNum = String.format("%06d", randomNum);
        String workOrder = provinceCode+"A"+date+newStrNum;
        info.setId(workOrder);
        //	01:故障工单
        info.setOrderType("01");
        info.setFaultId(mtEquip.getId().toString());
        info.setDetectTaskNo(mtEquip.getDetectTaskNo());
        info.setLineNo(mtEquip.getLineNo());
        info.setOldId(mtEquip.getOldId());
        info.setEquipNo(mtEquip.getEquipNo());
        info.setEquipName(mtEquip.getEquipName());
        info.setAlarmTime(mtEquip.getAlarmTime());
        List<Equip> equips=ivAlarmInfoService.queryListEquip(mtEquip.getEquipNo());
        FaultPrdProductInfoVO faultPrdProductInfoVO =new FaultPrdProductInfoVO();
        faultPrdProductInfoVO.setProductId(equips.get(0).getEquipId());
        faultPrdProductInfoVO.setLineNo(equips.get(0).getAreaId());
        List<FaultPrdProductInfoVO> faultPrdProductInfoVOList=  faultPrdProductInfoService.queryFaultPrdProduct(faultPrdProductInfoVO);
        //todo 存在多个设备绑定不同的等级怎么处理？
        if(!CollectionUtil.isEmpty(faultPrdProductInfoVOList)){
            info.setAlarmLevel(faultPrdProductInfoVOList.get(0).getFaultGrade());
        }
        info.setAlarmSource(mtEquip.getAlarmSource());
        info.setAlarmReason(mtEquip.getAlarmReason());
        info.setAlarmDesc(mtEquip.getAlarmDesc());
        info.setAlarmSugg(mtEquip.getAlarmSugg());
        info.setAlarmStatus(mtEquip.getAlarmStatus());
        info.setOverdurTime(mtEquip.getEndTime());
        info.setCreateTime(new Date());
        info.setCreateName(sysUser.getRealname());
        info.setCreateId(mtEquip.getCreateBy());
        info.setIsDel(0);
        //工单状态为01 待派工
        info.setProcessStatus("01");
        orderInfoService.save(info);

        return Result.ok("更新成功!");
    }

}
