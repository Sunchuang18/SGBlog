package com.sun.controller;

import com.sun.domain.Article;
import com.sun.domain.ResponseResult;
import com.sun.dto.AddArticleDto;
import com.sun.service.ArticleService;
import com.sun.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    //分页查询博客文章
    @GetMapping("/list")
    public ResponseResult list(Article article, Integer pageNum, Integer pageSize){
        PageVO pageVO = articleService.selectArticlePage(article, pageNum, pageSize);
        return ResponseResult.okResult(pageVO);
    }
}
