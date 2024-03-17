package com.sun.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//标记为配置类
@Configuration
//开启swagger，即可通过swagger为SG-blog工程生成接口文档
@EnableSwagger2
public class SwaggerConfig {

    /*
    //创建API基本信息
    @Bean
    public Docket createTestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //对所有的api进行监控
                .apis(RequestHandlerSelectors.any())
                //不显示错误的接口地址，也就是错误路径不监控
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }
     */

    @Bean
    public Docket customDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //只监控com.sun.controller包的api
                //.apis(RequestHandlerSelectors.basePackage("com.sun.controller"))
                //对所有的api进行监控
                .apis(RequestHandlerSelectors.any())
                //不显示错误的接口地址，也就是错误路径不监控
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }

    private ApiInfo apiInfo(){
        Contact contact = new Contact("团队名","https://xxsun.fun","123456@email.com");
        return new ApiInfoBuilder()
                .title("文档标题")
                .description("文档描述")
                //联系方式
                .contact(contact)
                //版本
                .version("1.1.0")
                .build();
    }
}
