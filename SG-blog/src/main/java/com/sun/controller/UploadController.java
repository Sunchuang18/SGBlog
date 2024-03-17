package com.sun.controller;

import com.sun.annotation.mySystemLog;
import com.sun.domain.ResponseResult;
import com.sun.service.OssUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "文件上传的相关接口文档")
public class UploadController {

    @Autowired
    private OssUploadService ossUploadService;

    @PostMapping("/upload")
    //自定义在swagger中请求接口的信息
    @ApiOperation("图片上传到七牛云")
    @mySystemLog(businessName = "图片上传到七牛云")
    public ResponseResult uploadImg(MultipartFile img){
        return ossUploadService.uploadImg(img);
    }
}
