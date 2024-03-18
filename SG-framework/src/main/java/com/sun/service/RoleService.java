package com.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.domain.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    //查询用户的角色信息
    List<String> selectRoleKeyByUserId(Long id);
}
