package com.sun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//新增博客文章。接收前端传来的参数
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddArticleDto {
    private Long id;
    //标题
    private String title;
    //文章内容
    private String content;
    //文章概要
    private String summary;
    //所属分类ID
    private Long categoryId;
    //缩略图
    private String thumbnail;
    //是否置顶（0否，1是）
    private String idTop;
    //状态（0已发布，1草稿）
    private String status;
    //访问量
    private Long viewComment;
    //是否允许评论（1是，0否）
    private String isComment;
    //tags属性是一个List集合，用于接收文章关联标签的id
    private List<Long> tags;
}
