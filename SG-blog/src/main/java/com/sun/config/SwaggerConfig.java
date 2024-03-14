package com.sun.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//标记为配置类
@Configuration
//开启swagger，即可通过swagger为SG-blog工程生成接口文档
@EnableSwagger2
public class SwaggerConfig {
}
