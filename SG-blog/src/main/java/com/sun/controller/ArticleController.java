package com.sun.controller;

import com.sun.annotation.mySystemLog;
import com.sun.domain.Article;
import com.sun.domain.ResponseResult;
import com.sun.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@Api(tags = "文章的相关接口文档")
public class ArticleController {

    @Autowired
    // 注入公共模块的ArticleService接口
    private ArticleService articleService;

    //测试mybatisPlus
    @GetMapping("/list")
    //自定义在swagger中请求接口的信息
    @ApiOperation("查询数据库的所有数据")
    @mySystemLog(businessName = "查询数据库的所有数据")
    //http://localhost:8080/article/list
    // Article是公共模块的实体类
    public List<Article> test(){
        // 查询数据库的所有数据
        return articleService.list();
    }

    //测试统一响应格式
    @GetMapping("/hotArticleList")
    //自定义在swagger中请求接口的信息
    @ApiOperation("查询热门文章")
    @mySystemLog(businessName = "查询热门文章")
    //http://localhost:8080/article/hotArticleList
    //ResponseResult是SG-framework工程的domain目录的类
    public ResponseResult hotArticleList(){
        //查询热门文章，封装成ResponseResult返回
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    //分类查询文章的列表
    @GetMapping("/articleList")
    //自定义在swagger中请求接口的信息
    @ApiOperation("分类查询文章的列表")
    @mySystemLog(businessName = "分类查询文章的列表")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId){
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    //查询文章详情
    //此处使用路径参数形式的HTTP请求。注意参数只有加@PathVariable注解才能接收路径参数形式的HTTP请求。
    @GetMapping("/{id}")//@PathVariable指定的id与@GetMapping指定的id一致
    //自定义在swagger中请求接口的信息
    @ApiOperation("查询文章详情")
    @mySystemLog(businessName = "查询文章详情")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    //从mysql根据文章id查询文章浏览量
    @PutMapping("/updateViewCount/{id}")
    //自定义在swagger中请求接口的信息
    @ApiOperation("根据文章id从mysql查询文章")
    @mySystemLog(businessName = "根据文章id从mysql查询文章")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
