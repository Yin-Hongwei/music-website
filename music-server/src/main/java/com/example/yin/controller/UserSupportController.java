package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.request.UserSupportRequest;
import com.example.yin.service.UserSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/11 16:07
 **/
@RestController
@RequestMapping("/userSupport")
public class UserSupportController {

    @Autowired
    UserSupportService userSupportService;

    @PostMapping("/test")
    public R isUserSupportComment(@RequestBody UserSupportRequest userSupportRequest) {
        return userSupportService.isUserSupportComment(userSupportRequest);
    }

    @PostMapping("/insert")
    public R insertCommentSupport(@RequestBody UserSupportRequest userSupportRequest) {
        return userSupportService.insertCommentSupport(userSupportRequest);
    }

    @PostMapping("/delete")
    public R deleteCommentSupport(@RequestBody UserSupportRequest userSupportRequest) {
        return userSupportService.deleteCommentSupport(userSupportRequest);
    }
}
