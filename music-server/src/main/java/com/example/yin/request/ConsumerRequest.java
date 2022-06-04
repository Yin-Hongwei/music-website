package com.example.yin.request;

import lombok.Data;

import java.util.Date;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/5 19:35
 * 这块 现在尝试把所有有关用户的属性都放入
 **/
@Data
public class ConsumerRequest {
    Integer id;
    String username;
    String oldPassword; //因为会用到用户旧密码 这无所谓的对应即可
    String password;
    Byte sex;
    String phoneNum;
    String email;
    Date birth;
    String introduction;
    String location;
    String avator;
    Date createTime;
}
