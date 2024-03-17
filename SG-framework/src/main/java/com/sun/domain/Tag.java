package com.sun.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 标签(Tag)表 实体类
 */
//告诉编译器忽略关于serialVersionUID字段的警告
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sg_tag")
public class Tag {
    @TableId
    private Long id;
    //标签名
    private String name;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    //删除标记（0代表未删除，1代表已删除）
    private Integer delFlag;
    //备注
    private String remark;
}
