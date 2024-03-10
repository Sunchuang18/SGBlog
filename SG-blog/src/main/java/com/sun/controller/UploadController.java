package com.sun.controller;

import com.sun.domain.ResponseResult;
import com.sun.service.OssUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private OssUploadService ossUploadService;

    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        return ossUploadService.uploadImg(img);
    }
}
