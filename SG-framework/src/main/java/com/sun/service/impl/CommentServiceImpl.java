package com.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.constants.SystemConstants;
import com.sun.domain.Comment;
import com.sun.domain.ResponseResult;
import com.sun.mapper.CommentMapper;
import com.sun.service.CommentService;
import com.sun.service.UserService;
import com.sun.utils.BeanCopyUtils;
import com.sun.vo.CommentVO;
import com.sun.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    //根据userid查询用户信息，也就是查username
    @Autowired
    private UserService userService;
    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        //对articleId进行判断，作用是得到指定的文章
        queryWrapper.eq(Comment::getArticleId, articleId);

        //对评论区的某条评论的rootID进行判断，如果为-1，就表示是根评论。
        queryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_ROOT);

        //分页查询。查的是整个评论区的每一条评论
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<CommentVO> commentVOList = xxToCommentList(page.getRecords());

        return ResponseResult.okResult(new PageVO(commentVOList, page.getTotal()));
    }

    //封装响应返回
    private List<CommentVO> xxToCommentList(List<Comment> list){
        //获取评论区的所有评论
        List<CommentVO> commentVOS = BeanCopyUtils.copyBeanList(list, CommentVO.class);
        //遍历。由封装好的数据里没有username字段，暂时不能返回给前端。此遍历用来得到username字段
        for (CommentVO commentVO : commentVOS) {
            //需要根据commentVO类的createBy字段来查询user表的nickname字段
            String nickName = userService.getById(commentVO.getCreateBy()).getNickName();
            //把nickname字段的数据赋值给commentVO类的username字段
            commentVO.setUsername(nickName);

            //查询根评论的用户昵称。（根评论的getToCommentUserId为1）
            if (commentVO.getToCommentUserId() != -1) {
                String toCommentUserName = userService.getById(commentVO.getToCommentUserId()).getNickName();
                //把nickname字段的数据赋值给commentVO类的toCommentUserName字段
                commentVO.setToCommentUserName(toCommentUserName);
            }
        }
        //返回给前端
        return commentVOS;
    }
}
