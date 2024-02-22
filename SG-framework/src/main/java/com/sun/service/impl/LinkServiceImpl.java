package com.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.constants.SystemConstants;
import com.sun.domain.Link;
import com.sun.domain.ResponseResult;
import com.sun.mapper.LinkMapper;
import com.sun.service.LinkService;
import com.sun.utils.BeanCopyUtils;
import com.sun.vo.LinkVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("/linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的友链
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        //要求查询的友链表status字段的值为0
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        //查询所有status为0的信息并赋值给List
        List<Link> links = list(queryWrapper);
        //把查询道德结果（links）封装成LinkListVO
        List<LinkVO> linkVOs = BeanCopyUtils.copyBeanList(links, LinkVO.class);
        //封装响应返回
        return ResponseResult.okResult(linkVOs);
    }
}
