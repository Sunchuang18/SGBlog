package com.sun.service;

import com.sun.domain.ResponseResult;
import com.sun.domain.User;

public interface SystemLoginService {

    //登录
    ResponseResult login(User user);

    //退出登录
    ResponseResult logout();
}
