package com.sun;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Component
@SpringBootTest
@ConfigurationProperties(prefix = "myoss")//指定读取application.yml文件的myoss属性的数据
public class OSSTest {
    //注意要从application.yml读取属性数据，下面的3个成员变量的名必须对应application.yml的myoss属性的子属性名
    private String ossAccessKey;
    private String ossSecretKey;
    private String ossBucket;
    public void setOssAccessKey(String ossAccessKey) {
        this.ossAccessKey = ossAccessKey;
    }
    public void setOssSecretKey(String ossSecretKey) {
        this.ossSecretKey = ossSecretKey;
    }
    public void setOssBucket(String ossBucket) {
        this.ossBucket = ossBucket;
    }

    @Test
    public void testOss(){
        //构造一个带指定 Region 对象的配置类。七牛云OSS在哪个区域，就调用Region的哪个方法
        Configuration cfg = new Configuration(Region.huabei());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        // 为避免信息暴露，把下面三行的信息添加到application.yml中，并用ConfigurationProperties注解和成员变量获取
        // String accessKey = "your access key";
        // String secretKey = "your secret key";
        // String bucket = "your bucket name";

        //文件名。默认不指定key的情况下，以文件内容的hash值作为文件名
        // String key = null;
        // 新建多目录文件，为：目录1/目录2/文件名
        String key = "2024/3/my01.png";
        // 七牛云的空目录会自动删除

        try {
            //byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            //ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
            //上两行是官方的，在此用自己写的
            InputStream ossInputStream = new FileInputStream("D:\\Project\\Resource\\SGBlog\\UploadFile\\myhead.png");

            Auth auth = Auth.create(ossAccessKey, ossSecretKey);
            String upToken = auth.uploadToken(ossBucket);

            try {
                Response response = uploadManager.put(ossInputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println("上传成功！生成的key是："+putRet.key);
                System.out.println("上传成功！生成的hash是："+putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception e) {
            //ignore
        }
    }
}
