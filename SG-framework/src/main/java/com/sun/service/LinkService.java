package com.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.domain.Link;
import com.sun.domain.ResponseResult;

public interface LinkService extends IService<Link> {
    //查询友链
    ResponseResult getAllLink();
}
