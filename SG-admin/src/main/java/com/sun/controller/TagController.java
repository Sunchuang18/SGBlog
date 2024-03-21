package com.sun.controller;

import com.sun.domain.ResponseResult;
import com.sun.domain.Tag;
import com.sun.dto.AddTagDto;
import com.sun.dto.EditTagDto;
import com.sun.dto.TagListDto;
import com.sun.service.TagService;
import com.sun.utils.BeanCopyUtils;
import com.sun.vo.PageVO;
import com.sun.vo.TagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //新增标签
    @PostMapping
    public ResponseResult add(@RequestBody AddTagDto tagDto){
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        tagService.save(tag);
        return ResponseResult.okResult();
    }

    //删除标签
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Long id){
        return tagService.deleteTag(id);
    }

    //修改标签
    //① 根据标签的id来查询标签
    @GetMapping("/{id}")
    public ResponseResult getTableById(@PathVariable Long id){
        return tagService.getTableById(id);
    }
    //② 根据标签的id来修改标签
    @PutMapping
    public ResponseResult updateById(@RequestBody TagVO tagVO){
        return tagService.myUpdateById(tagVO);
    }

    //写博文--查询文章标签的接口
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVO> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }
}
