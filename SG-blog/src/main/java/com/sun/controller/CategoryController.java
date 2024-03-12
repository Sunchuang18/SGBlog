package com.sun.controller;

import com.sun.annotation.mySystemLog;
import com.sun.domain.ResponseResult;
import com.sun.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    //CategoryService是在SG-framework工程写的接口
    @Autowired
    private CategoryService categoryService;

    //ResponseResult是在SG-framework工程写的实体类
    @GetMapping("/getCategoryList")
    @mySystemLog(businessName = "查询文章分类")
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();
    }
}
