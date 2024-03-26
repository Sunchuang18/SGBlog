package com.sun.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//UserDetails是SpringSecurity官方提供的接口
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private User user;

    //权限信息的集合
    private List<String> permissions;

    //用于返回权限信息。现在正在实现'认证'，'权限'后面才用得到。所以返回null即可
    //当要查询用户信息的时候，不能单纯返回null，要重写这个方法，作用是返回权限信息
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    //用于获取用户密码。由于使用的实体类是User，所以获取的是数据库的用户密码
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    //用于获取用户名。由于使用的实体类是User，所以获取的是数据库的用户名
    @Override
    public String getUsername() {
        return user.getUserName();
    }

    //判断登录状态是否过期。把这个改成true，表示永不过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //判断账号是否被锁定。把这个改成true，表示未锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //判断登录凭证是否过期。把这个改成true，表示永不过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //判断用户是否可用。把这个改成true，表示可用状态
    @Override
    public boolean isEnabled() {
        return true;
    }
}
