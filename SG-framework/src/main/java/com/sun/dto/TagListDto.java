package com.sun.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "查询标签的请求参数dto")
public class TagListDto {
    /*
    请求参数
        用户可传可不传
        这两个参数是给用户在搜索框根据name或remark查询对应的标签
     */
    private String name;
    private String remark;
}
