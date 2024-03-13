package com.sun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//添加包搜索，用于注入公共模块的类
@MapperScan("com.sun.mapper")
@EnableScheduling//@EnableScheduling是spring提供的定时任务的注解
public class SGBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(SGBlogApplication.class,args);
    }
}
