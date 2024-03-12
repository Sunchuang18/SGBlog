package com.sun.controller;

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
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    //更新个人信息
    @PutMapping("userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    //用户注册
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
