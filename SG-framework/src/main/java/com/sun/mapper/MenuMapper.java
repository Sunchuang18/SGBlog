package com.sun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.domain.Menu;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    //查询普通用户的权限信息
    List<String> selectPermsByOtherUserId(Long userId);

    //查询超级管理员的路由信息（权限菜单）
    List<Menu> selectAllRouterMenu();

    //查询普通用户的路由信息（权限菜单）
    List<Menu> selectOtherRouterMenuTreeByUserId(Long userId);

    //修改角色-根据角色id查询对应角色菜单列表树
    List<Long> selectMenuListByRoleId(Long roleId);
}
