package com.sun.exception;

import com.sun.enums.AppHttpCodeEnum;

//统一异常处理
public class SystemException extends RuntimeException{
    private int code;
    private String msg;

    public int getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }

    //定义一个构造方法，接收的参数是枚举类型
    public SystemException(AppHttpCodeEnum httpCodeEnum){
        super(httpCodeEnum.getMsg());
        //把某个枚举类里的code和msg复制给异常对象
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}
