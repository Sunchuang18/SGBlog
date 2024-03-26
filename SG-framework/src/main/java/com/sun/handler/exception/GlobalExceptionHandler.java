package com.sun.handler.exception;

import com.sun.domain.ResponseResult;
import com.sun.enums.AppHttpCodeEnum;
import com.sun.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//全局异常处理。最终都会在这个类
/*
    @RestControllerAdvice = @ControllerAdvice + @ResponseBody
        其中@ControllerAdvice是对controller层的增强
    @Slf4j：使用Lombok提供的Slf4j注解，实现日志功能
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //用户登录的异常交给SystemException类处理
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        //打印异常信息。其中{}是占位符，具体指由e决定
        // log.error("出现了异常！{}",e);
        log.error("出现了异常！{}",e.getMessage());
        //从异常对象中获取提示信息封装，然后返回。
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }

    //处理SpringSecurity的权限异常
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResult handleAccessDeniedException(AccessDeniedException e){
        return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH.getCode(), e.getMessage());
    }

    //其他异常的处理
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        //打印异常信息。其中{}是占位符，具体指由e决定
        log.error("出现了异常！{}",e);
        // log.error("出现了异常！{}",e.getMessage());
        //从异常对象中获取提示信息封装，然后返回。
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }
}
