package com.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.constants.SystemConstants;
import com.sun.domain.Menu;
import com.sun.mapper.MenuMapper;
import com.sun.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    //查询用户的权限信息
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //根据用户id查询用户的权限信息

        //用户id为1代表管理员，如果是管理员就返回所有的权限
        if (id == 1L){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            //查询条件是permissions中需要有所有菜单类型为C或F的权限
            wrapper.in(Menu::getMenuType, SystemConstants.TYPE_MENU, SystemConstants.TYPE_BUTTON);
            //查询条件是permissions中需要有状态为正常的权限
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            //查询条件是permissions中需要未被删除的权限的权限
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }

        //如果不是1，即不是管理员，就返回对应用户具有的权限
        List<String> otherPerms = getBaseMapper().selectPermsByOtherUserId(id);
        return otherPerms;
    }
}
