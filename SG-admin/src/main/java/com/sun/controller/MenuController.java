package com.sun.controller;


import com.sun.domain.Menu;
import com.sun.domain.ResponseResult;
import com.sun.service.MenuService;
import com.sun.utils.BeanCopyUtils;
import com.sun.vo.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
