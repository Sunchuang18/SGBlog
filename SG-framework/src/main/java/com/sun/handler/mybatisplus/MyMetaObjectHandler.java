package com.sun.handler.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sun.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/*
    这个类是用来配置mybatis的字段自动填充。用于'发送评论'功能。[createTime、createBy、updateTime、updateBy]
    由于在评论表无法对这四个字段进行插入数据(原因是前端在发送评论时没有在请求体提供这四个参数，所以后端向数据库插入数据时，这四个字段是空值)，
    所有就需要此类来辅助往这四个字段自动的插入值，只要更新了评论表的字段，就会自动将值插入到这四个无法插入值的字段
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    //只要对数据库执行了插入预计，就会执行此方法
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = null;
        try{
            //获取用户id
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            //如果异常了，就说明该用户还未注册，就把该用户的userid字段赋值为-1
            userId = -1L;
        }
        //自动把下面四个字段新增了值
        //设置metaObject所代表的Java对象的createTime字段的值为当前的日期和时间
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createBy", userId, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("", SecurityUtils.getUserId(), metaObject);
    }
}
