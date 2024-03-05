package com.sun.service.impl;

import com.sun.domain.LoginUser;
import com.sun.domain.ResponseResult;
import com.sun.domain.User;
import com.sun.service.BlogLoginService;
import com.sun.utils.BeanCopyUtils;
import com.sun.utils.JwtUtil;
import com.sun.utils.RedisCache;
import com.sun.vo.BlogUserLoginVO;
import com.sun.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

//认证，判断用户登录是否成功
@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    //AuthenticationManager是Security官方提供的接口
    @Autowired
    private AuthenticationManager authenticationManager;

    //RedisCache是config目录写的类
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
        //获取userid
        //Principal：通常是用户的唯一标识
        LoginUser loginUser = (LoginUser)authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        //把userid通过JwtUtil工具类转为密文，此密文就是token值
        String jwt = JwtUtil.createJWT(userId);

        /*
            下面那行
                第一个参数：把上面那行的jwt保存到Redis。存储是键值对的形式，值是jwt，key要加上“bloglogin:”前缀
                第二个参数：要把哪个对象存入Redis。写的是loginUser，里面有权限信息，后期会用到
         */
        redisCache.setCacheObject("bloglogin:"+userId, loginUser);

        //把User转化为UserInfoVO，再放入VO对象的第二个参数
        UserInfoVO userInfoVO = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVO.class);
        BlogUserLoginVO VO = new BlogUserLoginVO(jwt, userInfoVO);

        //封装响应返回
        return ResponseResult.okResult(VO);
    }

    //登出
    @Override
    public ResponseResult logout() {
        //获取token，然后解析token值获取其中的userid
        //获取当前用户的身份验证信息，并将其存储在Authentication对象中
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //返回一个Object类型的对象，代表认证的主体，通常是用户。再把对象强转
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        //获取userid
        Long userid = loginUser.getUser().getId();

        //在redis根据key来删除用户的value值，注意之前在存key的时候是加了“bloglogin:”前缀
        redisCache.deleteObject("bloglogin:"+userid);

        //封装响应返回
        return ResponseResult.okResult();
    }
}
