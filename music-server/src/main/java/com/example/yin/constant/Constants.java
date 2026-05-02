package com.example.yin.constant;

public class Constants {
    /* 歌曲图片，歌手图片，歌曲文件，歌单图片等文件的存放路径 */
    public static String PROJECT_PATH = System.getProperty("user.dir");
    // 注意：file: 协议后的路径在Unix/Linux/Mac上使用 /，在Windows上Spring会自动处理
    public static String AVATAR_IMAGES_PATH = "file:" + PROJECT_PATH + "/img/avatorImages/";
    public static String SONGLIST_PIC_PATH = "file:" + PROJECT_PATH + "/img/songListPic/";
    public static String SONG_PIC_PATH = "file:" + PROJECT_PATH + "/img/songPic/";
    public static String SONG_PATH = "file:" + PROJECT_PATH + "/song/";
    public static String SINGER_PIC_PATH = "file:" + PROJECT_PATH + "/img/singerPic/";
    public static String BANNER_PIC_PATH = "file:" + PROJECT_PATH + "/img/swiper/";


    /* 盐值加密 */
    public static String SALT = "zyt";
}
