package com.zjh.clouddisk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * MD5计算工具
 */
public class Md5Util {
    /**
     * 获取一个文件的md5值(可处理大文件)
     *
     * @return md5 value
     */
    public static String getMD5(MultipartFile file) {
        try {
            byte[] uploadBytes = file.getBytes();
            //file->byte[],生成md5
            String md5Hex = DigestUtils.md5Hex(uploadBytes);
            //file->InputStream,生成md5
            String md5Hex1 = DigestUtils.md5Hex(file.getInputStream());
            //对字符串生成md5
            String s = DigestUtils.md5Hex("字符串");
            return md5Hex;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
