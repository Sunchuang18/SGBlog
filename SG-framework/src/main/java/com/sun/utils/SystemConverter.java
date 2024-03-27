package com.sun.utils;

import com.sun.domain.Menu;
import com.sun.vo.MenuTreeVO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//新增角色-获取下拉树列表
public class SystemConverter {
    private SystemConverter(){}

    public static List<MenuTreeVO> buildMenuSelectTree(List<Menu> menus){
        List<MenuTreeVO> menuTreeVOS = menus.stream()
                .map(m -> new MenuTreeVO(m.getId(), m.getMenuName(), m.getParentId(), null))
                .collect(Collectors.toList());
        List<MenuTreeVO> options = menuTreeVOS.stream()
                .filter(o -> o.getParentId().equals(0L))
                .map(o -> o.setChildren(getChildList(menuTreeVOS, o)))
                .collect(Collectors.toList());

        return options;
    }

    //得到子节点
    private static List<MenuTreeVO> getChildList(List<MenuTreeVO> list, MenuTreeVO option){
        List<MenuTreeVO> options = list.stream()
                .filter(o -> Objects.equals(o.getParentId(), option.getId()))
                .map(o -> o.setChildren(getChildList(list, o)))
                .collect(Collectors.toList());
        return options;
    }
}
