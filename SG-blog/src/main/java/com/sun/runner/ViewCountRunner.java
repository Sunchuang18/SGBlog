package com.sun.runner;

import com.sun.domain.Article;
import com.sun.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import com.sun.utils.RedisCache;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//启动预处理。当项目启动时，把博客的浏览量(id和viewCount字段)存储到redis中
//实现CommandLineRunner接口，并把对应的bean注入容器。这样就会在应用启动时执行对应的代码
@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleMapper articleMapper;

    //Stream流+方法引用+Lambda
    @Override
    public void run(String... args) throws Exception {
        //查询数据库中的博客信息，注意只需要查询id、viewCount字段的博客信息
        List<Article> articles = articleMapper.selectList(null);//为null即无条件，表示查询所有
        //stream()方法将articles集合转换为一个流，这个流通过collect方法进行收集。
        //collect方法使用Collectors.toMap来创建一个Map。
        //这个Map的键是文章的ID（转换为String类型），值是文章的浏览次数（转换为Integer类型）。
        Map<String, Integer> viewCountMap = articles.stream()
                        .collect(Collectors.toMap(article -> article.getId().toString(),
                                                  article -> article.getViewCount().intValue()));
        //把查询到的id作为key，viewCount作为value，存入Redis
        redisCache.setCacheMap("article:viewCount",viewCountMap);
    }
}
