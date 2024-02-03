package com.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.domain.Article;
import com.sun.domain.ResponseResult;
import com.sun.mapper.ArticleMapper;
import com.sun.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//ServiceImpl是mybatisPlus官方提供的
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章，封装成ResponseResult返回。把所有查询条件写在queryWrapper里
        //queryWrapper是mybatis plus中实现查询的对象封装操作类
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //查询的不能是草稿。即Status字段不能是0
        //例: eq("name", "老王")--->name = '老王'
        queryWrapper.eq(Article::getStatus,0);
        //按照浏览量进行排序。也就是根据ViewCount字段降序排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只能查询出来10条信息。当前显示第一页的数据，每页显示10条数据
        Page<Article> page = new Page<>(1,10);
        page(page,queryWrapper);
        //获取最终的查询结果
        List<Article> articles = page.getRecords();

        return ResponseResult.okResult(articles);
    }
}
