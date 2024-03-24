package com.sun.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//作为Excel表格的列头
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelCategoryVO {

    @ExcelProperty("分类名")
    private String name;

    //描述
    @ExcelProperty("描述")
    private String description;

    //状态：0正常，1禁用
    @ExcelProperty("状态：0正常，1禁用")
    private String status;
}
