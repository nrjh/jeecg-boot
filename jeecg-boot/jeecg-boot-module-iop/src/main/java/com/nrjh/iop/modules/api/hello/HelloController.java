package com.nrjh.iop.modules.api.hello;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试新的module
 * @author chengtg
 *
 */
@Slf4j
@Api(tags="新建iop-api")
@RestController
@RequestMapping("/iop/api/hello")
public class HelloController {

    @ApiOperation(value = "测试Hello方法", notes = "获取hello world! ")
    @GetMapping(value="/")
    public Result<String> hello(){
        Result<String> result = new Result<String>();
        result.setResult("hello word!");
        result.setSuccess(true);
        return result;
    }
}

