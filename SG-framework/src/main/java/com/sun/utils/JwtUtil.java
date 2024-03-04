package com.sun.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

//JWT工具类
public class JwtUtil {
    //有效期
    public static final Long JWT_TTL = 70*60*60*1000L;//60*60*1000 一个小时
    //设置秘钥铭文
    public static final String JWT_KEY = "sunKey";

    //生成token
    public static String getUUID(){
        //生成一段没有“-”的UUID被toString后的字符串，并将其赋值为token
        String token = UUID.randomUUID().toString().replace("-","");
        return token;
    }

    /**
     * 生成jwt
     * @param subject  token中要存放的数据（json格式）
     * @param ttlMillis  token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis){
        //设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid){
        //SignatureAlgorithm 是一个枚举类型，而 HS256 是这个枚举类型中的一个值
        //使用HS256算法生成JWT的签名部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null){
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);

        return Jwts.builder()
                // 唯一的ID
                .setId(uuid)
                // 主题 可以是JSON数据
                .setSubject(subject)
                // 签发者
                .setIssuer("SC")
                // 签发时间
                .setIssuedAt(now)
                // 使用HS256对称加密算法签名，第二个参数为秘钥
                .signWith(signatureAlgorithm, secretKey)
                .setExpiration(expDate);
    }

    /**
     * 创建token
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis){
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);
        return builder.compact();
    }

    public static void main(String[] args) throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjYWM2ZDVhZi1mNjVlLTQ0MDAtYjcxMi0zYWEwOGIyOTIwYjQiLCJzdWIiOiJzZyIsImlzcyI6InNnIiwiaWF0IjoxNjM4MTA2NzEyLCJleHAiOjE2MzgxMTAzMTJ9.JVsSbkP94wuczb4QryQbAke3ysBDIL5ou8fWsbt_ebg";
        Claims claims = parseJWT(token);
        System.out.println(claims);
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return
     */
    public static SecretKey generalKey(){
        /*
        从 JwtUtil.JWT_KEY 获取 Base64 编码的字符串，使用 Base64.getDecoder() 获取一个 Base64 解码器。
        使用解码器将 JwtUtil.JWT_KEY 解码成原始的字节数据，将解码后的字节数据存储在 encodeKey 这个字节数组中。
         */
        byte[] encodeKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        /*
        使用 encodeKey 作为密钥的原始数据。从 encodeKey 的开始位置（偏移量 0）提取全部字节。
        使用 AES 算法将这些字节创建为一个 SecretKey 实例。
         */
        SecretKey key = new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");
        return key;
    }

    /**
     * 解析
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception{
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

}
