package com.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.domain.Comment;
import com.sun.domain.ResponseResult;

public interface CommentService extends IService<Comment> {
    ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize);
}
