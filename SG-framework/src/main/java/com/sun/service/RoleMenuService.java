package com.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.domain.RoleMenu;

public interface RoleMenuService extends IService<RoleMenu> {

    //修改角色-保存修改好的角色信息
    void deleteRoleMenuByRoleId(Long id);
}
