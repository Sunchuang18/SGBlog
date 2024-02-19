package com.sun.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//分类表。对应sg_category表
@Data
@AllArgsConstructor
@NoArgsConstructor
////将指定的数据库表和 JavaBean 进行映射
@TableName("sg_category")
public class Category {
    @TableId
    private Long id;

    //分类名
    private String name;
    //父分类id，如果没有父分类则为-1
    private Long pid;
    //描述
    private String status;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    //删除标志（0表示未删除，1表示已删除）
    private Integer delFlag;

}
