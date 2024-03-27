package com.sun.controller;


import com.sun.domain.Menu;
import com.sun.domain.ResponseResult;
import com.sun.service.MenuService;
import com.sun.utils.BeanCopyUtils;
import com.sun.utils.SystemConverter;
import com.sun.vo.MenuTreeVO;
import com.sun.vo.MenuVO;
import com.sun.vo.RoleMenuTreeSelectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    //查询菜单列表
    @GetMapping("/list")
    public ResponseResult list(Menu menu){
        List<Menu> menus = menuService.selectMenuList(menu);
        //转为VO
        List<MenuVO> menuVOS = BeanCopyUtils.copyBeanList(menus, MenuVO.class);
        return ResponseResult.okResult(menuVOS);
    }

    //新增菜单
    @PostMapping
    public ResponseResult add(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    //修改菜单
    //①先根据菜单id查询对应的权限菜单
    @GetMapping(value = "/{menuId}")
    public ResponseResult getInfo(@PathVariable Long menuId){
        return ResponseResult.okResult(menuService.getById(menuId));
    }
    //②更新查询到的权限菜单
    @PutMapping
    public ResponseResult edit(@RequestBody Menu menu){
        //不能把父菜单设置为当前菜单
        if (menu.getId().equals(menu.getParentId())){
            return ResponseResult.errorResult(500, "修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }

    //删除菜单
    @DeleteMapping("/{menuId}")
    public ResponseResult remove(@PathVariable("menuId") Long menuId){
        if (menuService.hasChild(menuId)){
            return ResponseResult.errorResult(500, "存在子菜单不允许删除");
        }
        menuService.removeById(menuId);
        return ResponseResult.okResult();
    }

    //新增角色-获取菜单下拉树列表
    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        //复用之前的selectMenuList方法。方法需要参数(参数可以用来进行条件查询)，但方法不需要天骄，所以直接new对象传入
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<MenuTreeVO> options = SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }

    //修改角色-根据角色id查询对应角色菜单列表树
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("roleId") Long roleId){
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<Long> checkedKeys = menuService.selectMenuListByRoleId(roleId);
        List<MenuTreeVO> menuTreeVOS = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVO vo = new RoleMenuTreeSelectVO(checkedKeys, menuTreeVOS);
        return ResponseResult.okResult(vo);
    }
}
