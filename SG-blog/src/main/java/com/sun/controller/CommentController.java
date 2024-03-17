package com.sun.controller;

import com.sun.annotation.mySystemLog;
import com.sun.constants.SystemConstants;
import com.sun.domain.Comment;
import com.sun.domain.ResponseResult;
import com.sun.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
//自定义添加swagger信息。tags：标签。description：描述信息。
@Api(tags = "评论的接口文档", description = "描述信息")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    //自定义在swagger中请求接口的信息
    @ApiOperation("查询评论区的评论")
    @mySystemLog(businessName = "查询评论区的评论")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    //在文章的评论区发送评论
    @PostMapping
    //自定义在swagger中请求接口的信息。默认是value属性
    @ApiOperation("在文章的评论区发送评论")
    @mySystemLog(businessName = "在文章的评论区发送评论")
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    //在友链的评论区发送评论
    @GetMapping("/linkCommentList")
    //自定义在swagger中请求接口的信息
    @ApiOperation(value = "友链评论列表", notes = "获取友链评论区的评论")
    @mySystemLog(businessName = "在友链的评论区发送评论")
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT, null, pageNum, pageSize);
    }
}
