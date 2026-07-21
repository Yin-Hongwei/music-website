package com.yin.music.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yin.music.model.R;
import com.yin.music.model.request.UserSupportRequest;
import com.yin.music.service.UserSupportService;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/11 16:07
 **/
@RequiredArgsConstructor
@RestController
@RequestMapping("/userSupport")
public class UserSupportController {

    private final UserSupportService userSupportService;

    @PostMapping("/test")
    public R<?> isUserSupportComment(@RequestBody UserSupportRequest userSupportRequest) {
        return userSupportService.isUserSupportComment(userSupportRequest);
    }

    @PostMapping("/insert")
    public R<?> insertCommentSupport(@RequestBody UserSupportRequest userSupportRequest) {
        return userSupportService.insertCommentSupport(userSupportRequest);
    }

    @PostMapping("/delete")
    public R<?> deleteCommentSupport(@RequestBody UserSupportRequest userSupportRequest) {
        return userSupportService.deleteCommentSupport(userSupportRequest);
    }
}
