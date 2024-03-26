package com.sun.service.impl;

import com.baomidou.mybatisplus.core.assist.ISqlRunner;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sun.constants.SystemConstants;
import com.sun.domain.LoginUser;
import com.sun.domain.User;
import com.sun.mapper.MenuMapper;
import com.sun.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

//当SGBlog的BlogLoginServiceImpl类封装好登录的用户名和密码之后，就会传到当前这个实现类
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

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

        //如果是后台用户，才需要查询权限。
        //对后台用户做权限校验
        if (user.getType().equals(SystemConstants.IS_ADMIN)){
            //根据用户id查询权限关键字，即list是权限信息的集合
            List<String> list = menuMapper.selectPermsByOtherUserId(user.getId());
            return new LoginUser(user, list);
        }

        //如果不是后台用户，就只封装用户信息，不封装权限信息
        //返回查询到的用户信息
        return new LoginUser(user, null);
    }
}
