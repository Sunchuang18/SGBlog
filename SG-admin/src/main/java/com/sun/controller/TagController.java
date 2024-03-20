package com.sun.controller;

import com.sun.domain.ResponseResult;
import com.sun.dto.TagListDto;
import com.sun.service.TagService;
import com.sun.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/tag")
@Api(tags = "标签的接口文档")
public class TagController {
    @Autowired
    private TagService tagService;

    //查询标签列表
    @GetMapping("/list")
    @ApiOperation("查询标签列表")
    public ResponseResult<PageVO> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }
}
