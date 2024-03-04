package com.sun.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
//@Accessors(chain = true) ：setter 方法会返回当前对象的实例（即 this），从而允许进行链式调用。
@Accessors(chain = true)
public class UserInfoVO {
    //主键
    private Long id;
    //昵称
    private String nickName;
    //头像
    private String avatar;
    private String sex;
    private String email;
}
