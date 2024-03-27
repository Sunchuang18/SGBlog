package com.sun.controller;


import com.sun.domain.Menu;
import com.sun.domain.ResponseResult;
import com.sun.service.MenuService;
import com.sun.utils.BeanCopyUtils;
import com.sun.vo.MenuVO;
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
}
