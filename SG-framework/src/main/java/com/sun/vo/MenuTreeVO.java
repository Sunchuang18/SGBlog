package com.sun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuTreeVO {
    private static final long serialVersionUID = 1L;

    //节点id
    private Long id;

    //节点名称
    private String label;

    private Long parentId;

    //子节点
    private List<MenuTreeVO> children;
}
