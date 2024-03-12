package com.sun.aspect;

import com.alibaba.fastjson.JSON;
import com.sun.annotation.mySystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

//切面类
@Component
@Aspect//告诉spring容器，myLogAspect是切面类
@Slf4j//用于在控制台打印日志信息
public class myLogAspect {

    //确定哪个切点。以后哪个类想成为切点就在哪个类添加@Pointcut注解。例如POT方法就是切点
    @Pointcut("@annotation(com.sun.annotation.mySystemLog)")
    public void POT(){}//切点：point of tangency

    //定义通知的方法（此处用的是环绕通知），通知的方法也就是增强的具体代码。
    //@Around注解表示该通知的方法会作用在哪个切点
    @Around("POT()")
    //ProceedingJoinPoint可以拿到被增强方法的信息
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //proceed方法表示调用目标方法，ret就是目标方法执行完成之后的返回值
        Object ret;
        try {
            //调用下面写的“实现打印日志信息的格式信息”方法
            handleBefore(joinPoint);
            //这是目标方法执行完成。上一行是目标方法未执行，下一行是目标方法已执行。
            ret = joinPoint.proceed();
            //调用下面写的“实现打印日志信息的数据信息”方法
            handleAfter(ret);
        } finally {
            //下面的“实现打印日志信息的数据信息”方法，不管有没有异常都会执行
            //System.lineSeparator()表示当前系统的换行符
            log.info("====================end====================" + System.lineSeparator());
        }

        //封装城切面返回
        return ret;
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        //拿到请求的报文，其中包含url、请求方式、ip。注意这里拿到的request会在以后的log中大量使用
        HttpServletRequest request = requestAttributes.getRequest();

        //获取被增强方法的注解对象。例如获取UserController类的updateUserInfo方法的@mySystemLog注解
        mySystemLog systemLog = getSystemLog(joinPoint);

        log.info("====================start====================");
        //下面的log输出，参数一{}表示占位符，值为参数二的变量
        //打印URL
        log.info("请求URL：{}",request.getRequestURL());
        //打印描述信息。例如获取UserController类的updateUserInfo方法的@mySystemLog注解的描述信息
        log.info("接口描述：{}",systemLog.businessName());
        //打印 Http method
        log.info("请求方式：{}",request.getMethod());
        //打印调试 controller 的全路径（全类名）、方法名
        log.info("请求类名：{}.{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                ((MethodSignature) joinPoint.getSignature()).getName());
        //打印请求的 IP
        log.info("访问IP：{}",request.getRemoteHost());
        //打印请求入参。JSON.toJSONString+FastJson提供的工具方法，能把数据转成JSON
        log.info("传入参数：{}", JSON.toJSONString(joinPoint.getArgs()));
    }

    private void handleAfter(Object ret) {
        //打印出参。JSON.toJSONString+FastJson提供的工具方法，能把数据转成JSON
        log.info("返回参数：{}",JSON.toJSONString(ret));
    }

    //获取被增强方法的注解对象。例如获取UserController类的updateUserInfo方法的@mySystemLog注解
    private mySystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        mySystemLog systemLog = methodSignature.getMethod().getAnnotation(mySystemLog.class);
        return systemLog;
    }

}
