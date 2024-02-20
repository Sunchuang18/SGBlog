package com.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.constants.SystemConstants;
import com.sun.domain.Article;
import com.sun.domain.Category;
import com.sun.domain.ResponseResult;
import com.sun.mapper.ArticleMapper;
import com.sun.service.ArticleService;
import com.sun.service.CategoryService;
import com.sun.utils.BeanCopyUtils;
import com.sun.vo.ArticleListVO;
import com.sun.vo.HotArticleVO;
import com.sun.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        /*
        //解决：把结果封装在HotArticleVO实体类里，此实体类只写需要的字段
        List<HotArticleVO> articleVos = new ArrayList<>();
        for (Article xxarticle : articles) {
            HotArticleVO xxvo = new HotArticleVO();
            //使用Spring提供的BeanUtils类来实现Bean拷贝。第一个参数是元数据，第二个参数是目标数据，把源数据拷贝给目标数据。
            //虽然xxarticle里有很多不同的字段，但xxvo里只有3个字段。所以拷贝之后，就能把xxvo里的3个字段填充具体数据。
            BeanUtils.copyProperties(xxarticle,xxvo);
            articleVos.add(xxvo);
        }
        */
        //将局部Bean拷贝注释。使用定义的BeanCopyUtils工具类的copyBeanList方法。如下：
        List<HotArticleVO> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVO.class);

        return ResponseResult.okResult(articleVos);
    }

    @Autowired
    private CategoryService categoryService;
    //分类查询文章的列表
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //判空。若前端传了categoryId参数，则查询时要和传入的相同
        //参数二是数据表的文章id。参数三十前端传来的文章id
        lambdaQueryWrapper.eq(
                Objects.nonNull(categoryId)&&categoryId>0,
                Article::getCategoryId,
                categoryId);
        //只查询状态是否正式发布的文章
        //Article实体类的status字段跟0作比较，一样则为正式发布
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop字段进行降序排序，实现置顶的文章（idTop值为1）在最前面
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);

        /*
            Article实体类有categoryId字段，但是没有categoryName字段的问题
            解决categoryName字段没有返回值的问题。在分页之后，封装成ArticleListVO之前，进行处理
        */
        /*
            用categoryId来查询categoryName（category表的name字段），也就是查询“分类名称”。
            有两种方法：
                ① for循环遍历
                ② stream流
         */
        List<Article> articles = page.getRecords();
        //方法一：for循环遍历
        /*
        for (Article article : articles) {
            //“article.getCategoryId()”表示从article表获取category_id字段，然后作为查询category表的name字段
            Category category = categoryService.getById(article.getCategoryId());
            //把查询出来的category表的name字段赋值，设置给Article实体类的categoryName成员变量
            article.setCategoryName(category.getName());
        }
         */
        //方法二：stream流
        articles.stream()
                .map(new Function<Article, Article>() {
                    @Override
                    public Article apply(Article article) {
                        Category category = categoryService.getById(article.getCategoryId());
                        String name = category.getName();
                        //把查询出来的category表的name字段值，设置给Article实体类的categoryName成员变量
                        article.setCategoryName(name);
                        return article;
                    }
                })
                .collect(Collectors.toList());

        //把最后的查询结果封装成ArticleListVO
        List<ArticleListVO> articleListVOS = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVO.class);

        //把以上的查询结果和文章总数封装在PageVO
        PageVO pageVO = new PageVO(articleListVOS, page.getTotal());
        return ResponseResult.okResult(pageVO);
    }


}
