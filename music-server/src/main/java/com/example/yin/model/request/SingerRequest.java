package com.example.yin.model.request;

import lombok.Data;

import java.util.Date;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/6 20:14
 **/
@Data
public class SingerRequest {
    private Integer id;

    private String name;

    private Byte sex;

    private String pic;

    private Date birth;

    private String location;

    private String introduction;
}
