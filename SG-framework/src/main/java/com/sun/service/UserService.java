package com.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.domain.ResponseResult;
import com.sun.domain.User;

public interface UserService extends IService<User> {
    //个人信息查询
    ResponseResult userInfo();

    //更新个人信息
    ResponseResult updateUserInfo(User user);

    //用户注册
    ResponseResult register(User user);

    //查询用户列表
    ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize);
}
