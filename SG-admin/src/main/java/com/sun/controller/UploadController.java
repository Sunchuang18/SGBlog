package com.sun.controller;

import com.sun.domain.ResponseResult;
import com.sun.service.OssUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private OssUploadService uploadService;

    //图片上传
    @PostMapping("/upload")
    public ResponseResult uploadImp(@RequestParam("img") MultipartFile multipartFile){
        try {
            return uploadService.uploadImg(multipartFile);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("文件上传失败");
        }
    }
}
