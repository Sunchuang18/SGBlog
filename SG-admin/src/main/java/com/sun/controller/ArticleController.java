package com.sun.controller;

import com.sun.domain.Article;
import com.sun.domain.ResponseResult;
import com.sun.dto.AddArticleDto;
import com.sun.dto.ArticleDto;
import com.sun.service.ArticleService;
import com.sun.vo.ArticleByIdVO;
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

    //根据文章id来修改文章
    //①先查询根据文章id查询对应的文章
    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        ArticleByIdVO article = articleService.getInfo(id);
        return ResponseResult.okResult(article);
    }
    //②修改文章
    @PutMapping
    public ResponseResult edit(@RequestBody ArticleDto article){
        articleService.edit(article);
        return ResponseResult.okResult();
    }
}
