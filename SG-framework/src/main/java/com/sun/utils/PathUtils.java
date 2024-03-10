package com.sun.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

//对原始文件名进行修改，并修改存放目录
public class PathUtils {
    public static String generateFilePath(String fileName){
        //根据日期生成路径
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String datePath = simpleDateFormat.format(new Date());
        //uuid作为文件名。生成无“-“的uuid字符串
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        //后缀和文件名一致
        int index = fileName.lastIndexOf(".");//查找字符串fileName中最后一个点号（.）的索引位置
        //分隔得到后缀 test.jpg -> .jpg
        String fileType = fileName.substring(index);

        return new StringBuilder().append(datePath).append(uuid).append(fileType).toString();
    }
}
