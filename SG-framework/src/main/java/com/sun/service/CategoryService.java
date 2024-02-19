package com.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.domain.Category;
import com.sun.domain.ResponseResult;

public interface CategoryService extends IService<Category> {
    //查询文章分类的接口
    ResponseResult getCategoryList();
}
