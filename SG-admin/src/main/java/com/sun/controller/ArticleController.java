package com.sun.controller;

import com.sun.domain.ResponseResult;
import com.sun.dto.AddArticleDto;
import com.sun.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    //新增博客文章
    @Autowired
    private ArticleService articleService;

    @PostMapping
    @PreAuthorize("@ps.hasPermission('content:article:writer')")//权限控制
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }
}
