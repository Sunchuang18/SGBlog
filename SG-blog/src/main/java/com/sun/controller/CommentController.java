package com.sun.controller;

import com.sun.annotation.mySystemLog;
import com.sun.constants.SystemConstants;
import com.sun.domain.Comment;
import com.sun.domain.ResponseResult;
import com.sun.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    //在文章的评论区发送评论
    @PostMapping
    @mySystemLog(businessName = "在文章的评论区发送评论")
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    //在友链的评论区发送评论
    @GetMapping("/linkCommentList")
    @mySystemLog(businessName = "在友链的评论区发送评论")
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT, null, pageNum, pageSize);
    }
}
