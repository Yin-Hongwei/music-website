package com.example.yin.request;

import lombok.Data;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/6 20:38
 **/
@Data
public class SongListRequest {
    private Integer id;

    private String title;

    private String pic;

    private String style;

    private String introduction;
}
