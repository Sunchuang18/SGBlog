package com.sun.controller;

import com.sun.annotation.mySystemLog;
import com.sun.domain.ResponseResult;
import com.sun.domain.User;
import com.sun.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户的相关接口文档")
public class UserController {

    @Autowired
    private UserService userService;

    //查询个人信息
    @GetMapping("/userInfo")
    //自定义在swagger中请求接口的信息
    @ApiOperation("查询个人信息")
    @mySystemLog(businessName = "查询个人信息")//接口描述，用于“日志记录”功能
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    //更新个人信息
    @PutMapping("userInfo")
    //自定义在swagger中请求接口的信息
    @ApiOperation("更新用户信息")
    @mySystemLog(businessName = "更新用户信息")//接口描述，用于“日志记录”功能
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    //用户注册
    @PostMapping("/register")
    //自定义在swagger中请求接口的信息
    @ApiOperation("用户注册")
    @mySystemLog(businessName = "用户注册")//接口描述，用于“日志记录”功能
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
