package com.sun.handler.security;

import com.alibaba.fastjson.JSON;
import com.sun.domain.ResponseResult;
import com.sun.enums.AppHttpCodeEnum;
import com.sun.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//自定义授权失败的处理器
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //输出异常信息
        accessDeniedException.printStackTrace();

        //枚举，返回给前端的信息
        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);

        //使用Spring提供的JSON工具类，把result转换为JSON，然后响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
