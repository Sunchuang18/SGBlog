package com.sun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.domain.Role;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    //查询普通用户的角色权限
    List<String> selectRoleKeyByOtherUserId(Long userId);
}
