package com.sun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.domain.ResponseResult;
import com.sun.domain.Tag;
import com.sun.dto.TabListDto;
import com.sun.dto.TagListDto;
import com.sun.vo.PageVO;
import com.sun.vo.TagVO;

import java.util.List;

public interface TagService extends IService<Tag> {

    //查询标签列表
    ResponseResult<PageVO> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    //新增标签
    ResponseResult addTag(TabListDto tagListDto);

    //删除标签
    ResponseResult deleteTag(Long id);

    //修改标签_①根据标签的id来查询标签
    ResponseResult getTableById(Long id);
    //修改标签_②根据标签的id来修改标签
    ResponseResult myUpdateById(TagVO tagVO);

    //写博文--查询文章标签的接口
    List<TagVO> listAllTag();
}
