package com.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.constants.SystemConstants;
import com.sun.domain.Article;
import com.sun.domain.ResponseResult;
import com.sun.mapper.ArticleMapper;
import com.sun.service.ArticleService;
import com.sun.vo.HotArticleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序。也就是根据ViewCount字段降序排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只能查询出来10条信息。当前显示第一页的数据，每页显示10条数据
        Page<Article> page = new Page<>(SystemConstants.ARTICLE_STATUS_CURRENT,SystemConstants.ARTICLE_STATUS_SIZE);
        page(page,queryWrapper);

        //获取最终的查询结果，把结果封装在Article实体类里面会有很多不需要的字段
        List<Article> articles = page.getRecords();

        //解决：把结果封装在HotArticleVO实体类里，此实体类只写需要的字段
        List<HotArticleVO> articleVos = new ArrayList<>();
        for (Article xxarticle : articles) {
            HotArticleVO xxvo = new HotArticleVO();
            /*
            使用Spring提供的BeanUtils类来实现Bean拷贝
                第一个参数是元数据，第二个参数是目标数据，把源数据拷贝给目标数据。
            虽然xxarticle里有很多不同的字段，但xxvo里只有3个字段
                所以拷贝之后，就能把xxvo里的3个字段填充具体数据。
            */
            BeanUtils.copyProperties(xxarticle,xxvo);
            articleVos.add(xxvo);
        }

        return ResponseResult.okResult(articleVos);
    }
}
