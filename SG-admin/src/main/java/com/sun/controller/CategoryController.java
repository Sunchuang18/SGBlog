package com.sun.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.sun.domain.Category;
import com.sun.domain.ResponseResult;
import com.sun.dto.CategoryDto;
import com.sun.enums.AppHttpCodeEnum;
import com.sun.service.CategoryService;
import com.sun.utils.BeanCopyUtils;
import com.sun.utils.WebUtils;
import com.sun.vo.CategoryVO;
import com.sun.vo.ExcelCategoryVO;
import com.sun.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //写博客--查询分类的接口
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVO> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }

    //分页查询分类列表
    @GetMapping("/list")
    public ResponseResult list(Category category, Integer pageNum, Integer pageSize){
        PageVO pageVO = categoryService.selectCategoryPage(category, pageNum, pageSize);
        return ResponseResult.okResult(pageVO);
    }

    //增加文章的分类
    @PostMapping
    public ResponseResult add(@RequestBody CategoryDto categoryDto){
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        categoryService.save(category);
        return ResponseResult.okResult();
    }

    //删除文章的分类
    @DeleteMapping(value = "/{id}")
    public ResponseResult remove(@PathVariable(value = "id")Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }

    //修改文章的分类
    // ①根据分类的id来查询分类
    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        Category category = categoryService.getById(id);
        return ResponseResult.okResult(category);
    }
    // ②根据分类的id来修改分类
    @PutMapping
    public ResponseResult edit(@RequestBody Category category){
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    //把分类数据写入到Excel并导出
    @PreAuthorize("@ps.hasPermission('content:category:export')")//权限控制。ps是PermissionService类的bean名称
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头，下载下来的Excel文件叫“分类.xlsx”
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            //获取需要导出的数据
            List<Category> categoryList = categoryService.list();

            List<ExcelCategoryVO> excelCategoryVOS = BeanCopyUtils.copyBeanList(categoryList, ExcelCategoryVO.class);
            //把数据写入到Excel中，也就是把ExcelCategoryVO实体类的字段作为Excel表格的列头
            //sheet方法里的字符串是Excel表格左下角工作簿的名字
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVO.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("文章分类")
                    .doWrite(excelCategoryVOS);
        } catch (Exception e){
            //如果出现异常，就返回失败的json数据
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

}
