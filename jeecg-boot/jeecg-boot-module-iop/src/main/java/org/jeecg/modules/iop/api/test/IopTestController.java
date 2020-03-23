package org.jeecg.modules.iop.api.test;

import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试新的module
 * @author chengtg
 *
 */
@Slf4j
@Api(tags="新建module--iop")
@RestController
@RequestMapping("/iop/api/test")
public class IopTestController {

    @ApiOperation(value = "测试test方法", notes = "获取test world! ")
    @GetMapping(value="/")
    public Result<String> test(){
        Result<String> result = new Result<String>();
        result.setResult("test word!");
        result.setSuccess(true);
        return result;
    }
}

