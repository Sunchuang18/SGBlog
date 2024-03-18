package com.sun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.domain.Role;
import com.sun.mapper.RoleMapper;
import com.sun.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService{

    //查询用户的角色信息
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员，如果是，就返回集合中只需要有admin
        if (id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则就不是管理员
        //查询对应普通用户所具有的角色信息
        List<String> otherRole = getBaseMapper().selectRoleKeyByOtherUserId(id);

        return otherRole;
    }
}
