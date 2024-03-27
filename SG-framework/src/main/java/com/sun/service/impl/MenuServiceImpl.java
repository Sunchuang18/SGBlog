package com.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.constants.SystemConstants;
import com.sun.domain.Menu;
import com.sun.mapper.MenuMapper;
import com.sun.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    /**
     * 查询用户的路由信息
     * @param userId
     * @return
     */
    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;

        //判断是否是超级管理员
        //用户id为1代表超级管理员，如果是就返回所有符合要求的权限菜单
        if (userId.equals(1L)){
            menus = menuMapper.selectAllRouterMenu();
        } else {
            //如果不是超级管理员，就查询对应用户所具有的路由信息（权限菜单）
            menus = menuMapper.selectOtherRouterMenuTreeByUserId(userId);
        }

        //构建成tree，也就是子父菜单树，有层级关系
        List<Menu> menuTree = buildMenuTree(menus, 0L);

        return menuTree;
    }

    /**
     * 把List集合里的数据构建城tree，也就是子父菜单树，有层级关系
     * @param menus
     * @param parentId
     * @return
     */
    private List<Menu> buildMenuTree(List<Menu> menus, Long parentId){
        List<Menu> menuTree = menus.stream()
                //过滤找出父菜单树，也就是第一层
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取传入参数的子菜单，并封装为List集合返回
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus){
        List<Menu> childrenList = menus.stream()
                //过滤得到子菜单
                .filter(m -> m.getParentId().equals(menu.getId()))
                //如果有三层菜单的话，也就是子菜单的子菜单，就用下面这行代码递归来处理
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

    //查询菜单列表
    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //menuName模糊查询
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()), Menu::getMenuName, menu.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menu.getStatus()), Menu::getStatus, menu.getStatus());
        //排序parent_id和order_num
        queryWrapper.orderByDesc(Menu::getParentId, Menu::getOrderNum);
        //封装
        List<Menu> menus = list(queryWrapper);
        return menus;
    }
}
