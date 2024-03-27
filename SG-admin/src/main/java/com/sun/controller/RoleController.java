package com.sun.controller;

import com.sun.domain.ResponseResult;
import com.sun.domain.Role;
import com.sun.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    //查询角色列表
    @GetMapping("/list")
    public ResponseResult list(Role role, Integer pageNum, Integer pageSize){
        return roleService.selectRolePage(role, pageNum, pageSize);
    }
}
