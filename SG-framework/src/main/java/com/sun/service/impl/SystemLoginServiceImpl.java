package com.sun.service.impl;

import com.sun.domain.LoginUser;
import com.sun.domain.ResponseResult;
import com.sun.domain.User;
import com.sun.service.SystemLoginService;
import com.sun.utils.JwtUtil;
import com.sun.utils.RedisCache;
import com.sun.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//认证，判断用户登录是否成功
@Service
public class SystemLoginServiceImpl implements SystemLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    //登录
    @Override
    public ResponseResult login(User user) {
        //封装登录的用户名和密码
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        //在下一行之前，封装的数据会先走UserDetailsServiceImpl实现类
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //上面那一行会得到所有的认证用户信息authenticate。
        //下一行需要判断用户认证是否通过，如果authenticate的值是null，证明没通过。
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //认证通过
        //获取userid
        //Principal：通常是用户的唯一标识
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        //把userid通过JwtUtil工具类转为密文，此密文就是token值
        String jwt = JwtUtil.createJWT(userid);

        /*
            下面那行
                第一个参数：把上面那行的jwt保存到Redis。存储是键值对的形式，值是jwt，key要加上“login:”前缀
                第二个参数：要把哪个对象存入Redis。写的是loginUser，里面有权限信息，后期会用到
         */
        redisCache.setCacheObject("login:"+userid, loginUser);

        //把token封装，返回
        Map<String,String> map = new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.okResult(map);
    }

    //退出登录
    @Override
    public ResponseResult logout() {
        //获取当前登录的用户id
        Long userId = SecurityUtils.getUserId();

        //删除redis中对应的值
        //在redis根据key来删除用户的value值，注意之前在存key的时候是加了“login:”前缀
        redisCache.deleteObject("login:" + userId);

        //封装响应返回
        return ResponseResult.okResult();
    }
}
