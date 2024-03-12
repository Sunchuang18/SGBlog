package com.sun.controller;

import com.sun.annotation.mySystemLog;
import com.sun.domain.ResponseResult;
import com.sun.domain.User;
import com.sun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //查询个人信息
    @GetMapping("/userInfo")
    @mySystemLog(businessName = "查询个人信息")//接口描述，用于“日志记录”功能
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    //更新个人信息
    @PutMapping("userInfo")
    @mySystemLog(businessName = "更新用户信息")//接口描述，用于“日志记录”功能
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    //用户注册
    @PostMapping("/register")
    @mySystemLog(businessName = "用户注册")//接口描述，用于“日志记录”功能
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
