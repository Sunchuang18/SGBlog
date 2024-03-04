package com.sun.service;

import com.sun.domain.ResponseResult;
import com.sun.domain.User;

public interface BlogLoginService {

    ResponseResult login(User user);
}
