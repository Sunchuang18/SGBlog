package com.sun.cronjob;

import com.sun.domain.Article;
import com.sun.service.ArticleService;
import com.sun.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//通过定时任务实现每隔3分钟把redis中浏览量更新到mysql数据库中
@Component
public class UpdateViewCount {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;

    //每隔3分钟，把redis中浏览量更新到mysql数据库
    @Scheduled(cron = "0 0/3 * * * ?")
    public void updateViewCount(){
        //获取redis中的浏览量，注意得到的viewCountMap是HashMap双列集合
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
        //让双列集合调用entrySet方法即可转为单列集合，然后才能调用stream方法
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());//把最终数据转为List集合
        //把获取到的浏览量更新到mysql数据库中
        articleService.updateBatchById(articles);
        //方便在控制台看打印信息
        System.out.println("Redis的文章浏览数据已更新到数据库，现在的时间是："  + LocalTime.now());
    }
}
