package com.sun.controller;

import com.sun.domain.ResponseResult;
import com.sun.domain.User;
import com.sun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    //查询用户列表
    @GetMapping("/list")
    public ResponseResult list(User user, Integer pageNum, Integer pageSize){
        return userService.selectUserPage(user, pageNum, pageSize);
    }
}
