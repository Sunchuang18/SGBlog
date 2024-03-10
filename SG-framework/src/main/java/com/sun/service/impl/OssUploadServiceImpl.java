package com.sun.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.sun.domain.ResponseResult;
import com.sun.enums.AppHttpCodeEnum;
import com.sun.exception.SystemException;
import com.sun.service.OssUploadService;
import com.sun.utils.PathUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@Data
@ConfigurationProperties(prefix = "myoss")//指定读取application.yml文件的myoss属性的数据
//图片上传到七牛云
public class OssUploadServiceImpl implements OssUploadService {
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();

        //获取文件大小
        long fileSize = img.getSize();

        //判断文件大小是否超过2MB（2MB=2*1024*1024 bytes）
        if (fileSize > 2*1024*1024){
            //抛出文件大小超过限制异常
            throw new SystemException(AppHttpCodeEnum.FILE_SIZE_ERROR);
        }
        //对原始文件名进行判断大小。只能上传png或jpg文件
        if (!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        //满足判断条件，上传文件至七牛云OSS，并得到一个图片外链访问地址
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url = uploadOss(img,filePath);

        //把得到的外链地址返回给前端
        return ResponseResult.okResult(url);
    }

    //-----------------------------上传文件到七牛云-----------------------------

    //注意要从application.yml读取属性数据，下面的3个成员变量的名必须对应application.yml的myoss属性的子属性名
    private String ossAccessKey;
    private String ossSecretKey;
    private String ossBucket;

    //上传文件的具体代码
    private String uploadOss(MultipartFile imgFile, String filePath){
        //构造一个带指定 Region 对象的配置类。七牛云OSS在哪个区域，就调用Region的哪个方法
        Configuration cfg = new Configuration(Region.huabei());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        UploadManager uploadManager = new UploadManager(cfg);

        //...生成上传凭证，然后准备上传
        // 为避免信息暴露，把下面三行的信息添加到application.yml中，并用ConfigurationProperties注解和成员变量获取
        // String accessKey = "your access key";
        // String secretKey = "your secret key";
        // String bucket = "your bucket name";

        //文件名。默认不指定key的情况下，以文件内容的hash值作为文件名
        // String key = null;
        // 新建多目录文件，为：目录1/目录2/文件名
        String key = filePath;
        // 七牛云的空目录会自动删除

        try {
            //byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            //ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
            //上两行是官方的，在此用自己写的
            //把前端传来的文件转换成InputStream对象
            InputStream ossInputStream = imgFile.getInputStream();

            Auth auth = Auth.create(ossAccessKey, ossSecretKey);
            String upToken = auth.uploadToken(ossBucket);

            try {
                //把前端传来的ossInputStream图片上传到七牛云
                Response response = uploadManager.put(ossInputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println("上传成功！生成的key是："+putRet.key);
                System.out.println("上传成功！生成的hash是："+putRet.hash);
                return "http://sa42y9m6z.hb-bkt.clouddn.com/" + key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        }catch (Exception e){
            //ignore
        }
        return "上传失败";
    }
}
