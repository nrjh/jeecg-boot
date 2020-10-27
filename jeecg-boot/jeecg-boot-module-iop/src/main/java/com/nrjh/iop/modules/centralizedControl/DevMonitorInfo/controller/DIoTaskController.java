package com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.entity.DIoTask;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.service.DIoTaskService;
import com.nrjh.iop.modules.centralizedControl.DevMonitorInfo.vo.DIoTaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linqinxiang
 * @email linqinxiang@leimingtech.net
 * @since 1.0.0 2020/9/12 15:39
 */
@Api(tags="监控设备管理")
@RestController
@RequestMapping("/ptm/dIoTask")
@Slf4j
public class DIoTaskController  extends JeecgController<DIoTask, DIoTaskService> {
    @Autowired
    private DIoTaskService ioTaskService;
    /**
     * todo 出入库任务监控列表--数据不确定（原因：原型描述不明确）
     * 分页列表查询
     *
     * @param dIoDet
     * @return
     */
    @AutoLog(value = "出入库任务监控列表")
    @ApiOperation(value="出入库任务监控列表查询", notes="出入库任务监控列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DIoTask dIoDet) {
        Map<String,Object> parmas = new HashMap<>();
        if(dIoDet.getTaskNo()!=null){
            parmas.put("taskNo",dIoDet.getTaskNo());
        }
        if(dIoDet.getIoFlag() != null){
            parmas.put("ioFlag",dIoDet.getIoFlag());
        }
        List<DIoTask> allDIotaskInfo = ioTaskService.getAllDIotaskInfo(parmas);
        List<DIoTaskVO> dIoTaskVOS = ConvertUtils.sourceToTarget(allDIotaskInfo, DIoTaskVO.class);
        Page<DIoTaskVO> resultPage = new Page<DIoTaskVO>();
        resultPage.setRecords(dIoTaskVOS);
        return Result.ok(resultPage);
    }

}
