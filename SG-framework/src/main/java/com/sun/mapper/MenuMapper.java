package com.sun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.domain.Menu;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    //查询普通用户的权限信息
    List<String> selectPermsByOtherUserId(Long userId);
}
