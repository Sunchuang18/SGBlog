package com.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.domain.ResponseResult;
import com.sun.domain.User;
import com.sun.enums.AppHttpCodeEnum;
import com.sun.exception.SystemException;
import com.sun.mapper.UserMapper;
import com.sun.service.UserService;
import com.sun.utils.BeanCopyUtils;
import com.sun.utils.SecurityUtils;
import com.sun.vo.PageVO;
import com.sun.vo.UserInfoVO;
import com.sun.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    //解密加密算法。在SecurityConfig类里覆盖过PasswordEncoder的bean
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    //更新个人信息
    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    //用户注册
    @Override
    public ResponseResult register(User user) {
        //对前端传过来的数据进行非空判断，例如null、"" 就抛出异常
        //用户名
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        //密码
        if (!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        //邮箱
        if (!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //昵称
        if (!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //手机号
        if (!StringUtils.hasText(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_NOT_NULL);
        }

        //判断用户传入的数据是否在数据库中已经存在
        //用户名
        if (UserNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //昵称
        if (NickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //邮箱
        if (EmailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //手机号
        if (PhoneNumberExist(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }

        //经过判断，确保传入的信息无误，可存入数据库
        //注意用户传入的密码信息是明文，先要转为密文后存储
        //加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);

        return ResponseResult.okResult();
    }

    //判断用户传入的用户名在数据库中是否存在
    private boolean UserNameExist(String userName){
        //根据条件查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //用户传入的用户名和数据库的用户名进行比较
        queryWrapper.eq(User::getUserName,userName);
        //如果大于0就说明数据库用相同的数据，也就是数据库中已存在
        return count(queryWrapper) > 0;
    }
    //判断用户传入的昵称在数据库中是否存在
    private boolean NickNameExist(String nickName){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper)>0;
    }
    //判断用户传入的邮箱在数据库中是否存在
    private boolean EmailExist(String email){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        return count(queryWrapper)>0;
    }
    //判断用户传入的手机号在数据库中是否存在
    private boolean PhoneNumberExist(String phoneNumber){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber,phoneNumber);
        return count(queryWrapper)>0;
    }

    //查询用户列表
    @Override
    public ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        //根据用户名模糊搜索
        queryWrapper.like(StringUtils.hasText(user.getUserName()), User::getUserName, user.getUserName());
        //进行状态的查询
        queryWrapper.eq(StringUtils.hasText(user.getStatus()), User::getStatus, user.getStatus());
        //进行手机号的搜索
        queryWrapper.eq(StringUtils.hasText(user.getPhonenumber()), User::getPhonenumber, user.getPhonenumber());

        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);

        //转为VO
        List<User> users = page.getRecords();
        List<UserVO> userVOList = users.stream()
                .map(u -> BeanCopyUtils.copyBean(u, UserVO.class))
                .collect(Collectors.toList());
        PageVO pageVO = new PageVO();
        pageVO.setTotal(page.getTotal());
        pageVO.setRows(userVOList);

        return ResponseResult.okResult(pageVO);
    }
}
