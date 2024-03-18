package com.sun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//当 @Accessors 注解的 chain 属性设置为 true 时，setter 方法会返回 this 而不是 void
@Accessors(chain = true)
public class AdminUserInfoVO {

    private List<String> permissions;

    private List<String> roles;

    private UserInfoVO user;
}
