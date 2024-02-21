package com.sun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//封装文章详情的实体类。只把需要的字段返回给前端
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVO {
    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    //文章详情代码实现。新增了文章的分类id
    private Long categoryId;
    //文章详情代码实现。新增了文章的内容，也就是详情
    private String content;
    //所属分类名
    private String categoryName;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;
    private Date createTime;
}
