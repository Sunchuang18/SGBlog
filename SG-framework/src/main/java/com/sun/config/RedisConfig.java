package com.sun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    /*
    @SuppressWarnings用于告诉编译器忽略指定的警告信息。
    这通常用于消除那些开发者认为不是问题的警告，或者当警告信息与实际代码逻辑不符时。
        unchecked: 用于消除未经检查的转换警告
        rawtypes:用于消除与原始类型相关的编译器警告。

    */
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
        //创建RedisTemplate实例，用于执行各种Redis操作，此实例将用于存储和检索Object类型的键和值
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        //创建 FastJsonRedisSerializer 的实例，并指定泛型类型为 Object.class
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);

        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        //Hash的key也采用StringRedisSerializer的序列方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();

        return template;
    }
}
