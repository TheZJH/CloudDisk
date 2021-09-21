package com.zjh.clouddisk.util;

/**
 * @author TheZJH
 * @version 1.0
 */
public class GetType {
    /**
     * 根据文件后缀名获取对应的类型
     * @param type
     * @return int 1:文本类型   2:图像类型  3:视频类型  4:音乐类型  5:其他类型
     */
    public static int getType(String type) {
        if (".chm".equals(type)||".txt".equals(type)||".xmind".equals(type)||".xlsx".equals(type)||".md".equals(type)
                ||".doc".equals(type)||".docx".equals(type)||".pptx".equals(type)
                ||".wps".equals(type)||".word".equals(type)||".html".equals(type)||".pdf".equals(type)){
            return  1;
        }else if (".bmp".equals(type)||".gif".equals(type)||".jpg".equals(type)||".ico".equals(type)||".vsd".equals(type)
                ||".pic".equals(type)||".png".equals(type)||".jepg".equals(type)||".jpeg".equals(type)||".webp".equals(type)
                ||".svg".equals(type)){
            return 2;
        } else if (".avi".equals(type)||".mov".equals(type)||".qt".equals(type)
                ||".asf".equals(type)||".rm".equals(type)||".navi".equals(type)||".wav".equals(type)
                ||".mp4".equals(type)||".mkv".equals(type)||".webm".equals(type)){
            return 3;
        } else if (".mp3".equals(type)||".wma".equals(type)){
            return 4;
        } else {
            return 5;
        }
    }
}
