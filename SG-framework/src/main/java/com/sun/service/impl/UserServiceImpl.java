package com.sun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.domain.User;
import com.sun.mapper.UserMapper;
import com.sun.service.UserService;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    //已增加UserServiceImpl实现类，此时应该是没有实现方法
}
