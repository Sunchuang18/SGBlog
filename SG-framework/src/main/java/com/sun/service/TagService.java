package com.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.domain.ResponseResult;
import com.sun.domain.Tag;
import com.sun.dto.TagListDto;
import com.sun.vo.PageVO;

public interface TagService extends IService<Tag> {

    //查询标签列表
    ResponseResult<PageVO> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);
}
