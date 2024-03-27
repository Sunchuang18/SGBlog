package com.sun.controller;

import com.sun.domain.ResponseResult;
import com.sun.domain.Role;
import com.sun.dto.ChangeRoleStatusDto;
import com.sun.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    //修改角色的状态
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDto roleStatusDto){
        Role role = new Role();
        role.setId(roleStatusDto.getRoleId());
        role.setStatus(roleStatusDto.getStatus());
        return ResponseResult.okResult(roleService.updateById(role));
    }
}
