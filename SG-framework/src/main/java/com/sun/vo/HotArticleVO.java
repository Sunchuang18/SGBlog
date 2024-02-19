package com.sun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    用VO来接收查询的结果，一个接口对应一个VO
*/
@Data
//@NoArgsConstructor在类上使用，可以生成无参构造方法
@NoArgsConstructor
//@AllArgsConstructor在类上使用，可以生成全参构造函数，且默认不生成无参构造函数。
@AllArgsConstructor
// 返回给前端特定的字段
public class HotArticleVO {
    private Long id;
    //标题
    private String title;
    //访问量
    private Long viewCount;
}
