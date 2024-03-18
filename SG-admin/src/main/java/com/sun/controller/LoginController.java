package com.sun.controller;

import com.sun.domain.LoginUser;
import com.sun.domain.ResponseResult;
import com.sun.domain.User;
import com.sun.enums.AppHttpCodeEnum;
import com.sun.exception.SystemException;
import com.sun.service.MenuService;
import com.sun.service.RoleService;
import com.sun.service.SystemLoginService;
import com.sun.utils.BeanCopyUtils;
import com.sun.utils.SecurityUtils;
import com.sun.vo.AdminUserInfoVO;
import com.sun.vo.UserInfoVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "管理员登录的相关接口文档")
public class LoginController {

    @Autowired
    private SystemLoginService systemLoginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    //登录
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        //如果用户在进行登录时没有传入“用户名”
        if (!StringUtils.hasText(user.getUserName())){
            //返回提示信息“必须填写用户名”
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return systemLoginService.login(user);
    }

    //查询（超级管理员|非超级管理员）的权限和角色信息
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVO> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVO userInfoVO = BeanCopyUtils.copyBean(user, UserInfoVO.class);

        //封装响应返回
        AdminUserInfoVO adminUserInfoVO = new AdminUserInfoVO(perms, roleKeyList, userInfoVO);
        return ResponseResult.okResult(adminUserInfoVO);
    }
}
