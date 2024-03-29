package com.sun.controller;

import com.sun.annotation.mySystemLog;
import com.sun.domain.ResponseResult;
import com.sun.domain.User;
import com.sun.enums.AppHttpCodeEnum;
import com.sun.exception.SystemException;
import com.sun.service.BlogLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "用户登录的相关接口文档")
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    //登录
    @PostMapping("/login")
    //自定义在swagger中请求接口的信息
    @ApiOperation("登录")
    @mySystemLog(businessName = "登录")
    public ResponseResult login(@RequestBody User user){
        //如果用户在进行登录时没有传入“用户名”
        if (!StringUtils.hasText(user.getUserName())){
            //返回提示信息“必须填写用户名”
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    //登出
    @PostMapping("/logout")
    //自定义在swagger中请求接口的信息
    @ApiOperation("登出")
    @mySystemLog(businessName = "登出")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
