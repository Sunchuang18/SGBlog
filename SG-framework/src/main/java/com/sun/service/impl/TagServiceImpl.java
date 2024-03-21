package com.sun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.domain.LoginUser;
import com.sun.domain.ResponseResult;
import com.sun.domain.Tag;
import com.sun.dto.TabListDto;
import com.sun.dto.TagListDto;
import com.sun.mapper.TagMapper;
import com.sun.service.TagService;
import com.sun.utils.BeanCopyUtils;
import com.sun.utils.SecurityUtils;
import com.sun.vo.PageVO;
import com.sun.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

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

    //新增标签
    @Override
    public ResponseResult addTag(TabListDto tagListDto) {
        Tag tag = new Tag();
        //获取创建人、创建时间
        //获取创建人的id
        LoginUser loginUser = SecurityUtils.getLoginUser();
        tag.setCreateBy(loginUser.getUser().getId());
        //获取创建时间
        try{
            //生成创建时间、更新时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //获取当前时间
            Date now = new Date();
            //将当前时间格式化为指定格式的字符串
            String strNow = dateFormat.format(now);
            //将字符串转换为Date类型
            Date date = dateFormat.parse(strNow);
            //得到创建时间
            tag.setCreateTime(date);
        }catch (ParseException e){
            e.printStackTrace();
        }

        //修改标签名、标签的描述信息
        tag.setName(tagListDto.getName());
        tag.setRemark(tagListDto.getRemark());

        //把输入的信息插入到数据库
        tagMapper.insert(tag);
        return ResponseResult.okResult();
    }

    //删除标签
    @Override
    public ResponseResult deleteTag(Long id) {
        //通过数据id查找数据
        Tag tag = tagMapper.selectById(id);
        //把值传入到数据库进行更新
        if (tag != null){
            //把 def_flag=1 即逻辑删除
            tagMapper.myUpdateById(id, 1);
        }
        return ResponseResult.okResult();
    }

    //修改标签
    //①根据标签的id来查询标签
    @Override
    public ResponseResult getTableById(Long id) {
        Tag tag = tagMapper.selectById(id);
        //封装成VO
        TagVO tagVODate = BeanCopyUtils.copyBean(tag, TagVO.class);
        return ResponseResult.okResult(tagVODate);
    }
    //②根据标签的id来修改标签
    @Override
    public ResponseResult myUpdateById(TagVO tagVO) {
        Tag tag = new Tag();
        //获取更新时间、更新人
        //更新人id
        LoginUser loginUser = SecurityUtils.getLoginUser();
        tag.setUpdateBy(loginUser.getUser().getId());
        //创建时间、更新时间
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //获取当前时间
            Date now = new Date();
            //将当前时间格式化为指定格式的字符串
            String strNow = dateFormat.format(now);
            //将字符串转换为Date类型
            Date date = dateFormat.parse(strNow);
            //得到创建时间
            tag.setUpdateTime(date);
        } catch (ParseException e){
            e.printStackTrace();
        }

        //修改标签id、标签名、标签的描述信息
        tag.setId(tagVO.getId());
        tag.setName(tagVO.getName());
        tag.setRemark(tagVO.getRemark());

        //把数据插入到数据库
        tagMapper.updateById(tag);
        return ResponseResult.okResult();
    }

    //写博文--查询文章标签的接口
    @Override
    public List<TagVO> listAllTag() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId, Tag::getName);
        List<Tag> list = list(wrapper);
        List<TagVO> tagVOS = BeanCopyUtils.copyBeanList(list, TagVO.class);
        return tagVOS;
    }
}
