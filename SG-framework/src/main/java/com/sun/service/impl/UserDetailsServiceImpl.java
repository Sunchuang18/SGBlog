package com.sun.service.impl;

import com.baomidou.mybatisplus.core.assist.ISqlRunner;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sun.domain.LoginUser;
import com.sun.domain.User;
import com.sun.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

//当SGBlog的BlogLoginServiceImpl类封装好登录的用户名和密码之后，就会传到当前这个实现类
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    //在这里之前，已经拿到了登录的用户名和密码。UserDetails是SpringSecurity官方提供的接口
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //根据拿到的用户名，并结合查询条件（数据库是否有这个用户名），去查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        User user = userMapper.selectOne(queryWrapper);

        //判断是否查询到用户，也就是这个用户是否存在，如果没查到就抛出异常
        if (Objects.isNull(user)){
            throw new RuntimeException("用户不存在");//后期对异常统一处理
        }

        //TODO 查询权限信息，并封装

        /*
            返回查询到的用户信息。
            注意下面那行直接返回user会报错，需要在SG-framework工程的domain目录新建LoginUser类，
            在LoginUser类实现UserDetails接口，然后下面那行就返回LoginUser对象。
         */
        return new LoginUser(user);
    }
}
