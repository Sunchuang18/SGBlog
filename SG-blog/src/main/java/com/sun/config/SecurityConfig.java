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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//实现Security提供的WebSecurityConfigurerAdapter类，就可以改变密码校验的规则了
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //注入JwtAuthenticationTokenFilter过滤器
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    //把官方的人PasswordEncoder密码加密方式替换成BCryptPasswordEncoder
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
                .antMatchers("/login").anonymous()

                //为方便测试认证过滤器，把查询友链的接口设置为需要登录才能访问
                .antMatchers("/link/getAllLink").authenticated()

                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().permitAll();

        //禁用默认的注销功能
        http.logout().disable();

        //把JwtAuthenticationTokenFilter过滤器添加到Security的过滤器链中
        //参数一是要添加的过滤器；参数二是把过滤器添加到哪个security官方过滤器之前
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //SpringSecurity配置允许跨域
        http.cors();
    }
}
