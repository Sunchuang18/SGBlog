package com.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.constants.SystemConstants;
import com.sun.domain.Comment;
import com.sun.domain.ResponseResult;
import com.sun.enums.AppHttpCodeEnum;
import com.sun.exception.SystemException;
import com.sun.mapper.CommentMapper;
import com.sun.service.CommentService;
import com.sun.service.UserService;
import com.sun.utils.BeanCopyUtils;
import com.sun.vo.CommentVO;
import com.sun.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    //根据userid查询用户信息，也就是查username
    @Autowired
    private UserService userService;

    //查询评论区的评论
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        //对articleId进行判断，作用是得到指定的文章
        //如果是文章评论，会判断articleId，避免友链评论判断articleId时出现空指针
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId);

        //对评论区的某条评论的rootID进行判断，如果为-1，就表示是根评论。
        queryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_ROOT);

        //文章的评论，避免查到友链的评论
        queryWrapper.eq(Comment::getType, commentType);

        //分页查询。查的是整个评论区的每一条评论
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        //根评论排序
        // 从Page<Comment>对象中获取当前页的所有记录，返回一个List<Comment>
        List<Comment> sortedComments = page.getRecords().stream()
                // 对流中的元素进行排序。
                // 这里使用了Comparator.comparing来根据Comment对象的getCreateTime方法的返回值进行排序。
                // .reversed()表示按照降序排序，也就是最新的评论会排在最前面。
                .sorted(Comparator.comparing(Comment::getCreateTime).reversed())
                // 将排序后的流重新收集到一个新的列表中
                .collect(Collectors.toList());
        List<CommentVO> commentVOList = returnToCommentList(sortedComments);

        //遍历。查询子评论（注意子评论只能查到二级评论）
        for (CommentVO commentVO : commentVOList) {
            //查询对应的子评论
            List<CommentVO> children = getChildren(commentVO.getId());
            //把查到的children子评论集的集合赋值给commentVO类的children字段
            commentVO.setChildren(children);
        }

        return ResponseResult.okResult(new PageVO(commentVOList, page.getTotal()));
    }

    //在文章的评论区发送评论
    @Override
    public ResponseResult addComment(Comment comment) {
        /*
            注意前端在调用这个发送评论接口时，在请求体是没有传入createTime、createId、updateTime、updateID字段，
            这会导致这四个字段没有值。
            为了解决这个问题，在MyMetaObjectHandler类、Comment类做了自动填充功能。
         */

        //限制用户在发送评论时，评论内容不能为空。为空则抛出异常
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }

        //解决四个字段没有值的情况，可以调用MyBatisPlus提供的save方法向数据库插入数据
        save(comment);

        //封装响应返回
        return ResponseResult.okResult();
    }

    //根据评论区的id来查询对应的所有子评论（注意子评论只能查到二级评论）
    private List<CommentVO> getChildren(Long id){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);
        //对子评论按照时间进行排序
        queryWrapper.orderByDesc(Comment::getCreateTime);
        //将queryWrapper封装为list集合
        List<Comment> comments = list(queryWrapper);
        //调用封装方法对结果进行封装
        List<CommentVO> commentVOS = returnToCommentList(comments);
        return commentVOS;
    }

    //封装响应返回
    private List<CommentVO> returnToCommentList(List<Comment> list){
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
