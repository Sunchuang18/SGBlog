package com.sun.service;

import com.sun.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface OssUploadService {
    //图片上传到七牛云
    ResponseResult uploadImg(MultipartFile img);
}
