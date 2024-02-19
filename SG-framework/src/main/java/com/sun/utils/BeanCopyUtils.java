package com.sun.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

//BeanUtils工具类，Bean拷贝工具类
public class BeanCopyUtils {

    //私有的空参构造方法
    private BeanCopyUtils(){}

    //1、单个实体类的拷贝。参数一是要拷贝的对象，参数二是累的字节码对象
    public static <V> V copyBean(Object source,Class<V> clazz){
        //创建目标对象
        V result = null;
        try{
            result = clazz.newInstance();
            //实现属性拷贝。把source的属性拷贝到这个目标对象。BeanUtils是Spring提供的工具类
            BeanUtils.copyProperties(source,result);
        } catch (Exception e){
            e.printStackTrace();
        }
        //返回结果
        return result;
    }

    //2、集合的拷贝。参数一是要拷贝的集合，参数二是累的字节码对象
    public static <O,V> List<V> copyBeanList(List<O> list,Class<V> clazz){
        //不使用for循环，使用stream流进行转换
        return list.stream()
                .map(o -> copyBean(o,clazz))
                .collect(Collectors.toList());//把结果转换为集合
    }
}
