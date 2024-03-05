package com.sun.service;

import com.sun.domain.ResponseResult;
import com.sun.domain.User;

public interface BlogLoginService {

    //登录
    ResponseResult login(User user);

    //登出
    ResponseResult logout();
}
