package com.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.constants.SystemConstants;
import com.sun.domain.Article;
import com.sun.domain.Category;
import com.sun.domain.ResponseResult;
import com.sun.mapper.CategoryMapper;
import com.sun.service.ArticleService;
import com.sun.service.CategoryService;
import com.sun.utils.BeanCopyUtils;
import com.sun.vo.CategoryVO;
import com.sun.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    //ArticleService是在sun-framework写的接口
    @Autowired
    private ArticleService articleService;

    //查询分类列表的核心代码
    @Override
    public ResponseResult getCategoryList(){
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        //要求查的是getStatus字段的值为1，注意SystemConstants是自定义常量类
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //查询文章列表，条件是上面的结果articleWrapper
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重。使用stream流来处理上一行得到的文章表集合
        Set<Long> categoryIds = articleList.stream()
                .map(new Function<Article, Long>() {
                    @Override
                    public Long apply(Article article) {
                        return article.getCategoryId();
                    }
                })
                .collect(Collectors.toSet());//把文章的分类id转换为Set集合

        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        //封装成CategoryVO实体类后返回给前端，CategoryVO的作用是只返回前端需要的字段。
        List<CategoryVO> categoryVOs = BeanCopyUtils.copyBeanList(categories, CategoryVO.class);
        return ResponseResult.okResult(categoryVOs);
    }

    //写博客--查询分类的接口
    @Override
    public List<CategoryVO> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        //查询处于正常状态的分类
        wrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        //把查询到的结果存入到list集合中
        List<Category> list = list(wrapper);
        //把list集合拷贝到VO中，并返回给前端
        List<CategoryVO> categoryVOS = BeanCopyUtils.copyBeanList(list, CategoryVO.class);
        return categoryVOS;
    }

    //分页查询分类列表
    @Override
    public PageVO selectCategoryPage(Category category, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //如果category的name字段有值，则在queryWrapper上添加一个模糊查询，查询Category表中name字段包含category.getName()返回值的记录
        queryWrapper.like(StringUtils.hasText(category.getName()), Category::getName, category.getName());
        //如果category的status字段非空，则在queryWrapper上添加一个精确匹配条件，查询Category表中status字段等于category.getStatus()返回值的记录
        queryWrapper.eq(Objects.nonNull(category.getStatus()), Category::getStatus, category.getStatus());

        Page<Category> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);

        //转换VO
        List<Category> categories = page.getRecords();

        PageVO pageVO = new PageVO();
        pageVO.setTotal(page.getTotal());
        pageVO.setRows(categories);
        return pageVO;
    }
}
