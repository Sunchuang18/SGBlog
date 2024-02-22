package com.sun.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//友链（Link）表实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sg_link")
public class Link {
    @TableId
    private Long id;
    private String name;
    private String logo;
    private String description;
    //网站地址
    private String address;
    //审核状态（0：审核通过。1：审核未通过。2：未审核）
    private String status;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    //删除标记
    private Integer delFlag;
}
