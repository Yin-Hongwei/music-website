package com.example.yin.model.request;

import lombok.Data;

import java.util.Date;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/6 19:11
 **/
@Data
public class SongRequest {

    private Integer id;

    private Integer singerId;

    private String name;

    private String introduction;

    private Date createTime;

    private Date updateTime;

    private String pic;

    private String lyric;

    private String url;
}
