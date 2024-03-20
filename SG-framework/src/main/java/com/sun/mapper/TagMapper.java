package com.sun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.domain.Tag;
import org.apache.ibatis.annotations.Param;

public interface TagMapper extends BaseMapper<Tag> {

    //删除标签。此处是逻辑删除，要自己些SQL语句
    int myUpdateById(@Param("id") Long id, @Param("flag") int flag);
}
