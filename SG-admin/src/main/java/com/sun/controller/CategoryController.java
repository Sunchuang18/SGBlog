package com.sun.controller;

import com.sun.domain.Category;
import com.sun.domain.ResponseResult;
import com.sun.service.CategoryService;
import com.sun.vo.CategoryVO;
import com.sun.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //写博客--查询分类的接口
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVO> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }

    //分页查询分类列表
    @GetMapping("/list")
    public ResponseResult list(Category category, Integer pageNum, Integer pageSize){
        PageVO pageVO = categoryService.selectCategoryPage(category, pageNum, pageSize);
        return ResponseResult.okResult(pageVO);
    }
}
