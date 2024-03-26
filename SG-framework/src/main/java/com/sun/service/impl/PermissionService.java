package com.sun.service.impl;

import com.sun.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {

    /**
     * 判断当前用户是否具有permission
     * @param permission  要判断的权限
     * @return
     */
    public boolean hasPermission(String permission){
        //如果是超级管理员，直接返回true
        if (SecurityUtils.isAdmin()){
            return true;
        }

        //若不是超级管理员
        //获取当前登录用户所具有的权限列表
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        //如果用户具有对应权限就返回true
        return permissions.contains(permission);
    }
}
