package com.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.domain.Article;
import com.sun.domain.ResponseResult;
import com.sun.dto.AddArticleDto;
import com.sun.dto.ArticleDto;
import com.sun.vo.ArticleByIdVO;
import com.sun.vo.PageVO;

public interface ArticleService extends IService<Article> {
    //文章列表
    ResponseResult hotArticleList();

    //分类查询文章列表
    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    //根据id查询文章详情
    ResponseResult getArticleDetail(Long id);

    //根据文章id从mysql查询文章
    ResponseResult updateViewCount(Long id);

    //新增博客文章
    ResponseResult add(AddArticleDto article);

    //管理后台（文章管理）-分页查询文章
    PageVO selectArticlePage(Article article, Integer pageNum, Integer pageSize);

    //修改文章-①根据文章id查询对应的文章
    ArticleByIdVO getInfo(Long id);
    //修改文章-②修改查询到的文章
    void edit(ArticleDto article);
}
