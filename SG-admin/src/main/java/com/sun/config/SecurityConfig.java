package com.sun.config;

import com.sun.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//实现Security提供的WebSecurityConfigurerAdapter类，既可改变密码校验的规则
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //注入JwtAuthenticationTokenFilter过滤器
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    //注入官方的认证失败的处理器。此处不写private，符合开闭原则
    //虽然注入了官方的处理器，但最终用的是自己写的
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    AccessDeniedHandler accessDeniedHandler;

    @Bean
    //把官方的PasswordEncoder密码加密方式替换成BCryptPasswordEncoder
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //由于是前后端分离项目，关闭csrf
                .csrf().disable()
                //由于是前后端分离项目，所以session是失效的，我们就不通过Session获取
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //and()是配置安全设置时的链式调用
                .and()
                //指定让spring Security放行登录接口的规则
                .authorizeRequests()

                // 对于登录接口 anonymous表示允许匿名访问
                .antMatchers("/user/login").anonymous()

                //为方便测试认证过滤器，把查询友链的接口设置为需要登录才能访问
                // .antMatchers("/link/getAllLink").authenticated()//不再需要测试则可以关闭
                //为了方便测试查询个人信息，把查询个人信息的接口设置为需要登录才能访问
                //.antMatchers("/user/userInfo").authenticated()

                //把文件上传的接口设置为需要登录才能访问
                //.antMatchers("/upload").authenticated()

                //退出登录的配置。若没登录就调用退出登录，则报错为“401 需要登录后操作”，也就是authenticated
                //.antMatchers("/logout").authenticated()

                // 除上面外的所有请求全部不需要认证即可访问
                //.anyRequest().permitAll()
                // 除上面外的所有请求外，其他全部都需要认证才可访问
                .anyRequest().authenticated();

        //把自定义异常处理器配置给Security
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        //禁用默认的注销功能
        http.logout().disable();

        //把JwtAuthenticationTokenFilter过滤器添加到Security的过滤器链中
        //参数一是要添加的过滤器；参数二是把过滤器添加到哪个security官方过滤器之前
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //SpringSecurity配置允许跨域
        http.cors();
    }
}