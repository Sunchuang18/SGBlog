package com.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.domain.ResponseResult;
import com.sun.domain.Tag;
import com.sun.dto.TagListDto;
import com.sun.mapper.TagMapper;
import com.sun.service.TagService;
import com.sun.vo.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    //查询标签列表
    @Override
    public ResponseResult<PageVO> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {

        //分页查询的条件。模糊+分页
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        //参数二、三相互比较。参数一是判空，当用户没有查询条件时，就不去比较后面两个参数
        //如果tagListDto的name属性非空，那么向QueryWrapper添加一个LIKE查询条件，用于在Tag表的name字段中模糊查询与tagListDto的name属性值相匹配的记录
        queryWrapper.like(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        queryWrapper.like(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());

        //分页查询
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);//设置了分页查询的当前页码
        page.setSize(pageSize);//设置了每页的大小，即每页应该返回多少条记录
        page(page, queryWrapper);

        //封装数据返回
        PageVO pageVO = new PageVO(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVO);
    }
}
