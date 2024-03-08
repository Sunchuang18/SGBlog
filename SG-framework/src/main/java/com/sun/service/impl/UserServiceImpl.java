package com.sun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.domain.ResponseResult;
import com.sun.domain.User;
import com.sun.mapper.UserMapper;
import com.sun.service.UserService;
import com.sun.utils.BeanCopyUtils;
import com.sun.utils.SecurityUtils;
import com.sun.vo.UserInfoVO;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    //查询个人信息
    @Override
    public ResponseResult userInfo() {
        //获取当前用户的用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);

        //封装成UserInfoVO，然后返回
        UserInfoVO userInfoVO = BeanCopyUtils.copyBean(user, UserInfoVO.class);
        return ResponseResult.okResult(userInfoVO);
    }
}
