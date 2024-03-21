package com.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.domain.Category;
import com.sun.domain.ResponseResult;
import com.sun.vo.CategoryVO;

import java.util.List;

public interface CategoryService extends IService<Category> {
    //查询文章分类的接口
    ResponseResult getCategoryList();

    //写博客--查询分类的接口
    List<CategoryVO> listAllCategory();
}
