package com.sun.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//加入Lombok，相当于引用多个注解
@Data
// Lombok 为类生成一个无参数的构造方法
@NoArgsConstructor
// Lombok 为类生成一个包含所有字段的构造方法
@AllArgsConstructor
//将指定的数据库表和 JavaBean 进行映射
@TableName("sg_article")
public class Article {
    //设置id的生成策略
    @TableId
    private Long id;
    //标题
    private String title;
    //文章内容
    private String content;
    //文章摘要
    private String summary;
    //所属分类id
    private Long categoryId;
    //增加字段categoryName，由categoryId查询得到
    //由于数据库没有category_name字段，所以要用注解指定一下字段
    @TableField(exist = false)//代表这个字段在数据库中不存在，避免MyBatisPlus在查询时报错
    private String categoryName;
    //缩略图
    private String thumbnail;
    //是否置顶（0否，1是）
    private String isTop;
    //状态（0已发布，1草稿）
    private String status;
    //访问量
    private Long viewCount;
    //是否允许评论（0否 1是）
    private String isComment;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    //删除标准（0表示未删除 1表示已删除）
    private Integer delFlag;
}
