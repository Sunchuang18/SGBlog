package com.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.domain.Article;
import com.sun.domain.ResponseResult;

public interface ArticleService extends IService<Article> {
    //定义 hotArticleList方法
    ResponseResult hotArticleList();
}
