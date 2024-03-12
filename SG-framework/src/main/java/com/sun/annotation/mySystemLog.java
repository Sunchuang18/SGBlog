package com.sun.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//自定义注解类，注解名就是接口名
@Retention(RetentionPolicy.RUNTIME)//表示该mySystemLog注解类会保持到runtime阶段
@Target(ElementType.METHOD)//表示该注解类的注解功能只能作用于方法上
public @interface mySystemLog {
    //为controller提供接口的描述信息，用于日志功能
    String businessName();
}
