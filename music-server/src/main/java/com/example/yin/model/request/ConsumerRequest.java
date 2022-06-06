package com.example.yin.model.request;

import lombok.Data;

import java.util.Date;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/5 19:35
 * 这块 现在尝试把所有有关用户的属性都放入
 **/
@Data
public class ConsumerRequest {
    private Integer id;

    private String username;

    private String oldPassword; //因为会用到用户旧密码 这无所谓的对应即可

    private String password;

    private Byte sex;

    private String phoneNum;

    private String email;

    private Date birth;

    private String introduction;

    private String location;

    private String avator;

    private Date createTime;
}
