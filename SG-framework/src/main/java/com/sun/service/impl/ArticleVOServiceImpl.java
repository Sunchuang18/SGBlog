package com.sun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.mapper.ArticleVOMapper;
import com.sun.service.ArticleVOService;
import com.sun.vo.ArticleVO;
import org.springframework.stereotype.Service;

@Service
public class ArticleVOServiceImpl extends ServiceImpl<ArticleVOMapper, ArticleVO> implements ArticleVOService {
}
