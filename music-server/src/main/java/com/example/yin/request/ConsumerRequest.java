package com.example.yin.request;

import lombok.Data;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/5 19:35
 * 这块 现在尝试把所有有关用户的属性都放入
 **/
@Data
public class ConsumerRequest {
    String id;
    String username;
    String password;
    String sex;
    String phoneNum;
    String email;
    String birth;
    String introduction;
    String location;
    String avator;
}
