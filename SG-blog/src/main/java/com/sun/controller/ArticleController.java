package com.sun.controller;

import com.sun.domain.Article;
import com.sun.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    // 注入公共模块的ArticleService接口
    private ArticleService articleService;

    @GetMapping("/list")
    // Article是公共模块的实体类
    public List<Article> test(){
        // 查询数据库的所有数据
        return articleService.list();
    }
}
