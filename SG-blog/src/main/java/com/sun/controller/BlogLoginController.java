package com.sun.controller;

import com.sun.domain.ResponseResult;
import com.sun.domain.User;
import com.sun.enums.AppHttpCodeEnum;
import com.sun.exception.SystemException;
import com.sun.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    //登录
    @PostMapping("/login")
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
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
