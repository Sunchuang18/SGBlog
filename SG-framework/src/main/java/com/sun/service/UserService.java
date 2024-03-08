package com.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.domain.ResponseResult;
import com.sun.domain.User;

public interface UserService extends IService<User> {
    //个人信息查询
    ResponseResult userInfo();
}
