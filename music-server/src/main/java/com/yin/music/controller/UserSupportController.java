package com.yin.music.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yin.music.model.R;
import com.yin.music.model.request.UserSupportRequest;
import com.yin.music.service.UserSupportService;
import com.yin.music.support.SessionUser;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
@RequestMapping("/userSupport")
public class UserSupportController {

    private final UserSupportService userSupportService;

    @PostMapping("/test")
    public R<?> isUserSupportComment(@RequestBody UserSupportRequest userSupportRequest, HttpSession session) {
        Integer currentUserId = SessionUser.requireUserId(session);
        return userSupportService.isUserSupportComment(userSupportRequest, currentUserId);
    }

    @PostMapping("/insert")
    public R<?> insertCommentSupport(@RequestBody UserSupportRequest userSupportRequest, HttpSession session) {
        Integer currentUserId = SessionUser.requireUserId(session);
        return userSupportService.insertCommentSupport(userSupportRequest, currentUserId);
    }

    @PostMapping("/delete")
    public R<?> deleteCommentSupport(@RequestBody UserSupportRequest userSupportRequest, HttpSession session) {
        Integer currentUserId = SessionUser.requireUserId(session);
        return userSupportService.deleteCommentSupport(userSupportRequest, currentUserId);
    }
}
