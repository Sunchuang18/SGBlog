package com.sun.controller;

import com.sun.annotation.mySystemLog;
import com.sun.domain.ResponseResult;
import com.sun.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("link")
@Api(tags = "友链的相关接口")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping("/getAllLink")
    //自定义在swagger中请求接口的信息
    @ApiOperation("查询友链")
    @mySystemLog(businessName = "查询友链")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}
