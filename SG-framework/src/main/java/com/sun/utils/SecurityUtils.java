package com.sun.utils;

import com.sun.domain.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

//在“发送评论”功能用到的工具类
public class SecurityUtils {
    //获取用户的userid
    public static LoginUser getLoginUser(){
        return (LoginUser) getAuthentication().getPrincipal();
    }

    //获取Authentication
    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    //制定userid为1的用户就是网站管理员
    public static Boolean isAdmin(){
        Long id = getLoginUser().getUser().getId();
        return id != null && 1L == id;
    }

    public static Long getUserId(){
        return getLoginUser().getUser().getId();
    }
}
