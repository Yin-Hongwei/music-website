package com.example.yin.model.request;

import lombok.Data;

import java.util.Date;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/6 14:29
 **/
@Data
public class CommentRequest {
    private Integer id;

    private Integer userId;

    private Integer songId;

    private Integer songListId;

    private String content;

    private Date createTime;

    private Byte nowType;

    private Integer up;//点赞
}
