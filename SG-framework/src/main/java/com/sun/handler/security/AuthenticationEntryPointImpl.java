package com.sun.handler.security;

import com.alibaba.fastjson.JSON;
import com.sun.domain.ResponseResult;
import com.sun.enums.AppHttpCodeEnum;
import com.sun.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//自定义认证失败的处理器
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //输出异常信息
        authException.printStackTrace();

        // 判断是登录才出现的异常（返回“用户名或密码错误”）
        // 还是未登录就访问特定接口才出现的异常（返回“需要登录后访问”）
        // 还是其他情况（返回“出现错误”）
        ResponseResult result = null;
        //BadCredentialsException 是Spring Security中用于表示提供的凭证（如用户名和密码）无效的异常类型。
        if (authException instanceof BadCredentialsException){
            //参数一返回的是响应码。参数二是返回具体的信息
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(), authException.getMessage());
        } else if (authException instanceof InsufficientAuthenticationException) {
            //InsufficientAuthenticationException 是Spring Security中用于表示用户认证不足或未认证的异常类型。
            //参数一返回的是响应码
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        } else {
            //参数一返回的是响应码。参数二是返回具体的信息
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "认证或授权失败");
        }

        //使用Spring提供的JSON工具类，把上一行的result转换成JSON，然后响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
