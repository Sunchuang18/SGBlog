package com.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.domain.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {

    //查询用户的权限信息
    List<String> selectPermsByUserId(Long id);
}
