package com.sun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//返回给前端的特定字段
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO {
    private Long id;
    private String name;

    //描述
    private String description;
}
